package com.example.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.model.BookCoverStyle
import com.example.model.BookItem

@Composable
fun BookCoverView(
    book: BookItem,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 6.dp,
    elevation: Dp = 6.dp
) {
    Box(
        modifier = modifier
            .shadow(elevation = elevation, shape = RoundedCornerShape(cornerRadius), clip = false)
            .clip(RoundedCornerShape(cornerRadius))
            .background(Color(book.primaryColorHex))
    ) {
        val bitmap = remember(book.coverImageBytes) {
            book.coverImageBytes?.let { bytes ->
                try {
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
                } catch (e: Exception) {
                    null
                }
            }
        }

        if (bitmap != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(book.primaryColorHex)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = bitmap,
                    contentDescription = book.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        } else if (!book.coverImageUrl.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(book.primaryColorHex)),
                contentAlignment = Alignment.Center
            ) {
                // Vector cover underneath as immediate fallback so it is never blank while loading
                VectorCover(book)

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(book.coverImageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = book.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        } else {
            VectorCover(book)
        }

        // Realistic book lighting overlay with subtle spine gradient
        BookLightingOverlay()
    }
}

@Composable
private fun VectorCover(book: BookItem) {
    when (book.coverStyle) {
        BookCoverStyle.POWER_OF_DISCIPLINE -> PowerOfDisciplineCover()
        BookCoverStyle.ATOMIC_HABITS -> AtomicHabitsCover()
        BookCoverStyle.BOOK_OF_FIVE_RINGS -> BookOfFiveRingsCover()
        BookCoverStyle.ONE_CHARBONNEAU -> OneCharbonneauCover()
        BookCoverStyle.ORPHANED_BELIEVERS -> OrphanedBelieversCover()
        BookCoverStyle.NEXT_OF_KIN -> NextOfKinCover()
        BookCoverStyle.CONFORM -> ConformCover()
        BookCoverStyle.TRUST -> TrustCover()
        BookCoverStyle.ELECTRIC_CITY -> ElectricCityCover()
        BookCoverStyle.PRACTICAL_UNCERTAINTY -> PracticalUncertaintyCover()
        BookCoverStyle.NERVOUS_SYSTEM -> NervousSystemCover()
        BookCoverStyle.THE_MAGIC -> TheMagicCover()
        BookCoverStyle.RICHEST_MAN_BABYLON -> RichestManCover()
        BookCoverStyle.TOO_BIG_TO_FAIL -> TooBigToFailCover()
        BookCoverStyle.POWER_OF_NOW -> PowerOfNowCover()
        BookCoverStyle.MANS_SEARCH -> MansSearchCover()
        BookCoverStyle.CUSTOM -> GenericCover(book)
    }
}

@Composable
private fun BookLightingOverlay() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val spineWidth = size.width * 0.08f
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Black.copy(alpha = 0.28f),
                    Color.Black.copy(alpha = 0.10f),
                    Color.Transparent
                ),
                startX = 0f,
                endX = spineWidth
            ),
            topLeft = Offset.Zero,
            size = androidx.compose.ui.geometry.Size(spineWidth, size.height)
        )
        drawLine(
            color = Color.White.copy(alpha = 0.18f),
            start = Offset(spineWidth * 0.9f, 0f),
            end = Offset(spineWidth * 0.9f, size.height),
            strokeWidth = 1f
        )
        val rightEdge = size.width * 0.025f
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.12f)),
                startX = size.width - rightEdge,
                endX = size.width
            ),
            topLeft = Offset(size.width - rightEdge, 0f),
            size = androidx.compose.ui.geometry.Size(rightEdge, size.height)
        )
    }
}

// Power of Discipline Cover (From screenshot: bright yellow, bold black title, Daniel Walter box)
@Composable
private fun PowerOfDisciplineCover() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFDD00))
            .padding(horizontal = 7.dp, vertical = 7.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "THE",
                    color = Color.Black,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "POWER",
                    color = Color.Black,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 15.sp,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "OF  DISCIPLINE",
                    color = Color.Black,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "HOW TO USE SELF CONTROL AND MENTAL TOUGHNESS TO ACHIEVE YOUR GOALS",
                    color = Color.Black.copy(alpha = 0.8f),
                    fontSize = 5.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 6.sp
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black, RoundedCornerShape(2.dp))
                    .padding(vertical = 3.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "DANIEL WALTER",
                    color = Color.White,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

// Atomic Habits Cover (From screenshot: beige, speckled title, gold bestseller badge, James Clear)
@Composable
private fun AtomicHabitsCover() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F3EB))
            .padding(horizontal = 6.dp, vertical = 6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "NEW YORK TIMES BESTSELLER",
                    color = Color(0xFF6B685F),
                    fontSize = 4.5.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.4.sp
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = "Tiny Changes, Remarkable Results",
                    color = Color(0xFF8B877C),
                    fontSize = 5.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Atomic",
                    color = Color(0xFFB57C48),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    lineHeight = 14.sp
                )
                Text(
                    text = "Habits",
                    color = Color(0xFFB57C48),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    lineHeight = 14.sp
                )
            }

            // Subtitle and Author
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "An Easy & Proven Way to Build Good Habits & Break Bad Ones",
                    color = Color(0xFF666359),
                    fontSize = 4.5.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 6.sp
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "James Clear",
                    color = Color.Black,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Book of Five Rings Cover (From screenshot: white top, dark red mandala/rings, Miyamoto Musashi)
