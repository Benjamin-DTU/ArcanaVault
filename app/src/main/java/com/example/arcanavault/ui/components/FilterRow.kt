package com.example.arcanavault.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

// This component was written with the assistance of ChatGPT
@Composable
fun FilterRow(
    selectedFilters: Map<String, List<String>>,
    onRemoveFilter: (String, String) -> Unit,
    onClearAll: () -> Unit,
    scrollFraction: Float,
) {
    // Interpolate fraction from 1.0 (no scroll) down to 0.0 (fully scrolled)
    val fraction = (1f - scrollFraction).coerceIn(0f, 1f)

    // Decide a max height for the row
    val maxHeight = 44.dp
    val currentHeight = maxHeight * fraction

    // Scroll state to track scroll position
    val scrollState = rememberScrollState()

    if (fraction > 0f) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(currentHeight)
                .alpha(fraction)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Scrollable Row for filters
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Row(
                            modifier = Modifier.horizontalScroll(scrollState),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            selectedFilters.forEach { (category, options) ->
                                options.forEach { option ->
                                    FilterTag(
                                        category = category,
                                        option = option,
                                        onRemove = { onRemoveFilter(category, option) }
                                    )
                                }
                            }
                        }

                        // Add the CustomScrollbar
                        CustomScrollbar(
                            descHeight = 282.dp,
                            scrollState = scrollState,
                            type = "filter"
                        )
                    }
                }

                // "Clear All" button
                if (selectedFilters.isNotEmpty()) {
                    IconButton(
                        onClick = { onClearAll() },
                        modifier = Modifier
                            .padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Clear All Filters"
                        )
                    }
                }
            }
        }
    }
}





