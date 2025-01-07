package com.example.arcanavault.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun FetchingDataView(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val dotsCount by infiniteTransition.animateValue(
        initialValue = 1,
        targetValue = 4,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    val dots = when (dotsCount) {
        1 -> ".  "
        2 -> ".. "
        3 -> "..."
        else -> "   "
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Fetching data$dots", fontSize = 36.sp)
    }
}
