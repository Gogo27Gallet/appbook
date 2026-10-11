package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BookItem
import com.example.model.CollectionItem
import com.example.ui.components.BookCoverView
import com.example.ui.components.InteractiveBookshelf
import com.example.ui.components.WoodenShelf
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.SearchBarBackground
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun CollectionDetailScreen(
    collection: CollectionItem,
    onBack: () -> Unit,
    onBookClick: (BookItem) -> Unit,
    onAddBookToShelf: () -> Unit,
    onShelfReordered: (List<BookItem>) -> Unit = {},
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var searchQuery by remember { mutableStateOf("") }
    var shelfBooks by remember(collection) { mutableStateOf(collection.featuredBooks) }

    val displayBooks = remember(collection, searchQuery) {
        if (searchQuery.isBlank()) {
            collection.allBooks
        } else {
            val q = searchQuery.trim().lowercase()
            collection.allBooks.filter {
                it.title.lowercase().contains(q) || it.author.lowercase().contains(q)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .statusBarsPadding()
    ) {
        // Top Navigation Header with centered Title and Subtitle matching Screenshot 2
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 12.dp, start = 12.dp, end = 12.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(40.dp)
                    .testTag("collection_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to collections",
                    tint = TextPrimary
                )
            }

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = collection.title,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = collection.subtitle,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Normal
                )
            }

            IconButton(
                onClick = onAddBookToShelf,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(36.dp)
                    .testTag("add_book_to_shelf_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add book",
                    tint = TextPrimary
                )
            }
        }

        // Search pill "Search this collection"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .height(48.dp)
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(24.dp), clip = false)
                .clip(RoundedCornerShape(24.dp))
                .background(SearchBarBackground)
                .padding(horizontal = 14.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TextTertiary,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Box(modifier = Modifier.weight(1f)) {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "Search this collection",
                            color = TextTertiary,
                            fontSize = 14.sp
                        )
                    }
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        singleLine = true,
                        textStyle = TextStyle(
                            color = TextPrimary,
                            fontSize = 14.sp
                        ),
                        cursorBrush = SolidColor(TextPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("collection_detail_search_input")
                    )
                }

                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { searchQuery = "" },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Main Content using LazyVerticalGrid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section 1: "Top books in collection shelf"
            item(span = { GridItemSpan(2) }) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Top books in collection shelf",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        modifier = Modifier.padding(start = 18.dp, top = 12.dp, bottom = 12.dp)
                    )

                    InteractiveBookshelf(
                        books = shelfBooks,
                        onBookClick = onBookClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Section 2: "Showing all" header
            item(span = { GridItemSpan(2) }) {
                Text(
                    text = "Showing all",
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    modifier = Modifier.padding(start = 18.dp, top = 18.dp, bottom = 2.dp)
                )
            }

            // Grid items: 2 columns of full book covers with true aspect ratio matching Screenshot 2
            items(displayBooks, key = { it.id }) { book ->
                val interactionSource = remember { MutableInteractionSource() }
                val isPressed by interactionSource.collectIsPressedAsState()
                val scale by animateFloatAsState(
                    targetValue = if (isPressed) 0.96f else 1.0f,
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                    label = "grid_book_scale"
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .scale(scale)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = ripple(bounded = true),
                            onClick = { onBookClick(book) }
                        )
                        .testTag("grid_book_${book.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    BookCoverView(
                        book = book,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.68f),
                        cornerRadius = 8.dp,
                        elevation = 8.dp
                    )
                }
            }

            if (displayBooks.isEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = "No books found matching '$searchQuery'",
                        fontSize = 14.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp)
                    )
                }
            }
        }
    }
}
