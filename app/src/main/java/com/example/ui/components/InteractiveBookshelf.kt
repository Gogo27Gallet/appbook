package com.example.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.BookItem

/**
 * InteractiveBookshelf provides a realistic, fluidly slidable bookshelf
 * where books sit upright on a wooden plank. Users can slide (scroll) across
 * the shelf freely without awkward position-swapping or abrupt reordering.
 */
@Composable
fun InteractiveBookshelf(
    books: List<BookItem>,
    onBookClick: (BookItem) -> Unit,
    modifier: Modifier = Modifier,
    onBooksReordered: ((List<BookItem>) -> Unit)? = null
) {
    val haptic = LocalHapticFeedback.current
    val listState = rememberLazyListState()

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Shelf Books Horizontal Slidable Row
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            LazyRow(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("shelf_books_row"),
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Bottom,
                flingBehavior = ScrollableDefaults.flingBehavior()
            ) {
                itemsIndexed(books, key = { index, book -> "${book.id}_$index" }) { index, book ->
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()

                    val scale by animateFloatAsState(
                        targetValue = if (isPressed) 0.96f else 1.0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        ),
                        label = "shelf_book_scale"
                    )

                    // Books sit upright on the shelf
                    Box(
                        modifier = Modifier
                            .scale(scale)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = ripple(bounded = true),
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onBookClick(book)
                                }
                            )
                            .testTag("shelf_book_${book.id}"),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        BookCoverView(
                            book = book,
                            modifier = Modifier
                                .width(94.dp)
                                .height(140.dp),
                            cornerRadius = 5.dp,
                            elevation = 8.dp
                        )
                    }
                }
            }
        }

        // Realistic Wooden Shelf bar directly under the books, spanning edge to edge
        WoodenShelf(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(top = 2.dp),
            shelfHeight = 16.dp
        )
    }
}
