package com.example.arcanavault.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterView(
    filterOptions: Map<String, List<String>>,
    selectedFilters: Map<String, List<String>>,
    onFilterChange: (String, List<String>) -> Unit,
    onClearAllFilters: () -> Unit,
    itemCount: Int?
) {
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp) // Consistent padding for title and count
                    ) {
                        val titleText = if (selectedCategory != null) {
                            "Select $selectedCategory"
                        } else {
                            "Filters"
                        }
                        Text(
                            text = titleText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f) // Title takes up available space
                        )
                        if (itemCount != null) {
                            Text(
                                text = "Count: $itemCount",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), // Bold style added
                            )
                        }
                    }
                },
                navigationIcon = {
                    if (selectedCategory != null) {
                        IconButton(onClick = { selectedCategory = null }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp) // Ensure consistent horizontal padding
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                if (selectedCategory == null) {
                    filterOptions.keys.forEach { category ->
                        OutlinedButton(
                            onClick = { selectedCategory = category },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                            shape = RectangleShape
                        ) {
                            Text(category)
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = onClearAllFilters,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color(0xFF8B0000)
                            )
                        ) {
                            Text(
                                text = "Clear All",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold) // Bold style added
                            )
                        }
                    }
                } else {
                    val options = filterOptions[selectedCategory] ?: emptyList()
                    options.forEach { option ->
                        val isSelected = selectedFilters[selectedCategory]?.contains(option) == true
                        FilterOption(
                            label = option,
                            selected = isSelected,
                            onSelect = { newValue ->
                                val updatedList = if (newValue) {
                                    (selectedFilters[selectedCategory] ?: emptyList()) + option
                                } else {
                                    (selectedFilters[selectedCategory] ?: emptyList()) - option
                                }
                                onFilterChange(selectedCategory!!, updatedList)
                            }
                        )
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
            onCheckedChange = { isChecked ->
                onSelect(isChecked)
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(label)
    }
}

