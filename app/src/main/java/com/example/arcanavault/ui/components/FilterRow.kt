package com.example.arcanavault.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun FilterRow(
    selectedFilters: Map<String, List<String>>,
    onRemoveFilter: (String, String) -> Unit,
    scrollFraction: Float,
    itemCount: Int?,
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
                // Physically shrink the height as scrollFraction grows
                .height(currentHeight)
                // Optionally fade out as well
                .alpha(fraction)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .horizontalScroll(rememberScrollState())
                    .align(Alignment.CenterStart)
            ) {
                if (itemCount != null) {
                    Text(
                        text = "Count: $itemCount",
                        modifier = Modifier.padding(4.dp)
                    )
                }

                selectedFilters.forEach { (category, options) ->
                    options.forEach { option ->
                        FilterTag(
                            category = category,
                            option = option,
                            onRemove = { onRemoveFilter(category, option) },

                        )
                    }
                }
            }
        }
    }
}
