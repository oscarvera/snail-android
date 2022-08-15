package com.oscarvera.snail.provider.local

import com.oscarvera.snail.MyApplication
import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.CardWithData
import com.oscarvera.snail.model.domain.Desk
import com.oscarvera.snail.model.domain.DeskShared
import com.oscarvera.snail.provider.CardDataSource
import com.oscarvera.snail.provider.DeskDataSource
import com.oscarvera.snail.util.customs.Result
import com.oscarvera.snail.util.extensions.setResultEnclosed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class LocalRepository : DeskDataSource, CardDataSource {

    override fun getDesk(id: String): Flow<Result<Desk>> = MyApplication.localDatabase.deskDao()
        .getDesk(id.toInt()).setResultEnclosed()

    override fun getRemoteDesk(idRemote: String): Flow<Result<DeskShared>> {
        //Not in local mode
        return flow {}
    }

    override fun getDesks(): Flow<Result<List<Desk>>> = MyApplication.localDatabase.deskDao().getDesks().setResultEnclosed()


    override suspend fun addDesk(desk: Desk, callback: DeskDataSource.SaveTaskCallback) {
        val newId = MyApplication.localDatabase.deskDao().addDesk(desk = desk)
        callback.onSaveSuccess(newId.toString())
    }

    override suspend fun deleteDesk(desk: Desk, callback: DeskDataSource.DeleteTaskCallback) {
        MyApplication.localDatabase.deskDao().delete(desk = desk)
        callback.onDeleteSuccess()
    }

    override fun addDeskShared(desk: DeskShared, callback: DeskDataSource.SaveTaskCallback) {
        //Not in local mode
    }

    override suspend fun getAllDesksWithCards(callback: DeskDataSource.LoadDesksWithCardsCallBack) {
        val desks = MyApplication.localDatabase.deskDao().getAllDeskswithCards()
        callback.onDesksLoaded(desks = desks)
    }

    override fun getAllDesksSharedWithCards(): Flow<Result<List<DeskShared>>> {
        //Not in local mode
        return flow {  }
    }

    override fun uploadNumDownloadShareDesk(desk: DeskShared) {
        //Not in local mode
    }

    override fun deleteAllDesks() {
        MyApplication.localDatabase.clearAllTables()
    }

    override suspend fun getDeskandCards(idDesk: String, callBack: CardDataSource.LoadCardsCallBack) {
        val deskcards = MyApplication.localDatabase.cardDao().getCards(idDesk.toInt())
        callBack.onCardsLoaded(deskcards)
    }

    override suspend fun getCardsAndData(
        idDesk: String
    ): Flow<Result<List<CardWithData>>> = MyApplication.localDatabase.cardDao().getCardsAndData(idDesk.toInt()).map {
        Result(Result.Status.SUCCESS, it, null, null)
    }


    override suspend fun addCard(idDesk: String, card: Card, callback: CardDataSource.SaveTaskCallback) {
        card.deskId = idDesk.toInt()
        var idCard = MyApplication.localDatabase.cardDao().addCard(card)
        card.cardswithdata.forEach {
            it.cardId = idCard.toInt()
            MyApplication.localDatabase.cardDao().addCardData(it)
        }
        callback.onSaveSuccess()
    }

    override suspend fun updateCard(
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