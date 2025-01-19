package com.example.arcanavault.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Condition(
    @SerialName("index")
    val index: String? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("url")
    val url: String? = null,

    @SerialName("desc")
    val description: List<String>? = null
)
