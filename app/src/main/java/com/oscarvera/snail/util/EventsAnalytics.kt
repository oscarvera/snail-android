package com.oscarvera.snail.util

import android.os.Bundle
import com.oscarvera.snail.MyApplication

/*
To send Event : SendEvent(EventType.*,null)
 */

enum class EventType(val nameEvent: String) {
    HOMEACTIVITY("HomeActivity"),
    SHOWDESKVIEW("ShowDeskView"),
    CLICKADDDESK("ClickAddDesk")
}


fun SendEvent(type: EventType, bundle: Bundle?) {
    MyApplication.firebaseAnalytics.logEvent(type.nameEvent, bundle)
}