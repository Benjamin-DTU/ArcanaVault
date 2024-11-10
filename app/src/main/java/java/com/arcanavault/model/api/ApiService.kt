package java.com.arcanavault.model.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.com.arcanavault.model.data.Spell

interface ApiService {

    @GET("spells/")
    fun getSpellByName(@Query("name") name: String): Call<Spell>
}
