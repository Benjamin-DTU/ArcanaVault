package com.example.arcanavault.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(2.dp, Color.Black)
            .background(Color.Gray)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Display Image with border
        AsyncImage(
            model = imageUrl,
            contentDescription = "$name Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .border(2.dp, Color.Black)
                .padding(4.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Column for name and description
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Display Name
            Text(
                text = name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Display Description with border
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color.Black)
                    .padding(8.dp)
            ) {
                Text(
                    text = description,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    }
}
