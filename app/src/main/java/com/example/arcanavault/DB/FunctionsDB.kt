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
            schoolName = this@toRealmModel.school.name ?: ""
            classes.addAll(this@toRealmModel.classes.map { it.name })
            subclasses.addAll(this@toRealmModel.subclasses.map { it.name })
        }
    }


    fun addToFavorites(spell: ApiSpell) {
        val realmInstance = AppDB_Config.realm
        realmInstance.writeBlocking {
            val existingSpell = query<RealmSpell>("index == $0", spell.index).first().find()
            if (existingSpell != null) {
                return@writeBlocking
            }

            copyToRealm(spell.toRealmModel())
        }
    }


    fun removeFromFavorites(index: String) {
        val realmInstance = AppDB_Config.realm
        realmInstance.writeBlocking {
            val spellToDelete = query<RealmSpell>("index == $0", index).first().find()
            spellToDelete?.let { delete(it) }
        }
    }

    fun getFavoriteSpells(): List<ApiSpell> {
        val realmInstance = AppDB_Config.realm
        val favoriteSpells = realmInstance.query<RealmSpell>().find()
        return favoriteSpells.map { realmSpell ->
            ApiSpell(
                index = realmSpell.index,
                name = realmSpell.name,
                level = realmSpell.level,
                url = realmSpell.url,
                imageUrl = realmSpell.imageUrl,
                description = realmSpell.description.toList(),
                shortDescription = realmSpell.shortDescription,
                higherLevel = realmSpell.higherLevel.toList(),
                range = realmSpell.range,
                components = realmSpell.components.toList(),
                material = realmSpell.material,
                ritual = realmSpell.ritual,
                duration = realmSpell.duration,
                concentration = realmSpell.concentration,
                castingTime = realmSpell.castingTime,
                attackType = realmSpell.attackType,
                school = realmSpell.schoolName.let { ItemReference(it) },
                classes = realmSpell.classes.map { ItemReference(it) },
                subclasses = realmSpell.subclasses.map { ItemReference(it) }
            ).apply {
                // Explicitly set the mutable state property here
                isFavorite = realmSpell.isFavorite
            }
        }
    }

}
