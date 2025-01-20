package com.example.arcanavault.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.arcanavault.model.data.Spell

@Composable
fun SortView(
    onSortSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) } // Controls dropdown visibility

    // Use a distinct icon for sorting
    IconButton(onClick = { expanded = true }) {
        Icon(imageVector = Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort Options")
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        // List of sorting options
        val sortOptions = listOf(
            "A-Z", "Z-A", "Level", "School"
        )

        sortOptions.forEach { option ->
            DropdownMenuItem(
                text = { Text(option) },
                onClick = {
                    expanded = false
                    onSortSelected(option)
                }
            )
        }
    }
}


// Helper function to return a comparator for sorting spells based on selected option
fun getSortComparator(sortOption: String): Comparator<Spell> {
    return when (sortOption) {
        "A-Z" -> compareBy { it.name }
        "Z-A" -> compareByDescending { it.name }
        "Level" -> compareBy { it.level }
        "School" -> compareBy { it.school.name }
        else -> compareBy { it.name }
    }
}
