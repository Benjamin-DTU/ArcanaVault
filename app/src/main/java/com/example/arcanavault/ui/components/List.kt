package com.example.arcanavault.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.imageLoader
import coil.request.ImageRequest
import com.example.arcanavault.model.data.IItem
import com.example.arcanavault.R

private val schoolNameToDrawableMap: Map<String, Int> = mapOf(
    "abjuration" to R.drawable.abjuration,
    "conjuration" to R.drawable.conjuration,
    "divination" to R.drawable.divination,
    "enchantment" to R.drawable.enchantment,
    "evocation" to R.drawable.evocation,
    "illusion" to R.drawable.illusion,
    "necromancy" to R.drawable.necromancy,
    "transmutation" to R.drawable.transmutation
)

@Composable
fun PreloadSchoolImages() {
    val context = LocalContext.current
    val imageLoader = context.imageLoader

    // Runs once and enqueues each drawable in schoolNameToDrawableMap for async caching/decoding
    LaunchedEffect(Unit) {
        schoolNameToDrawableMap.values.forEach { drawableRes ->
            val request = ImageRequest.Builder(context)
                .data(drawableRes)
                // You can specify a size to downsample if needed:
                .size(200, 200)
                .build()
            imageLoader.enqueue(request)
        }
    }
}

@Composable
fun <T : IItem> ListView(
    items: List<T>,
    titleProvider: (T) -> String,
    detailsProvider: (T) -> List<String>,
    onItemClick: (String) -> Unit,
    onFavoriteClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = remember { LazyListState() }
) {
    // Preload the images once here
    PreloadSchoolImages()

    // Grab the ImageLoader to pass down to items
    val imageLoader = LocalContext.current.imageLoader

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(2.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        state = listState
    ) {
        items(items) { item ->
            ItemView(
                title = titleProvider(item),
                details = detailsProvider(item),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(item.index) },
                actionsContent = {
                    IconButton(onClick = { onFavoriteClick(item) }) {
                        if (item.isFavorite) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Favorite fill",
                                tint = Color.Yellow
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.StarBorder,
                                contentDescription = "Favorite border",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                },
                imageLoader = imageLoader // pass it to the item
            )
        }
    }
}