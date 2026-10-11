package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BookItem
import com.example.model.DailyReadingPoint
import com.example.model.ReadingStats
import com.example.ui.components.BookCoverView
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.DockBackground
import com.example.ui.theme.SurfacePureWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.HapticManager

@Composable
fun StatsScreen(
    activeBook: BookItem,
    allBooks: List<BookItem>,
    stats: ReadingStats,
    isSessionRunning: Boolean,
    sessionSeconds: Int,
    sessionPagesRead: Int,
    onToggleSession: () -> Unit,
    onResetSession: () -> Unit,
    onIncrementPages: (Int) -> Unit,
    onSelectActiveBook: (BookItem) -> Unit,
    onOpenReader: (BookItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Session calculations
    val sessionMinutes = (sessionSeconds / 60f).coerceAtLeast(0.01f)
    val liveSessionPpm = if (sessionSeconds > 5 && sessionPagesRead > 0) {
        String.format(java.util.Locale.US, "%.2f", sessionPagesRead / sessionMinutes)
    } else {
        String.format(java.util.Locale.US, "%.2f", stats.pagesPerMinute)
    }
    val liveSessionWpm = if (sessionSeconds > 5 && sessionPagesRead > 0) {
        ((sessionPagesRead * 270) / sessionMinutes).toInt()
    } else {
        stats.wordsPerMinute
    }

    val remainingPages = (activeBook.pageCount - activeBook.currentPage).coerceAtLeast(0)
    val estimatedMinutesLeft = if (stats.pagesPerMinute > 0f) {
        (remainingPages / stats.pagesPerMinute).toInt()
    } else {
        remainingPages * 2
    }
    val hoursLeft = estimatedMinutesLeft / 60
    val minsLeft = estimatedMinutesLeft % 60

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 120.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Statistiques de Lecture",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Rythme, temps et métriques en temps réel",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }

            // Streak Badge
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFFFECE5))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = "Streak",
                    tint = Color(0xFFF05A28),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${stats.currentStreakDays} jours",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFFD34516)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Hero Card: Book Currently Being Read / Tracked
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfacePureWhite),
            shape = RoundedCornerShape(22.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BookCoverView(
                        book = activeBook,
                        modifier = Modifier
                            .width(82.dp)
                            .aspectRatio(0.68f),
                        cornerRadius = 8.dp,
                        elevation = 6.dp
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Livre Actuel",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF757B88),
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = activeBook.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            maxLines = 2
                        )
                        Text(
                            text = activeBook.author,
                            fontSize = 13.sp,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        val progressRatio = (activeBook.currentPage.toFloat() / activeBook.pageCount.toFloat()).coerceIn(0f, 1f)
                        LinearProgressIndicator(
                            progress = { progressRatio },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = DockBackground,
                            trackColor = Color(0xFFECEEF2)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${activeBook.currentPage} / ${activeBook.pageCount} p.",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Text(
                                text = "${(progressRatio * 100).toInt()}% terminé",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Time remaining estimate banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF4F6F9))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Timer,
                                contentDescription = null,
                                tint = DockBackground,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Temps restant estimé",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }

                        Text(
                            text = if (hoursLeft > 0) "${hoursLeft}h ${minsLeft}min" else "${minsLeft} min",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = DockBackground
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Live Reading Session & Stopwatch Card ("Temps de lecture / Chronomètre")
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfacePureWhite),
            shape = RoundedCornerShape(22.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(if (isSessionRunning) Color(0xFF4CAF50) else Color(0xFFB0B4BC))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isSessionRunning) "Session en cours..." else "Chronomètre de Lecture",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextPrimary
                        )
                    }

                    if (sessionSeconds > 0) {
                        IconButton(
                            onClick = {
                                HapticManager.click(haptic, context)
                                onResetSession()
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Réinitialiser",
                                tint = TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Chronometer Display
                val formattedTime = remember(sessionSeconds) {
                    val hrs = sessionSeconds / 3600
                    val mns = (sessionSeconds % 3600) / 60
                    val scs = sessionSeconds % 60
                    if (hrs > 0) String.format(java.util.Locale.US, "%02d:%02d:%02d", hrs, mns, scs)
                    else String.format(java.util.Locale.US, "%02d:%02d", mns, scs)
                }

                Text(
                    text = formattedTime,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isSessionRunning) DockBackground else TextPrimary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Session Page Stepper
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFF7F8FA))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pages lues cette session :",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SessionPageChip(
                            text = "-1",
                            onClick = {
                                HapticManager.tick(haptic, context)
                                onIncrementPages(-1)
                            }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$sessionPagesRead",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = DockBackground,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        SessionPageChip(
                            text = "+1",
                            onClick = {
                                HapticManager.tick(haptic, context)
                                onIncrementPages(1)
                            }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        SessionPageChip(
                            text = "+5",
                            onClick = {
                                HapticManager.tick(haptic, context)
                                onIncrementPages(5)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Calculated Metrics during session
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SpeedMetricBox(
                        title = "Vitesse Session",
                        value = "$liveSessionPpm p/min",
                        sub = "calculée en direct",
                        icon = Icons.Outlined.Speed,
                        modifier = Modifier.weight(1f)
                    )
                    SpeedMetricBox(
                        title = "Mots par Minute",
                        value = "$liveSessionWpm WPM",
                        sub = "rythme de lecture",
                        icon = Icons.Outlined.Bolt,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Start/Pause Button & Reader Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            HapticManager.click(haptic, context)
                            onToggleSession()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSessionRunning) Color(0xFFC72828) else DockBackground
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1.2f)
                            .height(48.dp)
                            .testTag("toggle_reading_session_button")
                    ) {
                        Icon(
                            imageVector = if (isSessionRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isSessionRunning) "Mettre en Pause" else "Lancer le Chrono",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            HapticManager.click(haptic, context)
                            onOpenReader(activeBook)
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(0.9f)
                            .height(48.dp)
                            .testTag("open_reader_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AutoStories,
                            contentDescription = null,
                            tint = TextPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Lire le texte",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 4 KPI Cards Grid
        Text(
            text = "Performances Globales",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val totalHours = stats.totalReadingTimeMinutes / 60
            val totalMins = stats.totalReadingTimeMinutes % 60
            KpiStatCard(
                title = "Temps de lecture",
                value = "${totalHours}h ${totalMins}m",
                caption = "Total cumulé",
                icon = Icons.Outlined.Timer,
                iconTint = Color(0xFF3F51B5),
                modifier = Modifier.weight(1f)
            )
            KpiStatCard(
                title = "Pages lues",
                value = "${stats.totalPagesRead}",
                caption = "${stats.booksCompleted} livres finis",
                icon = Icons.Outlined.MenuBook,
                iconTint = Color(0xFF2E7D32),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            KpiStatCard(
                title = "Pages / minute",
                value = "${stats.pagesPerMinute}",
                caption = "~${(stats.pagesPerMinute * 60).toInt()} pages/h",
                icon = Icons.Outlined.Speed,
                iconTint = Color(0xFFE65100),
                modifier = Modifier.weight(1f)
            )
            KpiStatCard(
                title = "Mots / minute",
                value = "${stats.wordsPerMinute}",
                caption = "Cadence moyenne",
                icon = Icons.Outlined.Bolt,
                iconTint = Color(0xFF6A1B9A),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Streak Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfacePureWhite),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Régularité & Streak",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Record personnel : ${stats.bestStreakDays} jours consécutifs",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFFFFECE5))
                            .padding(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = Color(0xFFF05A28),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Days of week row
                val days = listOf("Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    days.forEachIndexed { idx, day ->
                        val isDone = idx < stats.currentStreakDays
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isDone) Color(0xFFFF5722) else Color(0xFFECEEF2)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isDone) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                } else {
                                    Text(
                                        text = "${idx + 1}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF9EA3AF)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = day,
                                fontSize = 11.sp,
                                color = if (isDone) TextPrimary else TextSecondary,
                                fontWeight = if (isDone) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Weekly Activity Bar Chart
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfacePureWhite),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Temps de lecture cette semaine",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Minutes lues par jour",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(18.dp))

                val maxMinutes = stats.weeklyMinutes.maxOfOrNull { it.minutes }?.coerceAtLeast(60) ?: 60

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    stats.weeklyMinutes.forEach { point ->
                        val barHeightRatio = (point.minutes.toFloat() / maxMinutes.toFloat()).coerceIn(0.12f, 1f)
                        val isHighest = point.minutes == maxMinutes

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier
                                .clickable {
                                    HapticManager.tick(haptic, context)
                                }
                        ) {
                            Text(
                                text = "${point.minutes}m",
                                fontSize = 10.sp,
                                fontWeight = if (isHighest) FontWeight.Bold else FontWeight.Normal,
                                color = if (isHighest) DockBackground else TextSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(26.dp)
                                    .height((100 * barHeightRatio).dp)
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                    .background(if (isHighest) DockBackground else Color(0xFFD3D8E2))
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = point.dayLabel,
                                fontSize = 12.sp,
                                fontWeight = if (isHighest) FontWeight.Bold else FontWeight.Normal,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Yearly Challenge & Goal Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfacePureWhite),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Défi Lecture 2026",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${stats.booksCompleted} sur ${stats.yearlyGoalBooks} livres lus",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }

                    val percent = ((stats.booksCompleted.toFloat() / stats.yearlyGoalBooks.toFloat()) * 100).toInt()
                    Text(
                        text = "$percent%",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = DockBackground
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { (stats.booksCompleted.toFloat() / stats.yearlyGoalBooks.toFloat()).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = DockBackground,
                    trackColor = Color(0xFFECEEF2)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Plus que ${stats.yearlyGoalBooks - stats.booksCompleted} livres pour réussir votre objectif annuel !",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Switch Tracked Book Row
        if (allBooks.isNotEmpty()) {
            Text(
                text = "Changer le livre à suivre",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(allBooks) { book ->
                    val isSelected = book.id == activeBook.id
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(84.dp)
                            .clickable {
                                HapticManager.click(haptic, context)
                                onSelectActiveBook(book)
                            }
                    ) {
                        BookCoverView(
                            book = book,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.68f),
                            cornerRadius = 8.dp,
                            elevation = if (isSelected) 8.dp else 2.dp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = book.title,
                            fontSize = 11.sp,
                            maxLines = 1,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) DockBackground else TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SessionPageChip(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFE5E7EB))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = TextPrimary
        )
    }
}

@Composable
private fun SpeedMetricBox(
    title: String,
    value: String,
    sub: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF7F8FA))
            .padding(12.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = DockBackground,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = title,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = sub,
                fontSize = 10.sp,
                color = Color(0xFF9AA0AC)
            )
        }
    }
}

@Composable
private fun KpiStatCard(
    title: String,
    value: String,
    caption: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = SurfacePureWhite),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(iconTint.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = caption,
                fontSize = 11.sp,
                color = Color(0xFF8F95A3)
            )
        }
    }
}
