package com.example.arcanavault.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Rule(
    @SerialName("index")
    val index: String? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("url")
    val url: String? = null,

    @SerialName("desc")
    val description: String? = null,

    @SerialName("subsections")
    val ruleSections: List<RuleSection>? = null
)