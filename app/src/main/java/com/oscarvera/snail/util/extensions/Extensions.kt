package com.oscarvera.snail.util.extensions

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.oscarvera.snail.R
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

fun Card.getStatusName(context : Context): String{
    return when (this.getStatusCard()){
        StatusCard.LEARNED -> context.getString(R.string.detail_cards_learned)
        StatusCard.LEARNING -> context.getString(R.string.detail_cards_learning)
        StatusCard.TO_LEARN -> context.getString(R.string.detail_cards_tolearn)
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}