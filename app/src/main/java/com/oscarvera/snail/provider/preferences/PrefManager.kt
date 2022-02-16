package com.oscarvera.snail.provider.preferences

import androidx.core.content.edit
import com.oscarvera.snail.MyApplication.Companion.userPreferences

object PrefManager {

    var userIdFirebase: String?
        get() = userPreferences.getString("KEY_USER_ID", null)
        set(value) = userPreferences.edit { putString("KEY_USER_ID", value) }

    var isInLocalMode: Boolean
        get() = userPreferences.getBoolean("KEY_IS_IN_LOCAL_MODE", false)
        set(value) = userPreferences.edit { putBoolean("KEY_IS_IN_LOCAL_MODE", value) }

    var maxQuantifierToBeLearned: Int
        get() = userPreferences.getInt("MAX_QUA_LEARNED", 100)
        set(value) = userPreferences.edit { putInt("MAX_QUA_LEARNED", value) }

    var userNameShare: String?
        get() = userPreferences.getString("KEY_USER_NAME_SHARE", null)
        set(value) = userPreferences.edit { putString("KEY_USER_NAME_SHARE", value) }


    fun clear() = userPreferences.edit { clear() }



}