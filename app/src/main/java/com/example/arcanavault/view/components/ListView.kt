package com.example.arcanavault.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arcanavault.model.data.IEntity

@Composable
fun ListView(
    entities: List<IEntity>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(entities) { entity ->
            EntityView(
                imageUrl = entity.url,
                name = entity.name,
                description = "Level ${entity.level}",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
