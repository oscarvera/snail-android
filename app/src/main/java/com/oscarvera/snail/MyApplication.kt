package com.oscarvera.snail

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.firebase.analytics.FirebaseAnalytics
import com.oscarvera.snail.provider.local.DesksDatabase
import com.oscarvera.snail.util.Constants

class MyApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        userPreferences = this.getSharedPreferences(Constants.USER_PREFERENCES, Context.MODE_PRIVATE)
        localDatabase = Room.databaseBuilder(this, DesksDatabase::class.java,"database-snail").build()
    }

    companion object {
        lateinit var firebaseAnalytics: FirebaseAnalytics
            private set
        lateinit var userPreferences: SharedPreferences
            private set
        lateinit var localDatabase: DesksDatabase
            private set
    }
}