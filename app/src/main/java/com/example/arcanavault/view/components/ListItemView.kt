package com.example.arcanavault.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun ListItemView(
    imageUrl: String,
    name: String,
    description: String,
    isFavorite: Boolean?,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(2.dp, Color.Black)
            .background(Color.White)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Display Image
        AsyncImage(
            model = imageUrl,
            contentDescription = "$name Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .border(2.dp, Color.Black)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Name and Description
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Favorite Icon
        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = if (isFavorite == true) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = "Favorite",
                tint = Color.Black
            )
        }
    }
}
