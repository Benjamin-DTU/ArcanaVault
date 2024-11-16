package com.example.arcanavault.controller.api

import com.example.arcanavault.model.data.IEntity
import com.example.arcanavault.model.data.ListResponse
import com.example.arcanavault.model.data.Spell
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("spells")
    suspend fun getAllSpells(): ListResponse<Spell>

    @GET("spells/{index}")
    suspend fun getSpellByIndex(@Path("index") index: String): Spell
}
