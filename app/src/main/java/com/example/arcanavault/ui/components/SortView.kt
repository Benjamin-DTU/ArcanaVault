package com.example.arcanavault.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arcanavault.model.data.Spell

@Composable
fun SortView(
    selectedSort: String,
    isSortOrderAscending: Boolean,
    onSortSelected: (String, Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(false) } // Controls dropdown visibility

    Box {
        // Icon Button for sorting
        IconButton(onClick = { expanded = !expanded }) {
            Box {
                // Arrow icon
                Icon(
                    imageVector = if (isSortOrderAscending) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
                    contentDescription = "Sort Options",
                    modifier = Modifier.size(24.dp) // Ensure consistent arrow size
                )
            }
        }

        // Dropdown Menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            val sortOptions = listOf("Name", "Level", "School")

            sortOptions.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Sorting option name
                            Text(
                                text = option,
                                modifier = Modifier.weight(1f)
                            )
                            if (option == selectedSort) {
                                Icon(
                                    imageVector = if (isSortOrderAscending) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
                                    contentDescription = "Sort Direction",
                                    modifier = Modifier.size(16.dp) // Ensure consistent icon size
                                )
                            }
                        }
                    },
                    onClick = {
                        val newAscending = if (selectedSort == option) !isSortOrderAscending else true
                        onSortSelected(option, newAscending)
                    }
                )
            }
        }
    }
}

// Helper function to get a comparator based on selected sorting option and direction
fun getSortComparator(sortOption: String, isAscending: Boolean): Comparator<Spell> {
    return when (sortOption) {
        "Name" -> if (isAscending) compareBy { it.name } else compareByDescending { it.name }
        "Level" -> if (isAscending) compareBy { it.level } else compareByDescending { it.level }
        "School" -> if (isAscending) compareBy { it.school.name } else compareByDescending { it.school.name }
        else -> compareBy { it.name }
    }
}
