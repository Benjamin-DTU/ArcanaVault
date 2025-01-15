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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
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
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .shadow(3.5.dp, spotColor = Color.Black)
            //.background(Color.White)
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Image on the left
        AsyncImage(
            model = imageUrl,
            contentDescription = "$name Image",
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier
                .size(62.dp)
                .clip(MaterialTheme.shapes.extraSmall)
        )



        // Title and details
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 30.dp, vertical = 8.dp)

        ) {
            Text(
                text = name,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                //color = Color.Black,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))
            Row {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                            append("Level: ")
                        }
                        append(level?.toString() ?: "N/A")
                    },
                    fontSize = 13.sp,
                    //color = Color.Black
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                            append("School: ")
                        }
                        append(school.name)
                    },
                    fontSize = 13.sp,
                    //color = Color.Black
                    color = MaterialTheme.colorScheme.onSurface
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
                tint = if (isFavorite == true) Color(0xFFFFBD05) else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .size(35.dp)
            )
        }
    }
}

