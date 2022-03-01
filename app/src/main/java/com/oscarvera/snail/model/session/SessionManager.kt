package com.oscarvera.snail.model.session

import androidx.compose.material.ProvideTextStyle
import com.google.firebase.auth.FirebaseAuth
import com.oscarvera.snail.provider.preferences.PrefManager
import java.util.prefs.Preferences

class SessionManager {

    companion object {

        fun isLogged(): Boolean =
            PrefManager.userIdFirebase!=null

        fun signout() {
            PrefManager.userIdFirebase = null
            PrefManager.isInLocalMode = false
        }

        fun isLocalMode(): Boolean =
            PrefManager.isInLocalMode

        fun setLocalMode(localMode : Boolean){
            PrefManager.isInLocalMode = localMode
        }

        fun setIdFirebase(id : String){
            PrefManager.userIdFirebase = id
        }

        fun getIdFirebase(): String? = PrefManager.userIdFirebase

    }


}