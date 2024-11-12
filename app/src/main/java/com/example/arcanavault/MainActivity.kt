package com.example.arcanavault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.arcanavault.controller.api.ApiClient
import com.example.arcanavault.view.components.Hotbar
import com.example.arcanavault.view.screens.FavouritesView
import com.example.arcanavault.view.screens.SpellListView


class MainActivity : ComponentActivity() {
    private val apiClient = ApiClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                val selectedScreen = remember { mutableStateOf("home") }

                Scaffold(
                    bottomBar = {
                        Hotbar(selectedScreen = selectedScreen)
                    }
                ) { paddingValues ->
                    Surface(modifier = Modifier.padding(paddingValues)) {
                        when (selectedScreen.value) {
                            "home" -> SpellListView(apiClient = apiClient)
                            "favorite" -> FavouritesView(apiClient = apiClient)
                        }
                    }
                }
            }
        }
    }
}
