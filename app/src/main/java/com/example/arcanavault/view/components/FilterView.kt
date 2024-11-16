package com.example.arcanavault.view.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.RectangleShape
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
        // Filter icon button
        IconButton(onClick = {
            if (showFilterDialog && selectedCategory != null) {
                selectedCategory = null // Go back to main menu
            } else {
                showFilterDialog = !showFilterDialog
            }
        }) {
            Icon(
                imageVector = if (showFilterDialog && selectedCategory != null) Icons.AutoMirrored.Filled.ArrowBack
                else if (showFilterDialog) Icons.Filled.Close
                else Icons.Filled.FilterList,
                contentDescription = "Filter Menu"
            )
        }

        // Dialog for filters
        if (showFilterDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                if (selectedCategory != null) {
                    // Subfilter view
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .fillMaxHeight(0.8f)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text("Select ${selectedCategory}", style = MaterialTheme.typography.titleMedium)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Display options for the selected category
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
                    // Main filter categories view
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
                            Text("Filters", style = MaterialTheme.typography.titleMedium)

                            TextButton(
                                onClick = onClearAllFilters,
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Color(0xFF8B0000)
                                )
                            ) {
                                Text("Clear All")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Display categories
                        filterOptions.keys.forEach { category ->
                            OutlinedButton(
                                onClick = { selectedCategory = category },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.White,
                                    contentColor = Color.Black
                                ),
                                border = BorderStroke(1.dp, Color.Black),
                                shape = RectangleShape
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
