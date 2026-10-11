package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun WoodenShelf(
    modifier: Modifier = Modifier,
    shelfHeight: Dp = 14.dp
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(shelfHeight)
    ) {
        val w = size.width
        val h = size.height

        // Top beveled highlight edge of the shelf
        val topBevelHeight = h * 0.22f
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFC9A174),
                    Color(0xFFA67B4E)
                ),
                startY = 0f,
                endY = topBevelHeight
            ),
            topLeft = Offset.Zero,
            size = Size(w, topBevelHeight)
        )

        // Subtle top lip reflection line
        drawLine(
            color = Color(0xFFDEC19C).copy(alpha = 0.9f),
            start = Offset(0f, 1f),
            end = Offset(w, 1f),
            strokeWidth = 1.5f
        )

        // Main front plank of the shelf
        val mainPlankHeight = h * 0.58f
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF8F6136),
                    Color(0xFF754924),
                    Color(0xFF5E3616)
                ),
                startY = topBevelHeight,
                endY = topBevelHeight + mainPlankHeight
            ),
            topLeft = Offset(0f, topBevelHeight),
            size = Size(w, mainPlankHeight)
        )

        // Subtle horizontal wood grain textures
        drawLine(
            color = Color(0xFF6B421E).copy(alpha = 0.6f),
            start = Offset(0f, topBevelHeight + mainPlankHeight * 0.4f),
            end = Offset(w, topBevelHeight + mainPlankHeight * 0.4f),
            strokeWidth = 1f
        )
        drawLine(
            color = Color(0xFF9E7044).copy(alpha = 0.4f),
            start = Offset(0f, topBevelHeight + mainPlankHeight * 0.7f),
            end = Offset(w, topBevelHeight + mainPlankHeight * 0.7f),
            strokeWidth = 1f
        )

        // Deep bottom shadow beneath the shelf
        val shadowHeight = h - (topBevelHeight + mainPlankHeight)
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF3B200C).copy(alpha = 0.7f),
                    Color(0xFF261305).copy(alpha = 0.25f),
                    Color.Transparent
                ),
                startY = topBevelHeight + mainPlankHeight,
                endY = h
            ),
            topLeft = Offset(0f, topBevelHeight + mainPlankHeight),
            size = Size(w, shadowHeight)
        )
    }
}
