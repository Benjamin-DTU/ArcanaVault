package com.example.arcanavault.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Damage(
    @SerialName("damage_type")val damageType: EntityReference? = null,
    @SerialName("damage_at_slot_level")val damageAtSlotLevel: Map<String, String> = emptyMap()
)