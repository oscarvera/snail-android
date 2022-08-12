package com.oscarvera.snail.provider

import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.CardWithData
import com.oscarvera.snail.model.domain.Desk
import com.oscarvera.snail.model.domain.DeskWithCards
import com.oscarvera.snail.util.customs.Result
import kotlinx.coroutines.flow.Flow

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

    suspend fun getDeskandCards(idDesk : String, callBack: LoadCardsCallBack)
    suspend fun getCardsAndData(idDesk: String) : Flow<Result<List<CardWithData>>>
    suspend fun addCard(idDesk: String, card: Card, callback: SaveTaskCallback)
    suspend fun updateCard(idDesk: String, listCards: List<CardWithData>, indexToUpload : Int, callback: SaveTaskCallback)


}