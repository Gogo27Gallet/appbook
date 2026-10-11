package com.example.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.BookCoverService
import com.example.data.OnboardingBookCandidate
import com.example.data.SampleData
import com.example.model.BookCoverStyle
import com.example.model.BookItem
import com.example.model.CollectionItem
import com.example.model.DailyReadingPoint
import com.example.model.DockTab
import com.example.model.ReadingStats
import com.example.model.ReadingStatus
import com.example.model.UserPreferences
import com.example.ui.components.SortOption
import com.example.util.EpubParser
import com.example.util.HapticManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID

class CollectionsViewModel : ViewModel() {

    private val _collections = MutableStateFlow(SampleData.initialCollections)
    val collections: StateFlow<List<CollectionItem>> = _collections.asStateFlow()

    private val _selectedCollection = MutableStateFlow<CollectionItem?>(null)
    val selectedCollection: StateFlow<CollectionItem?> = _selectedCollection.asStateFlow()

    private val _currentTab = MutableStateFlow(DockTab.COLLECTIONS)
    val currentTab: StateFlow<DockTab> = _currentTab.asStateFlow()

    // Library books
    private val _libraryBooks = MutableStateFlow(SampleData.topSelectionsForYou)
    val libraryBooks: StateFlow<List<BookItem>> = _libraryBooks.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.RECENT)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    private val _selectedBook = MutableStateFlow<BookItem?>(null)
    val selectedBook: StateFlow<BookItem?> = _selectedBook.asStateFlow()

    // Active Book for stats and reading session
    private val _activeReadingBook = MutableStateFlow<BookItem>(SampleData.atomicHabits)
    val activeReadingBook: StateFlow<BookItem> = _activeReadingBook.asStateFlow()

    private val _isReadingFullScreen = MutableStateFlow(false)
    val isReadingFullScreen: StateFlow<Boolean> = _isReadingFullScreen.asStateFlow()

    private val _showFilterSheet = MutableStateFlow(false)
    val showFilterSheet: StateFlow<Boolean> = _showFilterSheet.asStateFlow()

    private val _showAddCollectionDialog = MutableStateFlow(false)
    val showAddCollectionDialog: StateFlow<Boolean> = _showAddCollectionDialog.asStateFlow()

    private val _showAddBookDialog = MutableStateFlow(false)
    val showAddBookDialog: StateFlow<Boolean> = _showAddBookDialog.asStateFlow()

    // Onboarding State
    private val _isOnboardingCompleted = MutableStateFlow(false)
    val isOnboardingCompleted: StateFlow<Boolean> = _isOnboardingCompleted.asStateFlow()

    private val _userPreferences = MutableStateFlow(UserPreferences())
    val userPreferences: StateFlow<UserPreferences> = _userPreferences.asStateFlow()

    // Reading Stats & Live Session
    private val _readingStats = MutableStateFlow(ReadingStats())
    val readingStats: StateFlow<ReadingStats> = _readingStats.asStateFlow()

    private val _isSessionRunning = MutableStateFlow(false)
    val isSessionRunning: StateFlow<Boolean> = _isSessionRunning.asStateFlow()

    private val _sessionSeconds = MutableStateFlow(0)
    val sessionSeconds: StateFlow<Int> = _sessionSeconds.asStateFlow()

    private val _sessionPagesRead = MutableStateFlow(0)
    val sessionPagesRead: StateFlow<Int> = _sessionPagesRead.asStateFlow()

    private var timerJob: Job? = null

    fun selectCollection(collection: CollectionItem) {
        _selectedCollection.value = collection
    }

    fun closeCollectionDetail() {
        _selectedCollection.value = null
    }

    fun setTab(tab: DockTab) {
        _currentTab.value = tab
        _selectedCollection.value = null
        _isReadingFullScreen.value = false
    }

    /**
     * User clicks "Lire" or opens Stats on a book:
     * Navigates directly to the rich Stats & Session section for this book!
     */
    fun openBookStats(book: BookItem) {
        _activeReadingBook.value = book
        _selectedBook.value = null
        _selectedCollection.value = null
        _isReadingFullScreen.value = false
        _currentTab.value = DockTab.STATS
    }

    fun launchReader(book: BookItem) {
        _activeReadingBook.value = book
        _isReadingFullScreen.value = true
    }

    fun stopReading() {
        _isReadingFullScreen.value = false
    }

    // Live Reading Session Controls
    fun toggleReadingSession() {
        if (_isSessionRunning.value) {
            pauseReadingSession()
        } else {
            startReadingSession()
        }
    }

    fun startReadingSession() {
        _isSessionRunning.value = true
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive && _isSessionRunning.value) {
                delay(1000)
                _sessionSeconds.update { it + 1 }
                // Update total reading time every 60 seconds
                if (_sessionSeconds.value % 60 == 0) {
                    _readingStats.update { current ->
                        current.copy(
                            totalReadingTimeMinutes = current.totalReadingTimeMinutes + 1
                        )
                    }
                }
            }
        }
    }

    fun pauseReadingSession() {
        _isSessionRunning.value = false
        timerJob?.cancel()
    }

    fun resetReadingSession() {
        pauseReadingSession()
        _sessionSeconds.value = 0
        _sessionPagesRead.value = 0
    }

    fun incrementSessionPages(delta: Int) {
        val newPages = (_sessionPagesRead.value + delta).coerceAtLeast(0)
        _sessionPagesRead.value = newPages

        // Also update book current page
        val activeBook = _activeReadingBook.value
        val updatedPage = (activeBook.currentPage + delta).coerceIn(0, activeBook.pageCount)
        val updatedBook = activeBook.copy(currentPage = updatedPage)
        _activeReadingBook.value = updatedBook
        updateBookInCollections(updatedBook)

        // Recalculate reading metrics
        val totalPages = (_readingStats.value.totalPagesRead + delta).coerceAtLeast(1)
        val totalMinutes = _readingStats.value.totalReadingTimeMinutes.coerceAtLeast(1)
        val pagesPerMin = totalPages.toFloat() / totalMinutes.toFloat()
        val wordsPerMin = (pagesPerMin * 270f).toInt().coerceAtLeast(100)

        _readingStats.update { current ->
            current.copy(
                totalPagesRead = totalPages,
                totalWordsRead = totalPages * 270,
                pagesPerMinute = String.format(java.util.Locale.US, "%.2f", pagesPerMin).toFloat(),
                wordsPerMinute = wordsPerMin
            )
        }
    }

    // Onboarding Completion & Initialization
    fun completeOnboarding(
        preferences: UserPreferences,
        selectedBooks: List<OnboardingBookCandidate>
    ) {
        _userPreferences.value = preferences

        // Convert candidate books to completed BookItems and add to library
        val completedBookItems = selectedBooks.map { candidate ->
            BookItem(
                id = "read_${candidate.id}_${UUID.randomUUID().toString().take(6)}",
                title = candidate.title,
                author = candidate.author,
                subtitle = "Lu en entier",
                coverStyle = BookCoverStyle.CUSTOM,
                primaryColorHex = candidate.primaryColorHex,
                coverImageUrl = candidate.coverUrl,
                rating = 4.9f,
                pageCount = candidate.pageCount,
                currentPage = candidate.pageCount,
                category = candidate.category,
                description = "Livre lu et apprécié lors de votre parcours de lecture.",
                status = ReadingStatus.COMPLETED
            )
        }

        if (completedBookItems.isNotEmpty()) {
            _libraryBooks.update { current ->
                (completedBookItems + current).distinctBy { it.title }
            }

            // Create or update a "Livres Déjà Lus" collection
            val readCollection = CollectionItem(
                id = "c_already_read",
                title = "Mes Livres Lus",
                subtitle = "${completedBookItems.size} livres terminés",
                lastUpdatedText = "Récemment",
                bookCount = completedBookItems.size,
                featuredBooks = completedBookItems.take(5),
                allBooks = completedBookItems
            )
            _collections.update { listOf(readCollection) + it }
        }

        // Calculate initial stats based on onboarding answers
        val totalPagesCalculated = (selectedBooks.sumOf { it.pageCount } + (preferences.readBooksCount * 280)).coerceAtLeast(1200)
        val estimatedTotalMinutes = (totalPagesCalculated * 0.82f).toInt()
        val ppm = totalPagesCalculated.toFloat() / estimatedTotalMinutes.toFloat()
        val wpm = (ppm * 275f).toInt()

        _readingStats.value = ReadingStats(
            totalReadingTimeMinutes = estimatedTotalMinutes,
            currentStreakDays = 7,
            bestStreakDays = 14,
            totalPagesRead = totalPagesCalculated,
            totalWordsRead = totalPagesCalculated * 275,
            pagesPerMinute = String.format(java.util.Locale.US, "%.2f", ppm).toFloat(),
            wordsPerMinute = wpm,
            booksCompleted = maxOf(selectedBooks.size, preferences.readBooksCount),
            yearlyGoalBooks = preferences.yearlyGoalCount,
            weeklyMinutes = listOf(
                DailyReadingPoint("Lun", 35),
                DailyReadingPoint("Mar", 40),
                DailyReadingPoint("Mer", 25),
                DailyReadingPoint("Jeu", 45),
                DailyReadingPoint("Ven", 30),
                DailyReadingPoint("Sam", 55),
                DailyReadingPoint("Dim", preferences.dailyGoalMinutes)
            )
        )

        _isOnboardingCompleted.value = true
        _currentTab.value = DockTab.COLLECTIONS
    }

    fun resetOnboarding() {
        _isOnboardingCompleted.value = false
    }

    fun setHapticsEnabled(enabled: Boolean) {
        HapticManager.isHapticsEnabled = enabled
    }

    fun importEpub(context: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val book = EpubParser.parseEpub(context, uri)
            if (book != null) {
                _libraryBooks.update { listOf(book) + it }
                _collections.update { list ->
                    list.mapIndexed { idx, col ->
                        if (idx == 0) col.copy(
                            allBooks = listOf(book) + col.allBooks,
                            bookCount = col.bookCount + 1
                        ) else col
                    }
                }
                _selectedCollection.update { col ->
                    col?.copy(allBooks = listOf(book) + col.allBooks, bookCount = col.bookCount + 1)
                }
                if (book.coverImageBytes == null) {
                    val onlineCover = BookCoverService.searchCoverUrl(book.title)
                    if (onlineCover != null) {
                        val withCover = book.copy(coverImageUrl = onlineCover)
                        updateBookInCollections(withCover)
                    }
                }
                _activeReadingBook.value = book
                _currentTab.value = DockTab.STATS
            }
        }
    }

    fun reorderShelfBooks(collectionId: String, newOrder: List<BookItem>) {
        _collections.update { list ->
            list.map { col ->
                if (col.id == collectionId) {
                    col.copy(featuredBooks = newOrder)
                } else col
            }
        }
        _selectedCollection.update { col ->
            if (col?.id == collectionId) {
                col.copy(featuredBooks = newOrder)
            } else col
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSortOption(option: SortOption) {
        _sortOption.value = option
        _collections.update { list ->
            when (option) {
                SortOption.RECENT -> SampleData.initialCollections
                SortOption.ALPHABETICAL -> list.sortedBy { it.title }
                SortOption.BOOK_COUNT -> list.sortedByDescending { it.bookCount }
            }
        }
    }

    fun openBookDetail(book: BookItem) {
        _selectedBook.value = book
    }

    fun closeBookDetail() {
        _selectedBook.value = null
    }

    fun setFilterSheetVisible(visible: Boolean) {
        _showFilterSheet.value = visible
    }

    fun setAddCollectionDialogVisible(visible: Boolean) {
        _showAddCollectionDialog.value = visible
    }

    fun setAddBookDialogVisible(visible: Boolean) {
        _showAddBookDialog.value = visible
    }

    fun updateBookStatus(status: ReadingStatus) {
        val currentBook = _selectedBook.value ?: return
        val updated = currentBook.copy(status = status)
        _selectedBook.value = updated
        updateBookInCollections(updated)
    }

    fun updateBookProgress(pages: Int) {
        val currentBook = _selectedBook.value ?: return
        val updated = currentBook.copy(currentPage = pages)
        _selectedBook.value = updated
        updateBookInCollections(updated)
    }

    private fun updateBookInCollections(updatedBook: BookItem) {
        _collections.update { list ->
            list.map { col ->
                col.copy(
                    featuredBooks = col.featuredBooks.map { if (it.id == updatedBook.id) updatedBook else it },
                    allBooks = col.allBooks.map { if (it.id == updatedBook.id) updatedBook else it }
                )
            }
        }
        _libraryBooks.update { list ->
            list.map { if (it.id == updatedBook.id) updatedBook else it }
        }
        _selectedCollection.update { col ->
            col?.copy(
                featuredBooks = col.featuredBooks.map { if (it.id == updatedBook.id) updatedBook else it },
                allBooks = col.allBooks.map { if (it.id == updatedBook.id) updatedBook else it }
            )
        }
        if (_activeReadingBook.value.id == updatedBook.id) {
            _activeReadingBook.value = updatedBook
        }
    }

    fun addCollection(title: String, subtitle: String) {
        val newCollection = CollectionItem(
            id = "c_${UUID.randomUUID()}",
            title = title,
            subtitle = subtitle,
            lastUpdatedText = "À l'instant",
            bookCount = 3,
            featuredBooks = listOf(SampleData.orphanedBelievers, SampleData.nextOfKin, SampleData.conform),
            allBooks = listOf(SampleData.orphanedBelievers, SampleData.nextOfKin, SampleData.conform)
        )
        _collections.update { listOf(newCollection) + it }
        _showAddCollectionDialog.value = false
    }

    fun addBookToCurrentCollection(title: String, author: String, pages: Int) {
        val current = _selectedCollection.value ?: return
        val colors = listOf(0xFF2E7D32L, 0xFFC2185BL, 0xFF1565C0L, 0xFFE65100L, 0xFF4527A0L)
        val randomColor = colors.random()
        val newBook = BookItem(
            id = "b_${UUID.randomUUID()}",
            title = title.uppercase(),
            author = author.uppercase(),
            subtitle = "Ajouté à l'étagère",
            coverStyle = BookCoverStyle.CUSTOM,
            primaryColorHex = randomColor,
            rating = 4.8f,
            pageCount = pages,
            currentPage = 0,
            category = current.title,
            quote = "Chaque livre ouvert est une nouvelle aventure.",
            description = "Un livre ajouté à votre collection personnelle.",
            status = ReadingStatus.WANT_TO_READ
        )

        viewModelScope.launch(Dispatchers.IO) {
            val coverUrl = BookCoverService.searchCoverUrl(title)
            if (coverUrl != null) {
                updateBookInCollections(newBook.copy(coverImageUrl = coverUrl))
            }
        }

        val updatedAllBooks = current.allBooks + newBook
        val updatedFeatured = if (current.featuredBooks.size < 5) current.featuredBooks + newBook else current.featuredBooks
        val updatedCol = current.copy(
            bookCount = current.bookCount + 1,
            featuredBooks = updatedFeatured,
            allBooks = updatedAllBooks
        )

        _selectedCollection.value = updatedCol
        _collections.update { list ->
            list.map { if (it.id == updatedCol.id) updatedCol else it }
        }
        _libraryBooks.update { listOf(newBook) + it }
        _showAddBookDialog.value = false
    }

    fun getAllBooks(): List<BookItem> {
        return (_libraryBooks.value + _collections.value.flatMap { it.allBooks }).distinctBy { it.id }
    }
}