@Composable
private fun BookOfFiveRingsCover() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6E1824))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // White Top
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF9F7F3))
                    .padding(horizontal = 6.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "The Book of\nFive Rings",
                    color = Color(0xFF90232E),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Center,
                    lineHeight = 9.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Miyamoto Musashi",
                    color = Color(0xFF333333),
                    fontSize = 6.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Concentric Glowing Rings Portal Illustration
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val cx = size.width / 2f
                    val cy = size.height * 0.6f
                    val radii = listOf(36f, 28f, 20f, 13f, 7f)
                    val colors = listOf(
                        Color(0xFF8B212E),
                        Color(0xFFB83B38),
                        Color(0xFFE26B38),
                        Color(0xFFF79C43),
                        Color(0xFFFFDF7D)
                    )
                    radii.forEachIndexed { idx, r ->
                        drawCircle(
                            color = colors[idx],
                            radius = r,
                            center = Offset(cx, cy),
                            style = Stroke(width = 3.5f)
                        )
                    }
                    // Silhouette figure standing inside portal
                    drawCircle(color = Color.Black, radius = 2.5f, center = Offset(cx, cy - 2f))
                    drawLine(
                        color = Color.Black,
                        start = Offset(cx, cy),
                        end = Offset(cx, cy + 8f),
                        strokeWidth = 2f
                    )
                }
            }
        }
    }
}

// ONE Cover by Bradley Charbonneau (From screenshot: white, green seedling sprouting, "ONE")
@Composable
private fun OneCharbonneauCover() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F4F6))
            .padding(horizontal = 6.dp, vertical = 6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Distill Your Vision Into a Single Word and Create a Roadmap to Your Future Self",
                    color = Color(0xFF555555),
                    fontSize = 4.5.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 6.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "ONE",
                    color = Color(0xFF1B1B1D),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 18.sp
                )
                Text(
                    text = "BECOME TOMORROW'S YOU TODAY",
                    color = Color(0xFF444444),
                    fontSize = 4.5.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.4.sp
                )
            }

            // Sprouting seedling graphic in center
            Canvas(modifier = Modifier.size(36.dp)) {
                val cx = size.width / 2f
                val cy = size.height * 0.8f
                // Soil mound
                drawOval(
                    color = Color(0xFF382A1D),
                    topLeft = Offset(cx - 14f, cy - 4f),
                    size = androidx.compose.ui.geometry.Size(28f, 10f)
                )
                // Stem
                drawLine(
                    color = Color(0xFF5CA336),
                    start = Offset(cx, cy),
                    end = Offset(cx, cy - 16f),
                    strokeWidth = 2.5f
                )
                // Left leaf
                drawOval(
                    color = Color(0xFF67B83A),
                    topLeft = Offset(cx - 10f, cy - 22f),
                    size = androidx.compose.ui.geometry.Size(10f, 6f)
                )
                // Right leaf
                drawOval(
                    color = Color(0xFF5CA336),
                    topLeft = Offset(cx, cy - 22f),
                    size = androidx.compose.ui.geometry.Size(10f, 6f)
                )
            }

            Text(
                text = "BRADLEY CHARBONNEAU",
                color = Color(0xFF555555),
                fontSize = 5.5.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.3.sp
            )
        }
    }
}

// 1. Orphaned Believers Cover
@Composable
private fun OrphanedBelieversCover() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC72828))
            .padding(horizontal = 6.dp, vertical = 7.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "ORPHANED", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black, lineHeight = 11.sp)
                Text(text = "BELIEVERS", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 10.sp)
            }
            Box(
                modifier = Modifier
                    .width(42.dp)
                    .height(44.dp)
                    .background(Color(0xFFFBFBFB), RoundedCornerShape(2.dp))
                    .padding(3.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp)
                        .background(Color(0xFF333333))
                )
            }
            Text(text = "SARA BILLUPS", color = Color.White, fontSize = 7.5.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// 2. Next of Kin Cover
@Composable
private fun NextOfKinCover() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD65223))
            .padding(horizontal = 6.dp, vertical = 7.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "NEXT", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Black, lineHeight = 11.sp)
                Text(text = "OF  KIN", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, lineHeight = 10.sp)
            }
            Canvas(modifier = Modifier.size(44.dp)) {
                val cx = size.width / 2f
                val cy = size.height * 0.85f
                drawLine(color = Color(0xFF2B170E), start = Offset(cx, cy), end = Offset(cx, cy - 22f), strokeWidth = 5f)
                drawCircle(color = Color(0xFF2B170E).copy(alpha = 0.85f), radius = 16f, center = Offset(cx, cy - 26f))
            }
            Text(text = "GABRIELLE HAMILTON", color = Color.White, fontSize = 7.5.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// 3. Conform Cover
@Composable
private fun ConformCover() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3ECE0))
            .padding(horizontal = 6.dp, vertical = 7.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Canvas(modifier = Modifier.size(40.dp)) {
                    val cx = size.width / 2f
                    val gold = Color(0xFFC39A52)
                    drawLine(color = gold, start = Offset(cx, 4f), end = Offset(cx, size.height - 4f), strokeWidth = 3f)
                    drawCircle(color = gold, radius = 12f, center = Offset(cx, size.height * 0.45f), style = Stroke(width = 2.5f))
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "CONFORM", color = Color(0xFFA67C34), fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
            }
            Text(text = "ARIEL SULLIVAN", color = Color(0xFF554D40), fontSize = 7.sp, fontWeight = FontWeight.Medium)
        }
    }
}

