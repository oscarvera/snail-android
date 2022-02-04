package com.oscarvera.snail.provider.preferences

import androidx.core.content.edit
import com.oscarvera.snail.MyApplication.Companion.userPreferences

object PrefManager {

    var userId: String?
        get() = userPreferences.getString("KEY_USER_ID", null)
        set(value) = userPreferences.edit { putString("KEY_USER_ID", value) }


    fun clear() = userPreferences.edit { clear() }



}