package com.example.arcanavault.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.arcanavault.ui.theme.ArcanaVaultTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
    title: String,
    buttons: List<@Composable () -> Unit>,
    scrollBehavior: TopAppBarScrollBehavior,
    content: (@Composable () -> Unit)? = null, // Optional content below the header
) {
    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            actions = {
                Row(
                    horizontalArrangement = Arrangement.End
                ) {
                    buttons.forEach { button ->
                        button()
                    }
                }
            },
            scrollBehavior = scrollBehavior,
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                scrolledContainerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
            )
        )
        // Add the optional content (e.g., FilterRow) below the TopAppBar
        content?.let {
            TopAppBar(
                title = { Text("") },
                scrollBehavior = scrollBehavior,
                actions = {
                    it()
                }
            )
        }
    }
}
