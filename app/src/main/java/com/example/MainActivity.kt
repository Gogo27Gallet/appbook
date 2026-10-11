package com.example

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.DockTab
import com.example.ui.components.AddBookDialog
import com.example.ui.components.AddCollectionDialog
import com.example.ui.components.BookDetailSheet
import com.example.ui.components.BottomFloatingDock
import com.example.ui.components.FilterBottomSheet
import com.example.ui.screens.CollectionDetailScreen
import com.example.ui.screens.CollectionsScreen
import com.example.ui.screens.LibraryView
import com.example.ui.screens.ReaderScreen
import com.example.ui.screens.SettingsView
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.CollectionsViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: CollectionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CollectionsApp(viewModel)
            }
        }
    }
}

@Composable
fun CollectionsApp(viewModel: CollectionsViewModel) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val collections by viewModel.collections.collectAsStateWithLifecycle()
    val selectedCollection by viewModel.selectedCollection.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val libraryBooks by viewModel.libraryBooks.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val sortOption by viewModel.sortOption.collectAsStateWithLifecycle()
    val selectedBook by viewModel.selectedBook.collectAsStateWithLifecycle()
    val activeReadingBook by viewModel.activeReadingBook.collectAsStateWithLifecycle()
    val isReadingFullScreen by viewModel.isReadingFullScreen.collectAsStateWithLifecycle()
    val showFilterSheet by viewModel.showFilterSheet.collectAsStateWithLifecycle()
    val showAddCollectionDialog by viewModel.showAddCollectionDialog.collectAsStateWithLifecycle()
    val showAddBookDialog by viewModel.showAddBookDialog.collectAsStateWithLifecycle()

    // Real EPUB file picker contract
    val epubPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            viewModel.importEpub(context, it)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundLight
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
        ) {
            // Screen Navigation with smooth transitions ("ajoutes de jholies transitions agreables")
            AnimatedContent(
                targetState = when {
                    isReadingFullScreen -> "READER"
                    selectedCollection != null -> "DETAIL_${selectedCollection!!.id}"
                    else -> "TAB_${currentTab.name}"
                },
                transitionSpec = {
                    (fadeIn(animationSpec = tween(220)) + scaleIn(initialScale = 0.98f, animationSpec = tween(220)))
                        .togetherWith(fadeOut(animationSpec = tween(160)) + scaleOut(targetScale = 0.98f, animationSpec = tween(160)))
                },
                label = "screen_transition"
            ) { targetScreen ->
                when {
                    targetScreen == "READER" -> {
                        // Full e-reader mode ("vrai truc pour lire pour l ex")
                        ReaderScreen(
                            book = activeReadingBook,
                            onBack = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.stopReading()
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    selectedCollection != null -> {
                        // Screenshot 2: Collection Detail Screen
                        CollectionDetailScreen(
                            collection = selectedCollection!!,
                            onBack = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.closeCollectionDetail()
                            },
                            onBookClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.openBookDetail(it)
                            },
                            onAddBookToShelf = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.setAddBookDialogVisible(true)
                            },
                            onShelfReordered = { newOrder ->
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                viewModel.reorderShelfBooks(selectedCollection!!.id, newOrder)
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    else -> {
                        when (currentTab) {
                            DockTab.COLLECTIONS -> {
                                // Screenshot 1: Collections Main Screen (HOME)
                                CollectionsScreen(
                                    collections = collections,
                                    searchQuery = searchQuery,
                                    onSearchQueryChange = { viewModel.setSearchQuery(it) },
                                    onOpenCollection = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.selectCollection(it)
                                    },
                                    onOpenFilter = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.setFilterSheetVisible(true)
                                    },
                                    onAddNewCollection = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.setAddCollectionDialogVisible(true)
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            DockTab.LIBRARY -> {
                                // Screenshot_20261011_163353_X.jpg: My Library
                                LibraryView(
                                    books = libraryBooks,
                                    onBookClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.openBookDetail(it)
                                    },
                                    onImportEpubClick = {
                                        epubPickerLauncher.launch("*/*")
                                    },
                                    onFilterClick = {
                                        viewModel.setFilterSheetVisible(true)
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            DockTab.READER -> {
                                ReaderScreen(
                                    book = activeReadingBook,
                                    onBack = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.setTab(DockTab.COLLECTIONS)
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            DockTab.SETTINGS -> {
                                SettingsView(
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }

            // Floating Bottom Dock (hidden only during active full-screen reader)
            if (!isReadingFullScreen) {
                BottomFloatingDock(
                    currentTab = currentTab,
                    onTabSelected = { tab ->
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        viewModel.setTab(tab)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .padding(bottom = 18.dp)
                )
            }

            // Book Detail Bottom Sheet
            selectedBook?.let { book ->
                BookDetailSheet(
                    book = book,
                    onDismiss = { viewModel.closeBookDetail() },
                    onUpdateStatus = { status -> viewModel.updateBookStatus(status) },
                    onUpdateProgress = { pages -> viewModel.updateBookProgress(pages) },
                    onStartReading = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.startReading(book)
                    }
                )
            }

            // Filter Bottom Sheet
            if (showFilterSheet) {
                FilterBottomSheet(
                    currentSort = sortOption,
                    onSortSelected = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        viewModel.setSortOption(it)
                    },
                    onDismiss = { viewModel.setFilterSheetVisible(false) }
                )
            }

            // Add Collection Dialog
            if (showAddCollectionDialog) {
                AddCollectionDialog(
                    onDismiss = { viewModel.setAddCollectionDialogVisible(false) },
                    onConfirm = { title, subtitle -> viewModel.addCollection(title, subtitle) }
                )
            }

            // Add Book Dialog
            if (showAddBookDialog) {
                AddBookDialog(
                    onDismiss = { viewModel.setAddBookDialogVisible(false) },
                    onConfirm = { title, author, pages ->
                        viewModel.addBookToCurrentCollection(title, author, pages)
                    }
                )
            }
        }
    }
}
