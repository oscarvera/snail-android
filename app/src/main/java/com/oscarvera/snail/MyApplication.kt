package com.oscarvera.snail

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import com.oscarvera.snail.provider.local.SnailDatabase
import com.oscarvera.snail.util.Constants

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {

        private lateinit var instance: MyApplication

        val firebaseDatabase: FirebaseFirestore by lazy {
            FirebaseFirestore.getInstance()
        }
        val firebaseAnalytics: FirebaseAnalytics by lazy {
            FirebaseAnalytics.getInstance(instance)
        }
        val userPreferences: SharedPreferences by lazy {
            instance.getSharedPreferences(Constants.USER_PREFERENCES, Context.MODE_PRIVATE)
        }
        val localDatabase: SnailDatabase by lazy {
            SnailDatabase.buildDatabase(instance)
        }
    }
}