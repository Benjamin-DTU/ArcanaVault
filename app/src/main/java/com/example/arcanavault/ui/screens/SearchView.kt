package com.example.arcanavault.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.arcanavault.controller.api.ApiClient

@Composable
fun SearchView(
    apiClient: ApiClient,
    navController: NavController,
    modifier: Modifier = Modifier) {
    Text("Search Screen", modifier = modifier)
}