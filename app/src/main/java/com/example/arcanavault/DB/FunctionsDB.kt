package com.example.arcanavault.DB

import io.realm.kotlin.ext.query
import com.example.arcanavault.model.data.Spell as ApiSpell
import com.example.arcanavault.DB.Spell as RealmSpell
import com.example.arcanavault.model.data.ItemReference

class FunctionsDB {

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
            school = ItemReference(index = this.schoolName, name = this.schoolName.ifEmpty { "Unknown" }),
            classes = this.classes.map { ItemReference(it, it) },
            subclasses = this.subclasses.map { ItemReference(it, it) },
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
                copyToRealm(existingSpell)
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
                if (existingSpell == null) {
                    copyToRealm(spell.toRealmModel())
                }
            }
        }
    }


    fun getSpellsFromDB(): List<ApiSpell> {
        val realmInstance = AppDB_Config.realm
        val allSpells = realmInstance.query<RealmSpell>().find()
        return allSpells.map { it.toApiSpell() }
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
}
