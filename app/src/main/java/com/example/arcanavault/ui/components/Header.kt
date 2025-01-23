package com.example.arcanavault.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
    title: String,
    buttons: List<@Composable () -> Unit>,
    scrollBehavior: TopAppBarScrollBehavior,
    content: (@Composable () -> Unit)? = null, // Optional content below the header
) {
    Column {
        TopAppBar(
            title = {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.padding(start = 16.dp) // Add padding to the left
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            actions = {
                Row(
                    horizontalArrangement = Arrangement.End
                ) {
                    buttons.forEach { button -> button() }
                }
            },
            scrollBehavior = scrollBehavior,
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                scrolledContainerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            )
        )

        // Add optional content below the header if provided
        if (content != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 50.dp)
                    //.padding(horizontal = 8.dp)
            ) {
                content()
            }
        }
    }
}
