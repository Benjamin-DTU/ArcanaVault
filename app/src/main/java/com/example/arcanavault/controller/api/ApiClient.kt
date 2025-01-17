package com.example.arcanavault.controller.api

import com.example.arcanavault.model.data.Spell
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType
import android.util.Log
import com.example.arcanavault.model.data.Condition
import com.example.arcanavault.model.data.Rule

class ApiClient {

    private var spells: List<Spell> = emptyList()
    private var rules: List<Rule> = emptyList()
    private var conditions: List<Condition> = emptyList()

    companion object {
        const val BASE_URL = "https://arcanavaultapi.azurewebsites.net/api/"
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
        if (spells.isEmpty()) {
            spells = apiService.getAllSpells()
            Log.d("API_SPELLS_COUNT", "Fetched ${spells.size} spells from API")
        }
        spells
    }

    suspend fun getAllRules(): List<Rule> = coroutineScope {
        if (rules.isEmpty()){
            rules = apiService.getAllRules()
        }
        rules
    }

    suspend fun getAllConditions(): List<Condition> = coroutineScope {
        if (conditions.isEmpty()){
            conditions = apiService.getAllConditions()
        }
        conditions
    }

}
