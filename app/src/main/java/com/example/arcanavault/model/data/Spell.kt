package com.example.arcanavault.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Spell(
    override val index: String,
    override val name: String,
    override val level: Int,
    override val url: String,
    @SerialName("desc") val desc: List<String> = emptyList(),
    @SerialName("higher_level") val higherLevel: List<String> = emptyList(),
    @SerialName("range") val range: String = "",
    @SerialName("components") val components: List<String> = emptyList(),
    @SerialName("material") val material: String? = null,
    @SerialName("ritual") val ritual: Boolean = false,
    @SerialName("duration") val duration: String = "",
    @SerialName("concentration") val concentration: Boolean = false,
    @SerialName("casting_time") val castingTime: String = "",
    @SerialName("attack_type") val attackType: String? = null,
    @SerialName("damage") val damage: Damage? = null,
    @SerialName("school") val school: EntityReference? = null,
    @SerialName("classes") val classes: List<EntityReference> = emptyList(),
    @SerialName("subclasses") val subclasses: List<EntityReference> = emptyList()
) : IEntity {

    companion object {
        /**
         * Generates filter options based on the specified fields in the Spell class.
         *
         * @param spells List of Spell objects to generate filter options from.
         * @return A map of filter categories to a list of unique options for each category.
         */
        fun generateFilterOptions(spells: List<Spell>): Map<String, List<String>> {
            return mapOf(
                "Level" to spells.map { it.level.toString() }.distinct().sorted()
            )
        }
    }

}
