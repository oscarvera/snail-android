package com.oscarvera.snail.provider

import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.CardWithData
import com.oscarvera.snail.model.domain.Desk
import com.oscarvera.snail.model.domain.DeskWithCards

interface CardDataSource {

    interface LoadCardsCallBack{

        fun onCardsLoaded(cards: DeskWithCards)
        fun onError(t: Throwable)

    }

    interface LoadCardsAndDataCallBack{

        fun onCardsLoaded(cards: List<CardWithData>)
        fun onError(t: Throwable)

    }

    interface SaveTaskCallback {
        fun onSaveSuccess()
        fun onError(t: Throwable)
    }

    fun getDeskandCards(idDesk : String, callBack: LoadCardsCallBack)
    fun getCardsAndData(idDesk: String, callback : LoadCardsAndDataCallBack)
    fun addCard(idDesk: String, card: Card, callback: SaveTaskCallback)


}