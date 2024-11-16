package com.example.arcanavault.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arcanavault.controller.api.ApiClient
import com.example.arcanavault.model.data.Spell

@Composable
fun SpellDetailsView(
    spell: Spell?,
    modifier: Modifier = Modifier
) {
    if (spell != null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = spell.name,
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Level: ${spell.level}",
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "School: ${spell.school?.name ?: "Unknown"}",
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Casting Time: ${spell.castingTime}",
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Range: ${spell.range}",
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Description:",
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
            )
            spell.description.forEach { line ->
                Text(
                    text = line,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Spell details not found.",
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
            )
        }
    }
}
