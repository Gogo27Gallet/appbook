package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BookItem
import com.example.model.ReaderTheme
import com.example.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    book: BookItem,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    var readerTheme by remember { mutableStateOf(ReaderTheme.SEPIA) }
    var fontSizeSp by remember { mutableFloatStateOf(17f) }
    var currentChapterIndex by remember { mutableIntStateOf(0) }
    var showTocSheet by remember { mutableStateOf(false) }
    var showAppearanceBar by remember { mutableStateOf(false) }

    val chapters = if (book.chapters.isNotEmpty()) book.chapters else listOf(
        com.example.model.BookChapter(
            number = 1,
            title = "Introduction",
            paragraphs = listOf(
                book.description.ifEmpty { "Commencez votre lecture de ce chef-d'œuvre." },
                "Chaque page tournée est une opportunité d'élargir votre conscience et d'approfondir votre compréhension du monde.",
                book.quote.ifEmpty { "La lecture transforme l'esprit et apaise le cœur." }
            )
        )
    )

    val currentChapter = chapters.getOrElse(currentChapterIndex) { chapters.first() }
    val scrollState = rememberScrollState()

    val bgColor = Color(readerTheme.bgHex)
    val textColor = Color(readerTheme.textHex)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Reader Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("reader_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Quitter la lecture",
                        tint = textColor
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = book.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor.copy(alpha = 0.9f),
                        maxLines = 1
                    )
                    Text(
                        text = "Chapitre ${currentChapter.number} : ${currentChapter.title}",
                        fontSize = 11.sp,
                        color = textColor.copy(alpha = 0.65f),
                        maxLines = 1
                    )
                }

                Row {
                    IconButton(
                        onClick = { showAppearanceBar = !showAppearanceBar },
                        modifier = Modifier.testTag("reader_appearance_toggle")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatSize,
                            contentDescription = "Taille du texte",
                            tint = textColor
                        )
                    }

                    IconButton(
                        onClick = { showTocSheet = true },
                        modifier = Modifier.testTag("reader_toc_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Sommaire",
                            tint = textColor
                        )
                    }
                }
            }

            // Quick Appearance Bar (Toggled)
            AnimatedVisibility(visible = showAppearanceBar) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(textColor.copy(alpha = 0.08f))
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Theme buttons
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ReaderTheme.entries.forEach { theme ->
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(theme.bgHex))
                                    .clickable {
                                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                                        readerTheme = theme
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = theme.displayName.take(1),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(theme.textHex)
                                )
                            }
                        }
                    }

                    // Font size buttons
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextButton(
                            onClick = { if (fontSizeSp > 13f) fontSizeSp -= 2f }
                        ) {
                            Text("A-", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textColor)
                        }
                        Text(
                            text = "${fontSizeSp.toInt()}sp",
                            fontSize = 12.sp,
                            color = textColor.copy(alpha = 0.7f),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        TextButton(
                            onClick = { if (fontSizeSp < 26f) fontSizeSp += 2f }
                        ) {
                            Text("A+", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = textColor)
                        }
                    }
                }
            }

            // Main Reader Scrollable Book Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // Chapter Header
                Text(
                    text = "CHAPITRE ${currentChapter.number}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    color = textColor.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentChapter.title,
                    fontSize = (fontSizeSp + 6f).sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Paragraphs with elegant typography
                currentChapter.paragraphs.forEachIndexed { pIdx, paragraph ->
                    Text(
                        text = paragraph,
                        fontSize = fontSizeSp.sp,
                        fontFamily = FontFamily.Serif,
                        color = textColor,
                        lineHeight = (fontSizeSp * 1.6f).sp,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Next Chapter Button
                if (currentChapterIndex < chapters.size - 1) {
                    Button(
                        onClick = {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            currentChapterIndex++
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = textColor.copy(alpha = 0.15f),
                            contentColor = textColor
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .testTag("next_chapter_button")
                    ) {
                        Text(
                            text = "Chapitre suivant : ${chapters[currentChapterIndex + 1].title}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                } else {
                    Text(
                        text = "Fin du livre • Marquer comme lu",
                        fontSize = 13.sp,
                        color = textColor.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(80.dp))
            }

            // Bottom Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bgColor)
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Chapitre ${currentChapterIndex + 1}/${chapters.size}",
                        fontSize = 11.sp,
                        color = textColor.copy(alpha = 0.6f)
                    )

                    val scrollFraction = if (scrollState.maxValue > 0) {
                        scrollState.value.toFloat() / scrollState.maxValue.toFloat()
                    } else 0f

                    LinearProgressIndicator(
                        progress = { scrollFraction },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = textColor.copy(alpha = 0.6f),
                        trackColor = textColor.copy(alpha = 0.15f),
                    )

                    Text(
                        text = "${(scrollFraction * 100).toInt()}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // Table of Contents Bottom Sheet
        if (showTocSheet) {
            val sheetState = rememberModalBottomSheetState()
            ModalBottomSheet(
                onDismissRequest = { showTocSheet = false },
                sheetState = sheetState,
                containerColor = bgColor,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Sommaire des chapitres",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    chapters.forEachIndexed { idx, ch ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (idx == currentChapterIndex) textColor.copy(alpha = 0.1f) else Color.Transparent)
                                .clickable {
                                    currentChapterIndex = idx
                                    showTocSheet = false
                                }
                                .padding(vertical = 12.dp, horizontal = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Chapitre ${ch.number}",
                                    fontSize = 11.sp,
                                    color = textColor.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = ch.title,
                                    fontSize = 14.sp,
                                    fontWeight = if (idx == currentChapterIndex) FontWeight.Bold else FontWeight.Medium,
                                    color = textColor
                                )
                            }
                            if (idx == currentChapterIndex) {
                                Text("En cours", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
