package com.example.arcanavault.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.arcanavault.AppState
import com.example.arcanavault.model.data.Rule
import com.example.arcanavault.model.data.Spell
import com.example.arcanavault.ui.components.CustomScrollbar
import dev.jeziellago.compose.markdowntext.MarkdownText
import com.example.arcanavault.DB.FunctionsDB



@Composable
fun SpellDetailsView(
    appState: AppState,
    spell: Spell?,
    modifier: Modifier = Modifier,
    functionsDB: FunctionsDB,
    onBackClick: () -> Unit
) {
    var showConditionDialog by remember { mutableStateOf(false) }
    var showRuleDialog by remember { mutableStateOf(false) }
    var selectedCondition by remember {
        mutableStateOf<com.example.arcanavault.model.data.Condition?>(
            null
        )
    }
    var selectedRule by remember { mutableStateOf<Rule?>(null) }
    var isBackProcessing by remember { mutableStateOf(false) }

    if (isBackProcessing) {
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(300)
            isBackProcessing = false
        }
    }

    if (spell != null) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Back Button
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable(enabled = !isBackProcessing) {
                            if (!isBackProcessing) {
                                isBackProcessing = true
                                onBackClick()
                            }
                        }
                )

                // Favorite Button
                val isFavorite = remember { mutableStateOf(spell.isFavorite) }
                IconButton(
                    onClick = {
                        val newFavoriteStatus = !isFavorite.value
                        isFavorite.value = newFavoriteStatus
                        spell.isFavorite = newFavoriteStatus

                        if (newFavoriteStatus) {
                            functionsDB.addToFavorites(spell)
                        } else {
                            functionsDB.removeFromFavorites(spell.index)
                        }

                        appState.updateSpellFavoriteStatus(spell.index, newFavoriteStatus)
                    }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (isFavorite.value) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Remove from Favorites",
                                tint = Color.Yellow,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.StarOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            // Scrollable Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp)
            ) {
                item {
                    Text(
                        text = spell.name,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
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
                    )
                    Spacer(modifier = Modifier.height(12.dp))
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
                        )
                    }
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
                    )


                    Spacer(modifier = Modifier.height(12.dp))
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
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Damage information
                if (spell.damage != null) {
                    item {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                    append("Damage Type: ")
                                }
                                append(spell.damage.damageType?.name ?: "Unknown")
                            },
                            fontSize = 14.sp,
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (spell.damage.damageAtSlotLevel.isNotEmpty()) {
                        item {
                            // Title
                            Text(
                                text = "Damage by Slot Level",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val scrollState = rememberScrollState()

                                Row(
                                    modifier = Modifier
                                        .horizontalScroll(scrollState)
                                        .wrapContentWidth()
                                        .padding(6.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    spell.damage.damageAtSlotLevel.forEach { (lvl, damage) ->
                                        Column(
                                            modifier = Modifier.padding(horizontal = 4.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "Level $lvl",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = damage,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                            if (scrollState.maxValue > 0) {
                                                Spacer(modifier = Modifier.height(8.dp))
                                            }
                                        }
                                    }

                                }

                                if (scrollState.maxValue > 0) {
                                    CustomScrollbar(
                                        scrollState = scrollState,
                                        type = "horizontal",
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }

                    if (spell.damage.damageAtCharLevel.isNotEmpty()) {
                        item {
                            // Title
                            Text(
                                text = "Damage by Character Level",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val scrollState = rememberScrollState()

                                Row(
                                    modifier = Modifier
                                        .horizontalScroll(scrollState)
                                        .fillMaxWidth()
                                        .padding(6.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    spell.damage.damageAtCharLevel.forEach { (lvl, damage) ->
                                        Column(
                                            modifier = Modifier.padding(horizontal = 4.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "Level $lvl",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = damage,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                            if (scrollState.maxValue > 0) {
                                                Spacer(modifier = Modifier.height(12.dp))
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
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
                            thickness = 1.5.dp,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp, top = 8.dp)
                        )
                    }
                }

                // Spell Description (markdown)
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(515.dp)
                    ) {

                        val scrollState = rememberScrollState()


                        // Scrollable content
                        Column(
                            modifier = Modifier
                                .verticalScroll(state = scrollState)
                                .fillMaxSize()
                                .padding(end = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
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
                                                        when {
                                                            url.startsWith("navigate://conditions/") -> {
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
                                                                        selectedCondition =
                                                                            condition
                                                                        showConditionDialog = true
                                                                    }
                                                                }
                                                            }

                                                            url.startsWith("navigate://rules/") -> {
                                                                val ruleName = url
                                                                    .substringAfter("navigate://rules/")
                                                                    .replace("-", " ")
                                                                    .trim()

                                                                if (ruleName.isNotBlank()) {
                                                                    val rule =
                                                                        appState.getRuleByName(
                                                                            ruleName
                                                                        )
                                                                    if (rule != null) {
                                                                        selectedRule = rule
                                                                        showRuleDialog = true
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    },
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
                                                    when {
                                                        url.startsWith("navigate://conditions/") -> {
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

                                                        url.startsWith("navigate://rules/") -> {
                                                            val ruleName = url
                                                                .substringAfter("navigate://rules/")
                                                                .replace("-", " ")
                                                                .trim()

                                                            if (ruleName.isNotBlank()) {
                                                                val rule =
                                                                    appState.getRuleByName(ruleName)
                                                                if (rule != null) {
                                                                    selectedRule = rule
                                                                    showRuleDialog = true
                                                                }
                                                            }
                                                        }
                                                    }
                                                },
                                                linkColor = Color(0xFF2196F3),
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
                                                when {
                                                    url.startsWith("navigate://conditions/") -> {
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

                                                    url.startsWith("navigate://rules/") -> {
                                                        val ruleName = url
                                                            .substringAfter("navigate://rules/")
                                                            .replace("-", " ")
                                                            .trim()

                                                        if (ruleName.isNotBlank()) {
                                                            val rule =
                                                                appState.getRuleByName(ruleName)
                                                            if (rule != null) {
                                                                selectedRule = rule
                                                                showRuleDialog = true
                                                            }
                                                        }
                                                    }
                                                }
                                            },
                                            linkColor = Color(0xFF2196F3),
                                            enableUnderlineForLink = false,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                        CustomScrollbar(scrollState = scrollState, type = "vertical")
                    }


                    if (showConditionDialog) {
                        selectedCondition?.let { condition ->
                            EntityDialog(
                                entityName = condition.name ?: "Unknown Condition",
                                type = "Condition",
                                entityDescription = condition.description.orEmpty(),
                                onDismiss = { showConditionDialog = false }
                            )
                        }
                    }

                    // Render Rule Dialog
                    if (showRuleDialog) {
                        selectedRule?.let { rule ->
                            EntityDialog(
                                entityName = rule.name ?: "Unknown Rule",
                                type = "Rule",
                                entityDescription = rule.description?.let { listOf(it) }.orEmpty(),
                                onDismiss = { showRuleDialog = false }
                            )
                        }
                    } else {
                        // Fallback UI for null spell
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text("Spell details not found.")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EntityDialog(
    entityName: String,
    type: String,
    entityDescription: List<String>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "$type: $entityName",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )

                entityDescription.forEach { description ->
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Close")
                }
            }
        }
    }
}