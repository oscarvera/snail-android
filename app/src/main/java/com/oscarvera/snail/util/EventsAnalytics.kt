package com.oscarvera.snail.util

import android.os.Bundle
import com.oscarvera.snail.MyApplication

/*
To send Event : sendEvent(EventType.*,null)
 */

enum class EventType(val nameEvent: String) {
    SHOWDESKSVIEW("ShowDesksView"),
    SHOWDESKVIEW("ShowDeskView"),
    CLICKADDDESK("ClickAddDesk"),
    LOGINLOCALMODE("LoginLocalMode"),
    LOGINONLINEMODE("LoginOnlineMode"),
    MIGRATESCREEN("MigrateScreen"),
    DOWNLOADDESK("DownloadDesk"),
    ERROR("ERROR"),
}


fun sendEvent(type: EventType, bundle: Bundle?) {
    MyApplication.firebaseAnalytics.logEvent(type.nameEvent, bundle)
}


fun sendEventWithDeskId(type: EventType, idDesk : String?) {
    val bundle = Bundle().apply {
        this.putString("idDesk", idDesk)
    }
    MyApplication.firebaseAnalytics.logEvent(type.nameEvent, bundle)
}

fun sendErrorEvent(classname : String,  descriptionError : String?) {
    val bundle = Bundle().apply {
        this.putString("className", classname)
        this.putString("descriptionError", descriptionError)
    }
    MyApplication.firebaseAnalytics.logEvent(EventType.ERROR.nameEvent, bundle)
}