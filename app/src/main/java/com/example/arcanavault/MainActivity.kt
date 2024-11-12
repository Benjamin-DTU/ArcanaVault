package com.example.arcanavault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.arcanavault.controller.api.ApiClient
import com.example.arcanavault.model.data.IEntity
import com.example.arcanavault.view.components.ListView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private val apiClient = ApiClient()
    private val entities = mutableListOf<IEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Fetch spells from ApiClient
        CoroutineScope(Dispatchers.IO).launch {
            val fetchedEntities = apiClient.getAllSpells()
            withContext(Dispatchers.Main) {
                entities.addAll(fetchedEntities)
                // Once entities are loaded, display the content
                setContent {
                    MaterialTheme {
                        Surface {
                            ListView(entities = entities) // Pass the list of entities to ListView
                        }
                    }
                }
            }
        }
    }
}
