package com.example.arcanavault.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FilterView(
    filterOptions: Map<String, List<String>>,
    selectedFilters: Map<String, List<String>>,
    onFilterChange: (String, List<String>) -> Unit,
    onClearAllFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showFilterDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier) {
        // Filter icon button to open or close the dialog
        IconButton(onClick = {
            // If in a sub-menu (filter category), go back to main menu instead of closing
            if (showFilterDialog && selectedCategory != null) {
                selectedCategory = null
            } else {
                showFilterDialog = !showFilterDialog
            }
        }) {
            Icon(
                if (showFilterDialog && selectedCategory != null) Icons.AutoMirrored.Filled.ArrowBack
                else if (showFilterDialog) Icons.Filled.Close
                else Icons.Filled.FilterList,
                contentDescription = if (showFilterDialog && selectedCategory != null) "Back" else "Close Filter"
            )
        }

        // Main dialog that stays open until the close button is pressed
        if (showFilterDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                // If a category is selected, show the options for that category
                if (selectedCategory != null) {
                    // Secondary view for the selected category
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text("Select ${selectedCategory}", style = MaterialTheme.typography.titleMedium)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Display each option as a selectable item
                        val options = filterOptions[selectedCategory] ?: emptyList()
                        options.forEach { option ->
                            val isSelected = selectedFilters[selectedCategory]?.contains(option) == true
                            FilterOption(
                                label = option,
                                selected = isSelected,
                                onSelect = { selected ->
                                    val updatedOptions = if (selected) {
                                        (selectedFilters[selectedCategory] ?: emptyList()) + option
                                    } else {
                                        (selectedFilters[selectedCategory] ?: emptyList()) - option
                                    }
                                    onFilterChange(selectedCategory!!, updatedOptions)
                                }
                            )
                        }
                    }
                } else {
                    // Main view with filter categories
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .fillMaxHeight(0.8f)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Select Filters", style = MaterialTheme.typography.titleMedium)

                            TextButton(onClick = onClearAllFilters) {
                                Text("Clear All")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Display each filter category as a button
                        filterOptions.keys.forEach { category ->
                            Button(
                                onClick = { selectedCategory = category },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(category)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterOption(
    label: String,
    selected: Boolean,
    onSelect: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = selected,
            onCheckedChange = onSelect
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(label)
    }
}
