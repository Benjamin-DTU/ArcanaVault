package com.example.arcanavault.ui.screens

import android.content.res.Resources
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.arcanavault.AppState
import com.example.arcanavault.model.data.Spell
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun SpellDetailsView(
    appState: AppState,
    spell: Spell?,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    var showConditionDialog by remember { mutableStateOf(false) }
    var selectedCondition by remember { mutableStateOf<com.example.arcanavault.model.data.Condition?>(null) }

    if (spell != null) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .height(30.dp)
                            .width(50.dp)
                            .padding(end = 24.dp)
                            .clickable { onBackClick() }
                    )
                }
            }

            // Spell name
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = spell.name,
                        style = MaterialTheme.typography.titleLarge
                    )

                }
            }

            // Spell level
            item {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                            append("Level: ")
                        }
                        append(spell.level.toString())
                    },
                    fontSize = 14.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Spell school
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                append("School: ")
                            }
                            append(spell.school?.name)
                        },
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    AsyncImage(
                        model = spell.imageUrl,
                        contentDescription = "${spell.name} Image",
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(MaterialTheme.shapes.extraSmall)
                    )
                }


                Spacer(modifier = Modifier.height(8.dp))
            }

            // Casting time
            item {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                            append("Casting Time: ")
                        }
                        append(spell.castingTime)
                    },
                    fontSize = 14.sp,
                    color = Color.Black
                )


                Spacer(modifier = Modifier.height(8.dp))
            }

            // Range
            item {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                            append("Range: ")
                        }
                        append(spell.range)
                    },
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Description header
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Description",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    HorizontalDivider(
                        color = Color.Black,
                        thickness = 1.5.dp,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp, top = 4.dp)
                    )
                }
            }

            // Spell Description (markdown)
            item {
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(515.dp) //Height of the container
                    ) {
                    val scrollState = rememberScrollState()
                    val viewMaxHeight = maxHeight.value
                    val columnMaxScroll = scrollState.maxValue.toFloat()
                    val scrollStateValue = scrollState.value.toFloat()
                    val paddingSize = (scrollStateValue * viewMaxHeight) / columnMaxScroll
                    val animation = animateDpAsState(targetValue = paddingSize.dp)
                    val scrollBarHeight = viewMaxHeight / spell.description.size

                    // Scrollable content
                    Column(
                        modifier = Modifier
                            .verticalScroll(state = scrollState)
                            .fillMaxWidth()
                            .padding(end = 10.dp), // Space for the scroll-bar
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        var isTable = false
                        val tableLines = mutableListOf<String>()

                        val customTextStyle = TextStyle(
                            fontSize = 14.sp
                        )

                        spell.description.forEach { line ->
                            if (line.startsWith("|") && line.endsWith("|")) {
                                isTable = true
                                tableLines.add(line)
                            } else if (isTable && line.startsWith("|---")) {
                                tableLines.add(line)
                            } else {
                                if (isTable) {
                                    Box(
                                        modifier = Modifier
                                            .width(400.dp)
                                            .height(400.dp)
                                    ) {
                                        ProvideTextStyle(customTextStyle) {
                                            MarkdownText(
                                                markdown = tableLines.joinToString("\n"),
                                                onLinkClicked = { url ->
                                                    if (url.startsWith("navigate://conditions/")) {
                                                        val conditionName = url
                                                            .substringAfter("navigate://conditions/")
                                                            .replace("-", " ")
                                                            .trim()

                                                        if (conditionName.isNotBlank()) {
                                                            val condition =
                                                                appState.getConditionByName(
                                                                    conditionName
                                                                )
                                                            if (condition != null) {
                                                                selectedCondition = condition
                                                                showConditionDialog = true
                                                            }
                                                        }
                                                    }
                                                },
                                                linkColor = Color.Blue,
                                                enableUnderlineForLink = false,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 4.dp)
                                            )
                                        }
                                    }
                                    tableLines.clear()
                                    isTable = false
                                }

                                Box(modifier = Modifier.fillMaxWidth()) {
                                    ProvideTextStyle(customTextStyle) {
                                        MarkdownText(
                                            markdown = line,
                                            onLinkClicked = { url ->
                                                if (url.startsWith("navigate://conditions/")) {
                                                    val conditionName = url
                                                        .substringAfter("navigate://conditions/")
                                                        .replace("-", " ")
                                                        .trim()

                                                    if (conditionName.isNotBlank()) {
                                                        val condition = appState.getConditionByName(
                                                            conditionName
                                                        )
                                                        if (condition != null) {
                                                            selectedCondition = condition
                                                            showConditionDialog = true
                                                        }
                                                    }
                                                }
                                            },
                                            linkColor = Color.Blue,
                                            enableUnderlineForLink = false,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                        )

                                    }
                                }
                            }
                        }

                        if (tableLines.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .width(800.dp)
                            ) {
                                ProvideTextStyle(customTextStyle) {
                                    MarkdownText(
                                        markdown = tableLines.joinToString("\n"),
                                        onLinkClicked = { url ->
                                            if (url.startsWith("navigate://conditions/")) {
                                                val conditionName = url
                                                    .substringAfter("navigate://conditions/")
                                                    .replace("-", " ")
                                                    .trim()

                                                if (conditionName.isNotBlank()) {
                                                    val condition =
                                                        appState.getConditionByName(conditionName)
                                                    if (condition != null) {
                                                        selectedCondition = condition
                                                        showConditionDialog = true
                                                    }
                                                }
                                            }
                                        },
                                        linkColor = Color.Blue,
                                        enableUnderlineForLink = false,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Custom scrollbar
                    if (scrollState.value < scrollState.maxValue) {
                        Box(
                            modifier = Modifier
                                .paddingFromBaseline(animation.value)
                                .padding(all = 0.dp)
                                .height(scrollBarHeight.dp)
                                .width(4.dp)
                                .background(
                                    color = Color.Black,
                                )
                                .align(Alignment.TopEnd)
                        )
                    }
                }
            }

            // Damage information
            if (spell.damage != null) {
                item {

                    Text(
                        text = "Damage Type: ${spell.damage.damageType?.name ?: "Unknown"}",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (spell.damage.damageAtSlotLevel.isNotEmpty()) {
                    item {
                        Text(
                            text = "Damage by Slot Level:",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        spell.damage.damageAtSlotLevel.forEach { (lvl, damage) ->
                            Text(
                                text = "Slot Level $lvl: $damage",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                if (spell.damage.damageAtCharLevel.isNotEmpty()) {
                    item {
                        Text(
                            text = "Damage by Character Level:",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        spell.damage.damageAtCharLevel.forEach { (charLvl, damage) ->
                            Text(
                                text = "Character Level $charLvl: $damage",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // Render condition dialog
        if (showConditionDialog && selectedCondition != null) {
            selectedCondition?.description?.toList()?.let {
                ConditionDialog(
                    conditionName = selectedCondition?.name.toString(),
                    conditionDescription = it,
                    onDismiss = { showConditionDialog = false }
                )
            }
        }

    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Spell details not found.",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun ConditionDialog(
    conditionName: String,
    conditionDescription: List<String>,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = MaterialTheme.shapes.small
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Title
                Text(
                    text = conditionName,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth()
                )


                // Description
                conditionDescription.forEach { description ->
                    MarkdownText(
                        markdown = description,
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                }


                // Dismiss button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(onClick = { onDismiss() }) {
                        Text(text = "Close")
                    }
                }
            }
        }
    }
}

