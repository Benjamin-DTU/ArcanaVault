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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.arcanavault.model.data.IItem
import com.example.arcanavault.ui.components.ItemView

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
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(2.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        state = listState
    ) {
        items(items) { item ->
            ItemView(
                imageUrl = item.imageUrl,
                title = titleProvider(item),
                details = detailsProvider(item),
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
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(item.index) }
            )
        }
    }
}