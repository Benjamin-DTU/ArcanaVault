package com.example.arcanavault.DB

import com.example.arcanavault.model.data.Damage
import io.realm.kotlin.ext.query
import com.example.arcanavault.model.data.Spell as ApiSpell
import com.example.arcanavault.DB.Spell as RealmSpell
import com.example.arcanavault.model.data.ItemReference
import android.util.Log
import io.realm.kotlin.UpdatePolicy

class FunctionsDB {


    //Spells Functions
    private fun ApiSpell.toRealmModel(): RealmSpell {
        return RealmSpell().apply {
            index = this@toRealmModel.index
            name = this@toRealmModel.name
            level = this@toRealmModel.level
            url = this@toRealmModel.url
            imageUrl = this@toRealmModel.imageUrl
            isFavorite = this@toRealmModel.isFavorite
            description.addAll(this@toRealmModel.description)
            shortDescription = this@toRealmModel.shortDescription
            higherLevel.addAll(this@toRealmModel.higherLevel)
            range = this@toRealmModel.range
            components.addAll(this@toRealmModel.components)
            material = this@toRealmModel.material ?: ""
            ritual = this@toRealmModel.ritual
            duration = this@toRealmModel.duration
            concentration = this@toRealmModel.concentration
            castingTime = this@toRealmModel.castingTime
            attackType = this@toRealmModel.attackType ?: ""
            schoolName = this@toRealmModel.school.name ?: "Unknown"
            classes.addAll(this@toRealmModel.classes.map { it.name })
            subclasses.addAll(this@toRealmModel.subclasses.map { it.name })
            damageType =
                this@toRealmModel.damage?.damageType?.name ?: "Unknown"
            damageAtSlotLevel.addAll(
                this@toRealmModel.damage?.damageAtSlotLevel //(Chatgbt assisted in how to handel the "damage" itemreference when saving and fetching from the db)
                    ?.map { (level, value) -> "$level: $value" }
                    ?: emptyList()
            )
            damageAtCharLevel.addAll(
                this@toRealmModel.damage?.damageAtCharLevel
                    ?.map { (level, value) -> "$level: $value" }
                    ?: emptyList()
            )
            searchCombined = this@toRealmModel.searchCombined
        }
    }


    private fun RealmSpell.toApiSpell(): ApiSpell {
        return ApiSpell(
            index = this.index,
            name = this.name,
            level = this.level,
            url = this.url,
            imageUrl = this.imageUrl,
            description = this.description.toList(),
            shortDescription = this.shortDescription,
            higherLevel = this.higherLevel.toList(),
            range = this.range,
            components = this.components.toList(),
            material = this.material,
            ritual = this.ritual,
            duration = this.duration,
            concentration = this.concentration,
            castingTime = this.castingTime,
            attackType = this.attackType,
            school = ItemReference(
                index = this.schoolName,
                name = this.schoolName.ifEmpty { "Unknown" }),
            classes = this.classes.map { ItemReference(it, it) },
            subclasses = this.subclasses.map { ItemReference(it, it) },
            damage = Damage(
                damageType = ItemReference(index = this.damageType, name = this.damageType),
                damageAtSlotLevel = this.damageAtSlotLevel.associate {
                    val parts = it.split(": ")
                    parts[0] to parts.getOrElse(1) { "" }
                },
                damageAtCharLevel = this.damageAtCharLevel.associate {
                    val parts = it.split(": ")
                    parts[0] to parts.getOrElse(1) { "" }
                }
            ),
            searchCombined = this.searchCombined
        ).apply {
            isFavorite = this@toApiSpell.isFavorite
        }
    }


    fun addToFavorites(spell: ApiSpell) {
        val realmInstance = AppDB_Config.realm
        realmInstance.writeBlocking {
            val existingSpell = query<RealmSpell>("index == $0", spell.index).first().find()
            if (existingSpell != null) {
                existingSpell.isFavorite = true
                copyToRealm(existingSpell, updatePolicy = UpdatePolicy.ALL)
            } else {

                copyToRealm(spell.toRealmModel().apply { isFavorite = true })
            }
        }
    }

    fun saveAllSpells(spells: List<ApiSpell>) {
        val realmInstance = AppDB_Config.realm
        realmInstance.writeBlocking {
            spells.forEach { spell ->
                val existingSpell = query<RealmSpell>("index == $0", spell.index).first().find()
                val isFavorite = existingSpell?.isFavorite ?: false
                copyToRealm(
                    spell.toRealmModel().apply { this.isFavorite = isFavorite },
                    updatePolicy = UpdatePolicy.ALL
                )
            }
        }
    }


