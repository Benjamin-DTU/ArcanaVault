package com.example.arcanavault.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    filterOptions: Map<String, List<String>>,
    selectedFilters: Map<String, List<String>>,
    onFilterChange: (String, List<String>) -> Unit,
    onClearAllFilters: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val titleText = if (selectedCategory != null) {
                        "Select $selectedCategory"
                    } else {
                        "Filters"
                    }
                    Text(
                        text = titleText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (selectedCategory != null) {
                                selectedCategory = null
                            } else {
                                onNavigateBack()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (selectedCategory != null)
                                Icons.AutoMirrored.Filled.ArrowBack
                            else
                                Icons.Filled.Close,
                            contentDescription = "Back or Close"
                        )
                    }
                },
                actions = {
                    if (selectedCategory == null) {
                        TextButton(
                            onClick = onClearAllFilters,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color(0xFF8B0000)
                            )
                        ) {
                            Text("Clear All")
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
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            if (selectedCategory == null) {
                // Show main categories
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
            } else {
                // Show options within the selected category
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
