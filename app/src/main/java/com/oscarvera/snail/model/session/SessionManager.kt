package com.oscarvera.snail.model.session

import androidx.compose.material.ProvideTextStyle
import com.google.firebase.auth.FirebaseAuth
import com.oscarvera.snail.provider.preferences.PrefManager
import java.util.prefs.Preferences

class SessionManager {

    companion object {

        fun isLogged(): Boolean =
            PrefManager.userIdFirebase!=null

        fun isLocalMode(): Boolean =
            PrefManager.isInLocalMode

        fun setAsLocalMode(){
            PrefManager.isInLocalMode = true
        }

        fun setIdFirebase(id : String){
            PrefManager.userIdFirebase = id
        }

        fun getIdFirebase(): String? = PrefManager.userIdFirebase

    }


}