package com.example.arcanavault.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AreaOfEffect(
    @SerialName("size") val size: Int = 0,
    @SerialName("type") val type: String = ""
)