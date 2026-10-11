package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.BookCoverService
import com.example.data.OnboardingBookCandidate
import com.example.model.UserPreferences
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.DockBackground
import com.example.ui.theme.SurfacePureWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.HapticManager
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun OnboardingScreen(
    onFinishOnboarding: (UserPreferences, List<OnboardingBookCandidate>) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    var currentStep by remember { mutableIntStateOf(1) }

    // Step 1: Archetype & Time of Day
    var selectedArchetype by remember { mutableStateOf("Le Dévoreur de Livres") }
    var selectedTimeOfDay by remember { mutableStateOf("Soirée au calme") }

    // Step 2: Books already read
    val candidateBooks = remember { mutableStateListOf<OnboardingBookCandidate>().apply { addAll(BookCoverService.popularOnboardingBooks) } }
    val selectedBooks = remember { mutableStateListOf<OnboardingBookCandidate>() }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchingCover by remember { mutableStateOf(false) }

    // Step 3: Genres
    val selectedGenres = remember {
        mutableStateListOf(
            "Développement personnel",
            "Philosophie & Sagesse",
            "Science-Fiction & Anticipation"
        )
    }

    // Step 4: History & Goals
    var booksReadLastYear by remember { mutableStateOf("6 à 12 livres") }
    var yearlyGoalCount by remember { mutableIntStateOf(18) }
    var dailyGoalMinutes by remember { mutableIntStateOf(30) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            // Header: Step indicators & Skip
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Step Dots
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    for (i in 1..5) {
                        val isActive = i <= currentStep
                        Box(
                            modifier = Modifier
                                .height(4.dp)
                                .width(if (i == currentStep) 24.dp else 12.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(if (isActive) DockBackground else Color(0xFFD8DCE4))
                        )
                    }
                }

                if (currentStep < 5) {
                    TextButton(
                        onClick = {
                            HapticManager.click(haptic, context)
                            currentStep = 5
                        }
                    ) {
                        Text(
                            text = "Passer",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step Content
            AnimatedContent(
                targetState = currentStep,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(180))
                },
                modifier = Modifier.weight(1f),
                label = "onboarding_steps"
            ) { step ->
                when (step) {
                    1 -> Step1Archetype(
                        selectedArchetype = selectedArchetype,
                        onArchetypeSelected = {
                            HapticManager.click(haptic, context)
                            selectedArchetype = it
                        },
                        selectedTimeOfDay = selectedTimeOfDay,
                        onTimeSelected = {
                            HapticManager.tick(haptic, context)
                            selectedTimeOfDay = it
                        }
                    )
                    2 -> Step2BooksAlreadyRead(
                        candidateBooks = candidateBooks,
                        selectedBooks = selectedBooks,
                        searchQuery = searchQuery,
                        onSearchQueryChange = { searchQuery = it },
                        isSearchingCover = isSearchingCover,
                        onSearchAndAddBook = { title ->
                            scope.launch {
                                isSearchingCover = true
                                val coverUrl = BookCoverService.searchCoverUrl(title)
                                val finalUrl = coverUrl ?: BookCoverService.COVER_ATOMIC_HABITS
                                val newBook = OnboardingBookCandidate(
                                    id = "custom_${UUID.randomUUID().toString().take(6)}",
                                    title = title.trim(),
                                    author = "Auteur personnalisé",
                                    coverUrl = finalUrl,
                                    category = "Mes lectures",
                                    pageCount = 300,
                                    primaryColorHex = 0xFF2A5298
                                )
                                candidateBooks.add(0, newBook)
                                selectedBooks.add(newBook)
                                searchQuery = ""
                                isSearchingCover = false
                                HapticManager.success(haptic, context)
                            }
                        },
                        onToggleBook = { book ->
                            HapticManager.click(haptic, context)
                            if (selectedBooks.contains(book)) {
                                selectedBooks.remove(book)
                            } else {
                                selectedBooks.add(book)
                            }
                        }
                    )
                    3 -> Step3PreferredGenres(
                        selectedGenres = selectedGenres,
                        onToggleGenre = { genre ->
                            HapticManager.tick(haptic, context)
                            if (selectedGenres.contains(genre)) {
                                if (selectedGenres.size > 1) selectedGenres.remove(genre)
                            } else {
                                selectedGenres.add(genre)
                            }
                        }
                    )
                    4 -> Step4HistoryAndGoals(
                        booksReadLastYear = booksReadLastYear,
                        onBooksReadChange = {
                            HapticManager.click(haptic, context)
                            booksReadLastYear = it
                        },
                        yearlyGoalCount = yearlyGoalCount,
                        onYearlyGoalChange = {
                            HapticManager.tick(haptic, context)
                            yearlyGoalCount = it
                        },
                        dailyGoalMinutes = dailyGoalMinutes,
                        onDailyGoalChange = {
                            HapticManager.tick(haptic, context)
                            dailyGoalMinutes = it
                        }
                    )
                    5 -> Step5CelebrationSummary(
                        selectedArchetype = selectedArchetype,
                        selectedBooks = selectedBooks,
                        selectedGenres = selectedGenres,
                        booksReadLastYear = booksReadLastYear,
                        yearlyGoalCount = yearlyGoalCount
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Navigation Bottom Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (currentStep > 1) {
                    OutlinedButton(
                        onClick = {
                            HapticManager.click(haptic, context)
                            currentStep--
                        },
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .weight(0.4f)
                            .height(52.dp)
                    ) {
                        Text(text = "Retour", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    }
                }

                Button(
                    onClick = {
                        if (currentStep < 5) {
                            HapticManager.click(haptic, context)
                            currentStep++
                        } else {
                            HapticManager.success(haptic, context)
                            val estimatedReadCount = when (booksReadLastYear) {
                                "0 à 2 livres" -> 2
                                "3 à 5 livres" -> 4
                                "6 à 12 livres" -> 8
                                "13 à 24 livres" -> 16
                                else -> 25
                            }
                            val prefs = UserPreferences(
                                archetypeTitle = selectedArchetype,
                                favoriteTimeOfDay = selectedTimeOfDay,
                                preferredGenres = selectedGenres.toList(),
                                booksReadLastYear = booksReadLastYear,
                                yearlyGoalCount = yearlyGoalCount,
                                dailyGoalMinutes = dailyGoalMinutes,
                                readBooksCount = maxOf(selectedBooks.size, estimatedReadCount)
                            )
                            onFinishOnboarding(prefs, selectedBooks.toList())
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DockBackground),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("onboarding_next_button")
                ) {
                    Text(
                        text = if (currentStep == 5) "Entrer dans ma Bibliothèque" else "Continuer",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// Step 1: Archetype & Timing
@Composable
private fun Step1Archetype(
    selectedArchetype: String,
    onArchetypeSelected: (String) -> Unit,
    selectedTimeOfDay: String,
    onTimeSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Bienvenue dans Collections",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = "Quel est votre archétype de lecteur ?",
            fontSize = 14.sp,
            color = TextSecondary,
            modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
        )

        val archetypes = listOf(
            Triple("🦉 Le Penseur Nocturne", "Lit dans le calme absolu avant de dormir pour se recentrer.", "Le Penseur Nocturne"),
            Triple("⚡ Le Dévoreur de Livres", "Plus de 20 livres par an, un livre toujours à portée de main.", "Le Dévoreur de Livres"),
            Triple("🧭 L'Explorateur Curieux", "Curieux insatiable, alternant essais, sciences et romans.", "L'Explorateur Curieux"),
            Triple("🧘 L'Esprit Méditatif", "Lecture lente, contemplative, axée sur le développement intérieur.", "L'Esprit Méditatif")
        )

        archetypes.forEach { (title, desc, key) ->
            val isSelected = selectedArchetype == key
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .clickable { onArchetypeSelected(key) }
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) DockBackground else Color(0xFFE2E4EA),
                        shape = RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(0xFFF4F6FB) else SurfacePureWhite
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = desc, fontSize = 12.sp, color = TextSecondary, lineHeight = 16.sp)
                    }
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = DockBackground,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Votre moment de lecture privilégié :",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        val times = listOf("Matin au réveil", "Dans les transports", "Soirée au calme", "Pendant les pauses")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            times.take(2).forEach { time ->
                FilterChip(
                    selected = selectedTimeOfDay == time,
                    onClick = { onTimeSelected(time) },
                    label = { Text(text = time, fontSize = 12.sp) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            times.takeLast(2).forEach { time ->
                FilterChip(
                    selected = selectedTimeOfDay == time,
                    onClick = { onTimeSelected(time) },
                    label = { Text(text = time, fontSize = 12.sp) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// Step 2: Books Already Read with Real Covers
@Composable
private fun Step2BooksAlreadyRead(
    candidateBooks: List<OnboardingBookCandidate>,
    selectedBooks: List<OnboardingBookCandidate>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    isSearchingCover: Boolean,
    onSearchAndAddBook: (String) -> Unit,
    onToggleBook: (OnboardingBookCandidate) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Quels livres avez-vous déjà lus ?",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = "Sélectionnez vos lectures passées pour récupérer leurs vraies couvertures.",
            fontSize = 13.sp,
            color = TextSecondary,
            modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
        )

        // Search or Add any book with live Open Library cover fetch
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Ajouter un livre (ex: 1984, Dune...)", fontSize = 13.sp) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DockBackground,
                    unfocusedBorderColor = Color(0xFFD6DAE2)
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { if (searchQuery.isNotBlank()) onSearchAndAddBook(searchQuery) },
                enabled = searchQuery.isNotBlank() && !isSearchingCover,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DockBackground),
                contentPadding = PaddingValues(horizontal = 14.dp)
            ) {
                if (isSearchingCover) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                } else {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Ajouter", tint = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Selected counter badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${selectedBooks.size} livre(s) déjà lu(s)",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = DockBackground
            )
            Text(
                text = "Touchez pour cocher",
                fontSize = 11.sp,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Grid of Books with REAL covers
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(candidateBooks, key = { it.id }) { book ->
                val isSelected = selectedBooks.any { it.id == book.id }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleBook(book) }
                        .border(
                            width = if (isSelected) 2.5.dp else 1.dp,
                            color = if (isSelected) DockBackground else Color(0xFFE2E4EA),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfacePureWhite)
                ) {
                    Column(modifier = Modifier.padding(6.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.68f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(book.primaryColorHex))
                        ) {
                            AsyncImage(
                                model = book.coverUrl,
                                contentDescription = book.title,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize()
                            )

                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.25f))
                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(DockBackground),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Sélectionné",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = book.title,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = book.author,
                            fontSize = 9.sp,
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

// Step 3: Preferred Genres
@Composable
private fun Step3PreferredGenres(
    selectedGenres: List<String>,
    onToggleGenre: (String) -> Unit
) {
    val allGenres = listOf(
        "🧠 Développement personnel",
        "🏛️ Philosophie & Sagesse",
        "🪐 Science-Fiction & Anticipation",
        "📈 Finance, Économie & Business",
        "🕵️ Thrillers & Enquêtes",
        "📜 Histoire & Biographies",
        "🧬 Sciences & Essais",
        "🎭 Littérature classique",
        "🧘 Psychologie & Bien-être",
        "🌿 Spiritualité & Méditation",
        "💡 Productivité & Stratégie",
        "❤️ Romance & Drame"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Vos univers & styles préférés",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = "Sélectionnez les genres qui vous passionnent le plus.",
            fontSize = 13.sp,
            color = TextSecondary,
            modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
        )

        allGenres.forEach { genreLabel ->
            val cleanKey = genreLabel.substringAfter(" ")
            val isSelected = selectedGenres.any { it.contains(cleanKey) }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .clickable { onToggleGenre(cleanKey) }
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) DockBackground else Color(0xFFE2E4EA),
                        shape = RoundedCornerShape(14.dp)
                    ),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(0xFFF3F5FA) else SurfacePureWhite
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = genreLabel,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 14.sp,
                        color = TextPrimary
                    )

                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = DockBackground,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

// Step 4: History & Yearly Goals
@Composable
private fun Step4HistoryAndGoals(
    booksReadLastYear: String,
    onBooksReadChange: (String) -> Unit,
    yearlyGoalCount: Int,
    onYearlyGoalChange: (Int) -> Unit,
    dailyGoalMinutes: Int,
    onDailyGoalChange: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Vos habitudes & objectifs",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = "Calibrons vos métriques et votre rythme de lecture.",
            fontSize = 13.sp,
            color = TextSecondary,
            modifier = Modifier.padding(top = 2.dp, bottom = 18.dp)
        )

        Text(
            text = "Nombre de livres lus l'an dernier :",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        val options = listOf(
            "0 à 2 livres" to "Nouveau départ 🚀",
            "3 à 5 livres" to "Lecteur occasionnel 📖",
            "6 à 12 livres" to "1 livre par mois 🎯",
            "13 à 24 livres" to "Lecteur assidu ⚡",
            "25+ livres" to "Grand dévoreur 🏆"
        )

        options.forEach { (range, desc) ->
            val isSelected = booksReadLastYear == range
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .clickable { onBooksReadChange(range) }
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) DockBackground else Color(0xFFE2E4EA),
                        shape = RoundedCornerShape(14.dp)
                    ),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(0xFFF3F5FA) else SurfacePureWhite
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = range, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                        Text(text = desc, fontSize = 11.sp, color = TextSecondary)
                    }

                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = DockBackground,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Yearly Goal
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfacePureWhite),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Objectif de lecture pour 2026 :",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onYearlyGoalChange((yearlyGoalCount - 1).coerceAtLeast(3)) },
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFECEEF2), CircleShape)
                    ) {
                        Text("-", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    Text(
                        text = "$yearlyGoalCount livres",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = DockBackground
                    )

                    IconButton(
                        onClick = { onYearlyGoalChange((yearlyGoalCount + 1).coerceAtMost(100)) },
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFECEEF2), CircleShape)
                    ) {
                        Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Daily Time
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfacePureWhite),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Temps de lecture quotidien visé :",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(15, 30, 45, 60).forEach { mins ->
                        FilterChip(
                            selected = dailyGoalMinutes == mins,
                            onClick = { onDailyGoalChange(mins) },
                            label = { Text("${mins}m/j", fontSize = 12.sp) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

// Step 5: Celebration & Calculated Metrics Summary
@Composable
private fun Step5CelebrationSummary(
    selectedArchetype: String,
    selectedBooks: List<OnboardingBookCandidate>,
    selectedGenres: List<String>,
    booksReadLastYear: String,
    yearlyGoalCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFECE5)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = null,
                tint = Color(0xFFF05A28),
                modifier = Modifier.size(42.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Votre Sanctuaire Littéraire",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Profil calculé avec succès pour $selectedArchetype",
            fontSize = 13.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 2.dp, bottom = 18.dp)
        )

        // Summary KPI Cards
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfacePureWhite),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Vos Métriques Personnalisées",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CalculatedMetricBox(
                        title = "Vitesse estimée",
                        value = "280 WPM",
                        sub = "~1.05 p/min",
                        icon = Icons.Outlined.Speed,
                        modifier = Modifier.weight(1f)
                    )
                    CalculatedMetricBox(
                        title = "Streak initial",
                        value = "7 Jours 🔥",
                        sub = "Prêt à enchaîner",
                        icon = Icons.Default.LocalFireDepartment,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CalculatedMetricBox(
                        title = "Objectif 2026",
                        value = "$yearlyGoalCount livres",
                        sub = "Défi annuel",
                        icon = Icons.Outlined.AutoStories,
                        modifier = Modifier.weight(1f)
                    )
                    CalculatedMetricBox(
                        title = "Livres intégrés",
                        value = "${selectedBooks.size} lus",
                        sub = "avec vraies couvertures",
                        icon = Icons.Outlined.MenuBook,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Selected books preview covers
        if (selectedBooks.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfacePureWhite),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Couvertures récupérées pour votre étagère :",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        selectedBooks.take(4).forEach { book ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(0.68f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(book.primaryColorHex))
                            ) {
                                AsyncImage(
                                    model = book.coverUrl,
                                    contentDescription = book.title,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalculatedMetricBox(
    title: String,
    value: String,
    sub: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF7F8FA))
            .padding(12.dp)
    ) {
        Column {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = DockBackground,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
            Text(text = title, fontSize = 11.sp, color = TextSecondary)
            Text(text = sub, fontSize = 10.sp, color = Color(0xFF9AA0AC))
        }
    }
}
