package com.example.arcanavault.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilterRow(
    selectedFilters: Map<String, List<String>>,
    onRemoveFilter: (String, String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .horizontalScroll(rememberScrollState())
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