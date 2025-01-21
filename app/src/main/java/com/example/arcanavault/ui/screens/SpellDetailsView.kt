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
import androidx.compose.material3.Surface
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
    var selectedCondition by remember { mutableStateOf<com.example.arcanavault.model.data.Condition?>(null) }
    var selectedRule by remember { mutableStateOf<Rule?>(null) }
    var isBackProcessing by remember { mutableStateOf(false) }


    if (isBackProcessing) {
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(300) // Adjust delay as needed
            isBackProcessing = false
        }
    }

    if (spell != null) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            item {
                // Back button and favorite button row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Back Button
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .clickable(enabled = !isBackProcessing) {
                                if (!isBackProcessing) {
                                    isBackProcessing = true // Disable further clicks
                                    onBackClick() // Trigger the back navigation
                                }
                            }
                    )

                    // Favorite Button
                    val isFavorite = remember { mutableStateOf(spell.isFavorite) }
                    IconButton(onClick = {

                        val newFavoriteStatus = !isFavorite.value
                        isFavorite.value = newFavoriteStatus
                        spell.isFavorite = newFavoriteStatus


                        if (newFavoriteStatus) {
                            functionsDB.addToFavorites(spell)
                        } else {
                            functionsDB.removeFromFavorites(spell.index)
                        }


                        appState.updateSpellFavoriteStatus(spell.index, newFavoriteStatus)
                    }) {
                        Box (contentAlignment = Alignment.Center){

                            if (isFavorite.value) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Remove from Favorites",
                                    tint = Color.Yellow,
                                    modifier = Modifier
                                        .size(24.dp)
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.StarOutline,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .size(28.dp)
                            )

                        }
                    }
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
                    Spacer(modifier = Modifier.width(8.dp))

                    val context = LocalContext.current
                    val imageId = context.resources.getIdentifier(
                        spell.school.name.lowercase(),
                        "drawable",
                        context.packageName
                    )

                    Image(
                        painter = painterResource(id = imageId),
                        contentDescription = "School name Image",
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(MaterialTheme.shapes.extraSmall)
                    )
                    /*AsyncImage(
                        model = spell.imageUrl,
                        contentDescription = "${spell.name} Image",
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(MaterialTheme.shapes.extraSmall)
                    )*/
                }


                Spacer(modifier = Modifier.height(12.dp))
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
                                    type ="horizontal",
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
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(505.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
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
                }
            }

        }


        // Render condition dialog
        if (showConditionDialog && selectedCondition != null) {
            selectedCondition?.description?.toList()?.let {
                EntityDialog(
                    entityName = selectedCondition?.name.toString(),
                    entityDescription = it,
                    type = "Condition",
                    onDismiss = { showConditionDialog = false }
                )
            }
        }

        // Render rule dialog
        if (showRuleDialog && selectedRule != null) {
            selectedRule?.description?.let {
                EntityDialog(
                    entityName = "",
                    type = "Rule",
                    entityDescription = listOf(it),
                    onDismiss = { showRuleDialog = false }
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
                    color = MaterialTheme.colorScheme.background,
                    shape = MaterialTheme.shapes.small
                )
                .padding(8.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = type,
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth()
                )
                // Title
                Text(
                    text = entityName,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                // Scrollable Content
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        entityDescription.forEach { description ->
                            MarkdownText(
                                markdown = description,
                                style = TextStyle(fontSize = 14.sp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }
                }

                // Dismiss Button
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


