package com.example.arcanavault.model.data

import kotlinx.serialization.Serializable

@Serializable
data class ListResponse<T : IEntity>(
    val count: Int,
    val results: List<T>
)
