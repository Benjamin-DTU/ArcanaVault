package com.example.arcanavault.controller.api

import com.example.arcanavault.model.data.Condition
import com.example.arcanavault.model.data.ListResponse
import com.example.arcanavault.model.data.Rule
import com.example.arcanavault.model.data.Spell
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("spells")
    suspend fun getAllSpells(): List<Spell>

    @GET("rules")
    suspend fun getAllRules(): List<Rule>

    @GET("conditions")
    suspend fun getAllConditions(): List<Condition>

}
