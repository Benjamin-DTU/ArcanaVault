package com.example.arcanavault

import kotlinx.coroutines.runBlocking
import com.example.arcanavault.controller.api.ApiClient

fun main() = runBlocking {
    val apiClient = ApiClient()

    try {
        // Fetch and print all spells
        val spells = apiClient.getAllSpells()
        spells.forEach { spell ->
            println("Spell name: ${spell.name}")
        }
        /*
        // Fetch and print fireball spell
        val spell = apiClient.getSpellByIndex("fireball");
        println("Spell name: ${spell.name}")
        */
    } catch (e: Exception) {
        println("Failed to fetch spells: ${e.message}")
    }
}