// 4. Trust Cover
@Composable
private fun TrustCover() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E6548))
            .padding(horizontal = 6.dp, vertical = 7.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .background(Color(0xFFE5B520), CircleShape)
            )
            Text(text = "T R U S T", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
            Text(text = "HERNAN DIAZ", color = Color.White.copy(alpha = 0.8f), fontSize = 6.sp)
        }
    }
}

// 5. Electric City Cover
@Composable
private fun ElectricCityCover() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF21252C))
            .padding(horizontal = 6.dp, vertical = 7.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "ELECTRIC\nCITY", color = Color(0xFFEBB138), fontSize = 9.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
            Text(text = "THOMAS HAGER", color = Color.White.copy(alpha = 0.9f), fontSize = 7.sp)
        }
    }
}

// 6. Practical Uncertainty Cover
@Composable
private fun PracticalUncertaintyCover() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .padding(horizontal = 6.dp, vertical = 7.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "PRACTICAL", color = Color(0xFF1E2024), fontSize = 8.5.sp, fontWeight = FontWeight.Black)
            Text(text = "UNCERTAINTY", color = Color(0xFF333333), fontSize = 7.sp, fontWeight = FontWeight.Bold)
            Text(text = "E. D. MILLER", color = Color(0xFF666666), fontSize = 7.sp)
        }
    }
}

// 7. Richest Man Cover
@Composable
private fun RichestManCover() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF192A44))
            .padding(horizontal = 7.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "THE RICHEST\nMAN IN\nBABYLON", color = Color(0xFFE2C485), fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif, textAlign = TextAlign.Center)
            }
            Text(text = "GEORGE S. CLASON", color = Color(0xFFE2C485), fontSize = 7.sp, fontFamily = FontFamily.Serif)
        }
    }
}

// 8. Too Big to Fail Cover
@Composable
private fun TooBigToFailCover() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD92828))
            .padding(horizontal = 6.dp, vertical = 7.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "ANDREW ROSS SORKIN", color = Color.White, fontSize = 7.5.sp, fontWeight = FontWeight.Bold)
            Text(text = "TOO\nBIG\nTO\nFAIL", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Serif, textAlign = TextAlign.Center, lineHeight = 16.sp)
            Text(text = "BESTSELLER", color = Color(0xFFF7D046), fontSize = 6.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// 9. Nervous System Cover
@Composable
private fun NervousSystemCover() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD6D130))
            .padding(horizontal = 6.dp, vertical = 7.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "NERVOUS\nSYSTEM", color = Color(0xFF1B2A1E), fontSize = 9.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
            Text(text = "DR. L. MOORE", color = Color(0xFF1B2A1E), fontSize = 7.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// 10. The Magic Cover
@Composable
private fun TheMagicCover() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF147585))
            .padding(horizontal = 6.dp, vertical = 7.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "THE MAGIC\nOF THINKING\nBIG", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text(text = "DAVID J. SCHWARTZ", color = Color.White.copy(alpha = 0.9f), fontSize = 6.5.sp)
        }
    }
}

// 11. Power of Now Cover
@Composable
private fun PowerOfNowCover() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD4A034))
            .padding(horizontal = 6.dp, vertical = 7.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "THE POWER\nOF NOW", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
            Text(text = "ECKHART TOLLE", color = Color.White, fontSize = 7.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// 12. Man's Search Cover
@Composable
private fun MansSearchCover() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF284C35))
            .padding(horizontal = 6.dp, vertical = 7.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "MAN'S SEARCH\nFOR MEANING", color = Color(0xFFEDE4C8), fontSize = 8.5.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif, textAlign = TextAlign.Center)
            Text(text = "VIKTOR E. FRANKL", color = Color(0xFFEDE4C8), fontSize = 7.sp, fontFamily = FontFamily.Serif)
        }
    }
}

// 13. Generic Cover for dynamically added books
@Composable
private fun GenericCover(book: BookItem) {
    val bg = Color(book.primaryColorHex)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(horizontal = 6.dp, vertical = 7.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = book.title, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, maxLines = 3)
            Text(text = book.author, color = Color.White.copy(alpha = 0.9f), fontSize = 7.sp, fontWeight = FontWeight.Medium, maxLines = 1)
        }
    }
}
