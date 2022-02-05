package com.oscarvera.snail.util.extensions

import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.DeskWithCards
import com.oscarvera.snail.model.domain.StatusCard
import com.oscarvera.snail.model.session.SessionManager
import com.oscarvera.snail.provider.preferences.PrefManager
import com.oscarvera.snail.util.Utils

fun DeskWithCards.getProperId(): String {
    return if (SessionManager.isLogged()){
        this.desk.idRemote
    }else{
        this.desk.id.toString()
    }
}

fun Card.getStatusCard(): StatusCard {
    return when {
        this.date_checked.isEmpty() or Utils.dateIsAfter(date_to_check)-> {
            StatusCard.TO_LEARN
        }
        this.quantifier > PrefManager.maxQuantifierToBeLearned -> {
            StatusCard.LEARNED
        }
        else -> {
            StatusCard.LEARNING
        }
    }
}