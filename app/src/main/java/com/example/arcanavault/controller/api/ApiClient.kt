package com.example.arcanavault.controller.api

import com.example.arcanavault.model.data.Spell
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType

class ApiClient {

    companion object {
        const val BASE_URL = "https://www.dnd5eapi.co/api/"
        private const val CONTENT_TYPE = "application/json; charset=UTF-8"
    }

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(
            json.asConverterFactory(CONTENT_TYPE.toMediaType())
        )
        .baseUrl(BASE_URL)
        .build()

    private val apiService: ApiService = retrofit.create(ApiService::class.java)

    suspend fun getAllSpells(): List<Spell> = coroutineScope {
        val spellsList = apiService.getAllSpells().results
        spellsList.map { minimalSpell ->
            async { getSpellByIndex(minimalSpell.index) }
        }.map { it.await() }
    }

    suspend fun getAllSpellsCount(): Int {
        return apiService.getAllSpells().count;
    }

    suspend fun getSpellByIndex(index: String): Spell {
        return apiService.getSpellByIndex(index)
    }
}
