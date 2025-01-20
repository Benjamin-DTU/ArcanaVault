package com.example.arcanavault.DB

import com.example.arcanavault.model.data.Damage
import com.example.arcanavault.model.data.Spell
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Spell : RealmObject {
    @PrimaryKey
    var index: String = ""
    var name: String = ""
    var level: Int = 0
    var url: String = ""
    var imageUrl: String = ""
    var isFavorite: Boolean = false
    var description: RealmList<String> = realmListOf()
    var shortDescription: String = ""
    var higherLevel: RealmList<String> = realmListOf()
    var range: String = ""
    var components: RealmList<String> = realmListOf()
    var material: String = ""
    var ritual: Boolean = false
    var duration: String = ""
    var concentration: Boolean = false
    var castingTime: String = ""
    var attackType: String = ""
    var damageType: String = ""
    var damageAtSlotLevel: RealmList<String> = realmListOf()
    var schoolName: String = ""
    var classes: RealmList<String> = realmListOf()
    var subclasses: RealmList<String> = realmListOf()
}

class Condition : RealmObject {
    var index: String = ""
    var name: String = ""
    var url: String = ""
    var description: RealmList<String> = realmListOf()
}

class Rule : RealmObject {
    var index: String = ""
    var name: String = ""
    var url: String = ""
    var description: String = ""
    var ruleSections: RealmList<RuleSection> = realmListOf() // List of RuleSection objects
}

class RuleSection : RealmObject {
    var index: String = ""
    var name: String = ""
    var url: String = ""
    var description: String = ""
}
