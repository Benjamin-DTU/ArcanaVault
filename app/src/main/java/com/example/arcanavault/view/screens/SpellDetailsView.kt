package com.example.arcanavault.view.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arcanavault.controller.api.ApiClient

@Composable
fun SpellDetailsView(apiClient: ApiClient, modifier: Modifier = Modifier) {
    Text("Spell Details Screen", modifier = modifier)
}