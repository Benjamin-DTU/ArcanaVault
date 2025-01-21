package com.example.arcanavault.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomScrollbar(descHeight: Dp, scrollState: ScrollState, type: String, modifier: Modifier = Modifier) {
    val thumbSize = if (scrollState.maxValue > 0) { 50.dp } else { 0.dp }

    val thumbOffset = if (scrollState.maxValue > 0 && type == "horizontal") {
        scrollState.value.toFloat() / scrollState.maxValue * (375.dp.value - thumbSize.value)
    } else if (scrollState.maxValue > 0 && type == "vertical") {
        scrollState.value.toFloat() / scrollState.maxValue * (descHeight.value - thumbSize.value)
    } else {
        0f
    }

    if (scrollState.maxValue > 0) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            when (type) {
                "vertical" -> {
                    Box(
                        modifier = Modifier.fillMaxHeight()
                            .width(4.dp)
                            .background(Color.Transparent)
                            .offset(x = 8.dp)
                            .align(Alignment.CenterEnd)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .height(thumbSize)
                                .offset(y = thumbOffset.dp)
                                .background(Color.Black)
                                .clip(MaterialTheme.shapes.extraSmall)
                        )
                    }
                }

                "horizontal" -> {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .height(4.dp)
                            .background(Color.Transparent)
                            .align(Alignment.BottomCenter)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(thumbSize)
                                .height(4.dp)
                                .offset(x = thumbOffset.dp)
                                .background(Color.Black)
                                .clip(MaterialTheme.shapes.extraSmall)
                        )
                    }
                }
                else -> throw IllegalArgumentException("Invalid type!")
            }
        }
    }
}