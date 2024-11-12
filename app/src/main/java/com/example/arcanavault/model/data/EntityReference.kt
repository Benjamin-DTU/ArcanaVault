package com.example.arcanavault.model.data

import kotlinx.serialization.Serializable

@Serializable
data class EntityReference(
    val index: String = "",
    val name: String = "",
    val url: String = ""
)