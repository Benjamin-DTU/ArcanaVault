package com.example.arcanavault.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.arcanavault.model.data.ItemReference

@Composable
fun ListItemView(
    imageUrl: String,
    level: Int?,
    school: ItemReference,
    name: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 4.dp)
            .border(1.dp, Color.LightGray, shape = MaterialTheme.shapes.medium)
            .background(Color.White)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Image on the left
        AsyncImage(
            model = imageUrl,
            contentDescription = "$name Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.extraSmall)
        )



        // Title and details
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 35.dp, vertical = 4.dp)

        ) {
            Text(
                text = name,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))
        Row(
        ){

            Text(
                text = "Level: ${level ?: "N/A"}",
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "School: ${school.name}",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
        }

        // Favorite icon on the right
        IconButton(
            onClick = onFavoriteClick
        ) {
            Icon(
                imageVector = if (isFavorite == true) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = "Favorite",
                tint = Color.Black
            )
        }
    }
}

