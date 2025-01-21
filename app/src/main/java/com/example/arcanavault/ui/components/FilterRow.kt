package com.example.arcanavault.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

@Composable
fun FilterRow(
    selectedFilters: Map<String, List<String>>,
    onRemoveFilter: (String, String) -> Unit,
    scrollFraction: Float,
) {
    // Interpolate fraction from 1.0 (no scroll) down to 0.0 (fully scrolled)
    val fraction = (1f - scrollFraction).coerceIn(0f, 1f)

    // Decide a max height for your row
    val maxHeight = 44.dp
    val currentHeight = maxHeight * fraction

    // Only show the row if there is something to show (fraction > 0f)
    if (fraction > 0f) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(currentHeight)
                .alpha(fraction)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Scrollable Row for filters
                Row(
                    modifier = Modifier
                        .weight(1f) // Limit width of the scrollable area
                        .horizontalScroll(rememberScrollState()),
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
            }
        }
    }
}
