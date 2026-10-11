package com.example.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

data class OnboardingBookCandidate(
    val id: String,
    val title: String,
    val author: String,
    val coverUrl: String,
    val category: String,
    val pageCount: Int,
    val primaryColorHex: Long
)

object BookCoverService {

    // Free Open Library covers
    const val COVER_ATOMIC_HABITS = "https://covers.openlibrary.org/b/isbn/9780735211292-L.jpg"
    const val COVER_POWER_OF_DISCIPLINE = "https://covers.openlibrary.org/b/isbn/9781647480066-L.jpg"
    const val COVER_FIVE_RINGS = "https://covers.openlibrary.org/b/isbn/9781590302484-L.jpg"
    const val COVER_RICHEST_MAN = "https://covers.openlibrary.org/b/isbn/9780451205360-L.jpg"
    const val COVER_TOO_BIG_TO_FAIL = "https://covers.openlibrary.org/b/isbn/9780143118244-L.jpg"
    const val COVER_POWER_OF_NOW = "https://covers.openlibrary.org/b/isbn/9781577314806-L.jpg"
    const val COVER_MANS_SEARCH = "https://covers.openlibrary.org/b/isbn/9780807014295-L.jpg"
    const val COVER_TRUST = "https://covers.openlibrary.org/b/isbn/9780593420317-L.jpg"
    const val COVER_SAPIENS = "https://covers.openlibrary.org/b/isbn/9780062316097-L.jpg"
    const val COVER_1984 = "https://covers.openlibrary.org/b/isbn/9780451524935-L.jpg"
    const val COVER_ALCHEMIST = "https://covers.openlibrary.org/b/isbn/9780062315007-L.jpg"
    const val COVER_PETIT_PRINCE = "https://covers.openlibrary.org/b/isbn/9780156012195-L.jpg"
    const val COVER_DUNE = "https://covers.openlibrary.org/b/isbn/9780441172719-L.jpg"
    const val COVER_PSYCHOLOGY_MONEY = "https://covers.openlibrary.org/b/isbn/9780857197689-L.jpg"
    const val COVER_DEEP_WORK = "https://covers.openlibrary.org/b/isbn/9781455586691-L.jpg"
    const val COVER_STEVE_JOBS = "https://covers.openlibrary.org/b/isbn/9781451648539-L.jpg"
    const val COVER_MEDITATIONS = "https://covers.openlibrary.org/b/isbn/9780140449334-L.jpg"

    val popularOnboardingBooks = listOf(
        OnboardingBookCandidate(
            id = "ob_atomic",
            title = "Atomic Habits",
            author = "James Clear",
            coverUrl = COVER_ATOMIC_HABITS,
            category = "Développement personnel",
            pageCount = 320,
            primaryColorHex = 0xFFF5A623L
        ),
        OnboardingBookCandidate(
            id = "ob_sapiens",
            title = "Sapiens: Une brève histoire de l'humanité",
            author = "Yuval Noah Harari",
            coverUrl = COVER_SAPIENS,
            category = "Histoire & Essais",
            pageCount = 512,
            primaryColorHex = 0xFFB37D4E
        ),
        OnboardingBookCandidate(
            id = "ob_1984",
            title = "1984",
            author = "George Orwell",
            coverUrl = COVER_1984,
            category = "Classiques",
            pageCount = 328,
            primaryColorHex = 0xFF3E4E50
        ),
        OnboardingBookCandidate(
            id = "ob_alchemist",
            title = "L'Alchimiste",
            author = "Paulo Coelho",
            coverUrl = COVER_ALCHEMIST,
            category = "Philosophie",
            pageCount = 208,
            primaryColorHex = 0xFF2B5876
        ),
        OnboardingBookCandidate(
            id = "ob_psychology",
            title = "The Psychology of Money",
            author = "Morgan Housel",
            coverUrl = COVER_PSYCHOLOGY_MONEY,
            category = "Finances",
            pageCount = 252,
            primaryColorHex = 0xFF1B4965
        ),
        OnboardingBookCandidate(
            id = "ob_dune",
            title = "Dune",
            author = "Frank Herbert",
            coverUrl = COVER_DUNE,
            category = "Science-Fiction",
            pageCount = 688,
            primaryColorHex = 0xFFC75D2C
        ),
        OnboardingBookCandidate(
            id = "ob_power_now",
            title = "Le Pouvoir du moment présent",
            author = "Eckhart Tolle",
            coverUrl = COVER_POWER_OF_NOW,
            category = "Spiritualité",
            pageCount = 256,
            primaryColorHex = 0xFF355070
        ),
        OnboardingBookCandidate(
            id = "ob_petit_prince",
            title = "Le Petit Prince",
            author = "Antoine de Saint-Exupéry",
            coverUrl = COVER_PETIT_PRINCE,
            category = "Classiques",
            pageCount = 96,
            primaryColorHex = 0xFF4A90E2
        ),
        OnboardingBookCandidate(
            id = "ob_richest_man",
            title = "The Richest Man in Babylon",
            author = "George S. Clason",
            coverUrl = COVER_RICHEST_MAN,
            category = "Finances",
            pageCount = 194,
            primaryColorHex = 0xFF3F51B5
        ),
        OnboardingBookCandidate(
            id = "ob_deep_work",
            title = "Deep Work",
            author = "Cal Newport",
            coverUrl = COVER_DEEP_WORK,
            category = "Productivité",
            pageCount = 304,
            primaryColorHex = 0xFFBF360C
        ),
        OnboardingBookCandidate(
            id = "ob_steve_jobs",
            title = "Steve Jobs",
            author = "Walter Isaacson",
            coverUrl = COVER_STEVE_JOBS,
            category = "Biographies",
            pageCount = 656,
            primaryColorHex = 0xFF263238
        ),
        OnboardingBookCandidate(
            id = "ob_meditations",
            title = "Pensées pour moi-même",
            author = "Marc Aurèle",
            coverUrl = COVER_MEDITATIONS,
            category = "Philosophie",
            pageCount = 224,
            primaryColorHex = 0xFF5D4037
        )
    )

    suspend fun searchCoverUrl(query: String): String? = withContext(Dispatchers.IO) {
        try {
            val encoded = URLEncoder.encode(query.trim(), "UTF-8")
            val url = URL("https://openlibrary.org/search.json?q=$encoded&limit=1")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 4000
                readTimeout = 4000
                requestMethod = "GET"
                setRequestProperty("User-Agent", "CollectionsApp/1.0")
            }

            if (conn.responseCode == 200) {
                val json = conn.inputStream.bufferedReader().use { it.readText() }
                val root = JSONObject(json)
                val docs = root.optJSONArray("docs")
                if (docs != null && docs.length() > 0) {
                    val firstDoc = docs.getJSONObject(0)
                    val coverI = firstDoc.optInt("cover_i", -1)
                    if (coverI > 0) {
                        return@withContext "https://covers.openlibrary.org/b/id/$coverI-L.jpg"
                    }
                    val isbns = firstDoc.optJSONArray("isbn")
                    if (isbns != null && isbns.length() > 0) {
                        val isbn = isbns.getString(0)
                        return@withContext "https://covers.openlibrary.org/b/isbn/$isbn-L.jpg"
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        null
    }
}
