package com.example.arcanavault.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.arcanavault.R

private val schoolNameToDrawableMap: Map<String, Int> = mapOf(
    "abjuration" to R.drawable.abjuration,
    "conjuration" to R.drawable.conjuration,
    "divination" to R.drawable.divination,
    "enchantment" to R.drawable.enchantment,
    "evocation" to R.drawable.evocation,
    "illusion" to R.drawable.illusion,
    "necromancy" to R.drawable.necromancy,
    "transmutation" to R.drawable.transmutation
)

@Composable
fun ItemView(
    title: String,
    details: List<String>,
    modifier: Modifier = Modifier,
    actionsContent: @Composable (() -> Unit)? = null,
    surfaceColor: Color = MaterialTheme.colorScheme.surface,
    onSurfaceColor: Color = MaterialTheme.colorScheme.onSurface,
    imageLoader: ImageLoader // add a param for the image loader
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .background(surfaceColor)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Extract the schoolName from details
        val schoolName = details.find { it.contains("school", ignoreCase = true) }
            ?.split(": ")
            ?.lastOrNull()
            ?.lowercase()

        // Lookup the local resource ID from the map, fallback if not found
        val imageId = remember(schoolName) {
            schoolName?.let { schoolNameToDrawableMap[it] } ?: R.drawable.fallback
        }

        // Async image loading with the provided imageLoader
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageId)
                .build(),
            imageLoader = imageLoader,
            contentDescription = "$schoolName Image",
            modifier = Modifier
                .size(62.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .border(1.dp, onSurfaceColor, MaterialTheme.shapes.extraSmall),
            // e.g. contentScale = ContentScale.Crop,
        )

        // Title & details
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = onSurfaceColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                details.forEach { detail ->
                    val parts = detail.split(": ")
                    if (parts.size == 2) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("${parts[0]}: ")
                                }
                                append(parts[1])
                            },
                            fontSize = 13.sp,
                            color = onSurfaceColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else {
                        Text(
                            text = detail,
                            fontSize = 13.sp,
                            color = onSurfaceColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        // Optional actions on the right
        if (actionsContent != null) {
            IconButton(
                onClick = {},
                modifier = Modifier.size(36.dp),
            ) {
                actionsContent()
            }
        }
    }
}
