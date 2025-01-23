package com.example.arcanavault.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    actionsContent: @Composable() (() -> Unit)? = null,
    surfaceColor: Color = MaterialTheme.colorScheme.surface,
    onSurfaceColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .shadow(3.dp, RectangleShape, spotColor = Color.Gray)
            .background(surfaceColor)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val schoolName = details.find { it.contains("school", ignoreCase = true) }
            ?.split(": ")
            ?.lastOrNull()
            ?.lowercase()

        val context = LocalContext.current
        val imageId = remember(schoolName) {
            schoolName?.let { schoolNameToDrawableMap[it] } ?: R.drawable.fallback
        }
        // Image on the left
        Image(
            painter = painterResource(id = imageId),
            contentDescription = "$schoolName Image",
            modifier = Modifier
                .size(62.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .border(1.dp, onSurfaceColor, MaterialTheme.shapes.extraSmall)
        )

        /*AsyncImage(
            model = imageUrl,
            contentDescription = "$title Image",
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier
                .size(62.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .border(1.dp, onSurfaceColor, MaterialTheme.shapes.extraSmall)
        )*/

        // Title and details
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Title
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = onSurfaceColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Details displayed below the title
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =  Arrangement.spacedBy(8.dp)
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

        // Actions content on the right
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
