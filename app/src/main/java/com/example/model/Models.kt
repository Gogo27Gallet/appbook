package com.example.model

enum class BookCoverStyle {
    ORPHANED_BELIEVERS,
    NEXT_OF_KIN,
    CONFORM,
    TRUST,
    ELECTRIC_CITY,
    PRACTICAL_UNCERTAINTY,
    NERVOUS_SYSTEM,
    THE_MAGIC,
    RICHEST_MAN_BABYLON,
    TOO_BIG_TO_FAIL,
    POWER_OF_NOW,
    MANS_SEARCH,
    POWER_OF_DISCIPLINE,
    ATOMIC_HABITS,
    BOOK_OF_FIVE_RINGS,
    ONE_CHARBONNEAU,
    CUSTOM
}

enum class ReadingStatus(val label: String) {
    READING("Currently Reading"),
    COMPLETED("Completed"),
    WANT_TO_READ("Want to Read")
}

data class BookChapter(
    val number: Int,
    val title: String,
    val paragraphs: List<String>
)

data class BookItem(
    val id: String,
    val title: String,
    val author: String,
    val subtitle: String = "",
    val coverStyle: BookCoverStyle,
    val primaryColorHex: Long = 0xFFC72828,
    val coverImageUrl: String? = null,
    val coverImageBytes: ByteArray? = null,
    val rating: Float = 4.8f,
    val pageCount: Int = 320,
    val currentPage: Int = 184,
    val category: String = "Spiritual",
    val quote: String = "",
    val description: String = "",
    val isTopShelf: Boolean = false,
    val status: ReadingStatus = ReadingStatus.READING,
    val chapters: List<BookChapter> = emptyList(),
    val isEpubImported: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as BookItem
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}

data class CollectionItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val lastUpdatedText: String,
    val bookCount: Int,
    val featuredBooks: List<BookItem>,
    val allBooks: List<BookItem>
)

enum class DockTab {
    LIBRARY,
    COLLECTIONS,
    STATS,
    SETTINGS
}

data class UserPreferences(
    val archetypeTitle: String = "Le Dévoreur de Livres",
    val favoriteTimeOfDay: String = "Soirée au calme",
    val preferredGenres: List<String> = listOf("Développement personnel", "Philosophie", "Science-Fiction"),
    val booksReadLastYear: String = "6 à 12 livres",
    val yearlyGoalCount: Int = 18,
    val dailyGoalMinutes: Int = 30,
    val readBooksCount: Int = 12
)

data class DailyReadingPoint(
    val dayLabel: String,
    val minutes: Int
)

data class ReadingStats(
    val totalReadingTimeMinutes: Int = 1580,
    val currentStreakDays: Int = 7,
    val bestStreakDays: Int = 14,
    val totalPagesRead: Int = 1890,
    val totalWordsRead: Int = 510300,
    val pagesPerMinute: Float = 1.19f,
    val wordsPerMinute: Int = 322,
    val booksCompleted: Int = 12,
    val yearlyGoalBooks: Int = 18,
    val weeklyMinutes: List<DailyReadingPoint> = listOf(
        DailyReadingPoint("Lun", 35),
        DailyReadingPoint("Mar", 42),
        DailyReadingPoint("Mer", 25),
        DailyReadingPoint("Jeu", 45),
        DailyReadingPoint("Ven", 30),
        DailyReadingPoint("Sam", 60),
        DailyReadingPoint("Dim", 50)
    )
)

enum class ReaderTheme(val displayName: String, val bgHex: Long, val textHex: Long) {
    LIGHT("Clair", 0xFFFFFFFFL, 0xFF1E2024L),
    SEPIA("Sépia", 0xFFFBF0D9L, 0xFF4A3728L),
    DARK("Sombre", 0xFF18191CL, 0xFFE0E0E6L)
}