    fun getSpellsFromDB(): List<ApiSpell> {
        val realmInstance = AppDB_Config.realm
        val allSpells = realmInstance.query<RealmSpell>().find()

        //Log each spell retrieved from the database
        //Log.d("Database", "Retrieved ${allSpells.size} spells from the database:")
        //allSpells.forEach { spell ->
            //Log.d("Database", spell.toString())
        //}

        val apiSpells = allSpells.map { it.toApiSpell() }
        apiSpells.forEach { spell ->
        //Log.d("Database", spell.toString())
        }

        return apiSpells
    }

    fun removeFromFavorites(index: String) {
        val realmInstance = AppDB_Config.realm
        realmInstance.writeBlocking {
            val existingSpell = query<RealmSpell>("index == $0", index).first().find()
            if (existingSpell != null) {
                existingSpell.isFavorite = false
                copyToRealm(existingSpell)
            }
        }
    }


    fun getFavoriteSpells(): List<ApiSpell> {
        val realmInstance = AppDB_Config.realm
        val favoriteSpells = realmInstance.query<RealmSpell>("isFavorite == true").find()
        return favoriteSpells.map { it.toApiSpell() }
    }


//Conditions Functions

    private fun com.example.arcanavault.model.data.Condition.toRealmModel(): Condition {
        return Condition().apply {
            index = this@toRealmModel.index ?: ""
            name = this@toRealmModel.name ?: ""
            url = this@toRealmModel.url ?: ""
            description.addAll(this@toRealmModel.description ?: emptyList())
        }
    }

    private fun Condition.toApiModel(): com.example.arcanavault.model.data.Condition {
        return com.example.arcanavault.model.data.Condition(
            index = this.index,
            name = this.name,
            url = this.url,
            description = this.description.toList()
        )
    }

    fun saveAllConditions(conditions: List<com.example.arcanavault.model.data.Condition>) {
        val realmInstance = AppDB_Config.realm
        realmInstance.writeBlocking {
            conditions.forEach { condition ->
                val existingCondition =
                    query<Condition>("index == $0", condition.index ?: "").first().find()
                if (existingCondition == null) {
                    copyToRealm(condition.toRealmModel())
                }
            }
        }
    }

    fun getConditionsFromDB(): List<com.example.arcanavault.model.data.Condition> {
        val realmInstance = AppDB_Config.realm
        val allConditions = realmInstance.query<Condition>().find()
        return allConditions.map { it.toApiModel() }
    }


    //Rule Functions
    private fun com.example.arcanavault.model.data.Rule.toRealmModel(): Rule {
        return Rule().apply {
            index = this@toRealmModel.index ?: ""
            name = this@toRealmModel.name ?: ""
            url = this@toRealmModel.url ?: ""
            description = this@toRealmModel.description ?: ""
            ruleSections.addAll(this@toRealmModel.ruleSections?.map { it.toRealmModel() }
                ?: emptyList())
        }
    }

    private fun Rule.toApiModel(): com.example.arcanavault.model.data.Rule {
        return com.example.arcanavault.model.data.Rule(
            index = this.index,
            name = this.name,
            url = this.url,
            description = this.description,
            ruleSections = this.ruleSections.map { it.toApiModel() }
        )
    }

    fun saveAllRules(rules: List<com.example.arcanavault.model.data.Rule>) {
        val realmInstance = AppDB_Config.realm
        realmInstance.writeBlocking {
            rules.forEach { rule ->
                val existingRule = query<Rule>("index == $0", rule.index ?: "").first().find()
                if (existingRule == null) {
                    copyToRealm(rule.toRealmModel())
                }
            }
        }
    }

    fun getRulesFromDB(): List<com.example.arcanavault.model.data.Rule> {
        val realmInstance = AppDB_Config.realm
        val allRules = realmInstance.query<Rule>().find()
        return allRules.map { it.toApiModel() }
    }


    //Ruleset Functions
    private fun com.example.arcanavault.model.data.RuleSection.toRealmModel(): RuleSection {
        return RuleSection().apply {
            index = this@toRealmModel.index ?: ""
            name = this@toRealmModel.name ?: ""
            url = this@toRealmModel.url ?: ""
            description = this@toRealmModel.description ?: ""
        }
    }

    private fun RuleSection.toApiModel(): com.example.arcanavault.model.data.RuleSection {
        return com.example.arcanavault.model.data.RuleSection(
            index = this.index,
            name = this.name,
            url = this.url,
            description = this.description
        )
    }
}