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
    @SerialName("school") val school: ItemReference? = null,
    @SerialName("classes") val classes: List<ItemReference> = emptyList(),
    @SerialName("subclasses") val subclasses: List<ItemReference> = emptyList()
) : IItem {

    companion object {
        /**
         * Generates filter options based on the specified fields in the Spell class.
         *
         * @param spells List of Spell objects to generate filter options from.
         * @return A map of filter categories to a list of unique options for each category.
         */
        fun generateFilterOptions(spells: List<Spell>): Map<String, List<String>> {
            return mapOf(
                "Classes" to spells.flatMap { it.classes.map { _class -> _class.name } }.distinct().sorted(),
                "Level" to spells.map { it.level.toString() }.distinct().sorted(),
                "Casting Time" to spells.map { it.castingTime.toString()}.distinct().sorted(),
                "School" to spells.mapNotNull { it.school?.name }.distinct().sorted(),
                "Damage Type" to spells.map { it.damage?.damageType?.name.toString()}.distinct().sorted(),
                "Components" to spells.flatMap { it.components }.distinct().sorted(),
                "Concentration" to spells.map { it.concentration.toString()}.distinct().sorted(),
                "Ritual" to spells.map { it.ritual.toString()}.distinct().sorted(),
            )
        }
    }
}
