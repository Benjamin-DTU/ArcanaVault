package com.example.arcanavault.DB

import android.app.Application
import android.util.Log
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class AppDB_Config : Application() {

    companion object {
        lateinit var realm: Realm
            private set
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("AppDB_Config", "Realm has been initialized")

        val realmConfig = RealmConfiguration.Builder(
            schema = setOf(Spell::class)
        )
            .deleteRealmIfMigrationNeeded()  // No argument is needed here
            .build()

        realm = Realm.open(realmConfig)
    }


    override fun onTerminate() {
        super.onTerminate()
        realm.close()
        Log.d("AppDB_Config", "Realm has been closed")
    }
}
