package com.example.arcanavault.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.arcanavault.ui.theme.ArcanaVaultTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
    title: String,
    buttons: List<@Composable () -> Unit>,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    ArcanaVaultTheme {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge

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
                scrolledContainerColor = MaterialTheme.colorScheme.surface
            )
        )
    }
}
