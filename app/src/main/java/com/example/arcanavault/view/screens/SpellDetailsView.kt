package com.example.arcanavault.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arcanavault.controller.api.ApiClient
import com.example.arcanavault.model.data.AreaOfEffect
import com.example.arcanavault.model.data.Damage
import com.example.arcanavault.model.data.ItemReference
import com.example.arcanavault.model.data.Spell

@Composable
fun SpellDetailsView(
    spell: Spell?,
    modifier: Modifier = Modifier
) {
    if (spell != null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = spell.name,
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Level: ${spell.level}",
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "School: ${spell.school?.name ?: "Unknown"}",
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Casting Time: ${spell.castingTime}",
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Range: ${spell.range}",
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Description:",
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
            )
            spell.description.forEach { line ->
                Text(
                    text = line,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Check for Damage Information
            if (spell.damage != null) {
                Text(
                    text = "Damage Type: ${spell.damage.damageType?.name ?: "Unknown"}",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Display Damage by Slot Level if available
                if (spell.damage.damageAtSlotLevel.isNotEmpty()) {
                    Text(
                        text = "Damage by Slot Level:",
                        style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
                    )
                    spell.damage.damageAtSlotLevel.forEach { (lvl, damage) ->
                        Text(
                            text = "Slot Level $lvl: $damage",
                            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Display Damage by Character Level if available
                if (spell.damage.damageAtCharLevel.isNotEmpty()) {
                    Text(
                        text = "Damage by Character Level:",
                        style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
                    )
                    spell.damage.damageAtCharLevel.forEach { (charLvl, damage) ->
                        Text(
                            text = "Character Level $charLvl: $damage",
                            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                        )
                    }
                }
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
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
fun SpellDetailsPreview(){
    SpellDetailsView(spell = previewSpell)
}

val previewSpell = Spell(
    index = "fireball",
    name = "Fireball",
    level = 3,
    url = "https://example.com/fireball",
    description = listOf("A bright streak flashes from your pointing finger to a point you choose."),
    shortDescription = "A bright streak flashes...",
    higherLevel = listOf("When cast at higher levels, the damage increases."),
    range = "150 feet",
    components = listOf("V", "S", "M"),
    material = "A tiny ball of bat guano and sulfur.",
    areaOfEffect = AreaOfEffect(
        size = 20,
        type = "sphere"
    ),
    ritual = false,
    duration = "Instantaneous",
    concentration = false,
    castingTime = "1 action",
    attackType = "ranged",
    damage = Damage(
        damageType = ItemReference(
            index = "fire",
            name = "Fire",
            url = "/api/damage-types/fire"
        ),
        damageAtSlotLevel = mapOf(
            "3" to "8d6",
            "4" to "9d6",
            "5" to "10d6"
        ),
        damageAtCharLevel = mapOf(
            "1" to "2d6",
            "5" to "4d6",
            "10" to "6d6"
        )
    ),
    school = ItemReference(
        index = "evocation",
        name = "Evocation",
        url = "/api/magic-schools/evocation"
    ),
    classes = listOf(
        ItemReference(
            index = "wizard",
            name = "Wizard",
            url = "/api/classes/wizard"
        )
    ),
    subclasses = listOf(
        ItemReference(
            index = "lore",
            name = "Lore",
            url = "/api/subclasses/lore"
        )
    )
)
