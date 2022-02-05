package com.oscarvera.snail.provider

import com.oscarvera.snail.model.domain.Desk
import com.oscarvera.snail.model.domain.DeskWithCards

interface DeskDataSource {

    interface LoadDesksCallBack{

        fun onDesksLoaded(desks: List<Desk>)
        fun onError(t: Throwable)

    }
    interface LoadDeskCallBack{

        fun onDeskLoaded(desk: Desk)
        fun onError(t: Throwable)

    }

    interface LoadDesksWithCardsCallBack{

        fun onDesksLoaded(desks: List<DeskWithCards>)
        fun onError(t: Throwable)

    }

    interface SaveTaskCallback {
        fun onSaveSuccess()
        fun onError(t: Throwable)
    }

    fun getDesk(id: String, callBack: LoadDeskCallBack )
    fun getDesks(callBack: LoadDesksCallBack)
    fun addDesk(desk : Desk, callback: SaveTaskCallback)
    fun getAllDesksWithCards(callback: LoadDesksWithCardsCallBack)


}