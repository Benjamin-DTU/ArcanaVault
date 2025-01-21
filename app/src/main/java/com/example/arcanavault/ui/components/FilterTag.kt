package com.example.arcanavault.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun FilterTag(category: String, option: String, onRemove: () -> Unit) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .padding(4.dp)
            .clickable { onRemove() } // Remove filter when clicked
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "$category: $option",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
    }
}
