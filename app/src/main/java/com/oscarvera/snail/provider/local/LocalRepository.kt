package com.oscarvera.snail.provider.local

import com.oscarvera.snail.MyApplication
import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.CardWithData
import com.oscarvera.snail.model.domain.Desk
import com.oscarvera.snail.model.domain.DeskShared
import com.oscarvera.snail.provider.CardDataSource
import com.oscarvera.snail.provider.DeskDataSource

class LocalRepository : DeskDataSource, CardDataSource {

    override fun getDesk(id: String, callBack: DeskDataSource.LoadDeskCallBack) {
        val desk = MyApplication.localDatabase.deskDao().getDesk(id.toInt())
        callBack.onDeskLoaded(desk)
    }

    override fun getRemoteDesk(idRemote: String, callBack: DeskDataSource.LoadSharedDeskCallBack) {
        //Not available
    }

    override fun getDesks(callBack: DeskDataSource.LoadDesksCallBack) {
        val desks = MyApplication.localDatabase.deskDao().getDesks()
        callBack.onDesksLoaded(desks = desks)
    }

    override fun addDesk(desk: Desk, callback: DeskDataSource.SaveTaskCallback) {
        val newId = MyApplication.localDatabase.deskDao().addDesk(desk = desk)
        callback.onSaveSuccess(newId.toString())
    }

    override fun addDeskShared(desk: DeskShared, callback: DeskDataSource.SaveTaskCallback) {
        //Not available
    }

    override fun getAllDesksWithCards(callback: DeskDataSource.LoadDesksWithCardsCallBack) {
        val desks = MyApplication.localDatabase.deskDao().getAllDeskswithCards()
        callback.onDesksLoaded(desks = desks)
    }

    override fun getAllDesksSharedWithCards(callback: DeskDataSource.LoadDesksSharedWithCardsCallBack) {
        //Not available
    }

    override fun getDeskandCards(idDesk: String, callBack: CardDataSource.LoadCardsCallBack) {
        val deskcards = MyApplication.localDatabase.cardDao().getCards(idDesk.toInt())
        callBack.onCardsLoaded(deskcards)
    }

    override fun getCardsAndData(
        idDesk: String,
        callback: CardDataSource.LoadCardsAndDataCallBack
    ) {
        val deskcards = MyApplication.localDatabase.cardDao().getCardsAndData(idDesk.toInt())
        callback.onCardsLoaded(deskcards)
    }

    override fun addCard(idDesk: String, card: Card, callback: CardDataSource.SaveTaskCallback) {
        card.deskId = idDesk.toInt()
        var idCard = MyApplication.localDatabase.cardDao().addCard(card)
        for (carddata in card.cardswithdata) {
            carddata.cardId = idCard.toInt()
            MyApplication.localDatabase.cardDao().addCardData(carddata)
        }
        callback.onSaveSuccess()
    }

    override fun updateCard(
        idDesk: String,
        listCards: List<CardWithData>,
        indexToUpload: Int,
        callback: CardDataSource.SaveTaskCallback
    ) {
        //card.deskId = idDesk.toInt()
        val card = listCards[indexToUpload].card
        MyApplication.localDatabase.cardDao().updateCard(card)
        callback.onSaveSuccess()
    }


}