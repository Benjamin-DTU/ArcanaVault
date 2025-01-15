import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.arcanavault.ui.components.FilterTag
import kotlin.math.roundToInt

@Composable
fun FilterRow(
    selectedFilters: Map<String, List<String>>,
    onRemoveFilter: (String, String) -> Unit,
    scrollFraction: Float // Accept scrollFraction as a parameter
) {
    // Only render the FilterRow when it is partially or fully visible
    if (scrollFraction < 1f) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .horizontalScroll(rememberScrollState())
                .graphicsLayer(
                    alpha = 1f - scrollFraction, // Adjust opacity based on scroll fraction
                    translationY = -(scrollFraction * 50).roundToInt().toFloat() // Adjust vertical position
                )
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

