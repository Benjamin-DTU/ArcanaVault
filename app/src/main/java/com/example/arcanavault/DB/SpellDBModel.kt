package com.example.arcanavault.DB

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
    var schoolName: String = ""
    var classes: RealmList<String> = realmListOf()
    var subclasses: RealmList<String> = realmListOf()
}
