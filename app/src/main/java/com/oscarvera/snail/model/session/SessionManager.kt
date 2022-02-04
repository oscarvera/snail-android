package com.oscarvera.snail.model.session

import com.google.firebase.auth.FirebaseAuth
import com.oscarvera.snail.provider.preferences.PrefManager
import java.util.prefs.Preferences

class SessionManager {

    companion object {

        fun isLogged(): Boolean =
            PrefManager.userId!=null
    }


}