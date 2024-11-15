package com.example.arcanavault.model.data

import kotlinx.serialization.Serializable

@Serializable
data class ItemReference(
    val index: String = "",
    val name: String = "",
    val url: String = ""
)