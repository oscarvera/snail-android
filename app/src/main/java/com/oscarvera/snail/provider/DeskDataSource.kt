package com.oscarvera.snail.provider

import com.oscarvera.snail.model.domain.Desk
import com.oscarvera.snail.model.domain.DeskShared
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
    interface LoadSharedDeskCallBack{

        fun onDeskLoaded(desk: DeskShared)
        fun onError(t: Throwable)

    }

    interface LoadDesksWithCardsCallBack{

        fun onDesksLoaded(desks: List<DeskWithCards>)
        fun onError(t: Throwable)

    }

    interface LoadDesksSharedWithCardsCallBack{

        fun onDesksLoaded(desks: List<DeskShared>)
        fun onError(t: Throwable)

    }

    interface SaveTaskCallback {
        fun onSaveSuccess(newId : String?)
        fun onError(t: Throwable)
    }

    interface DeleteTaskCallback {
        fun onDeleteSuccess()
        fun onError(t: Throwable)
    }

    fun getDesk(id: String, callBack: LoadDeskCallBack )
    fun getRemoteDesk(idRemote: String, callBack: LoadSharedDeskCallBack )
    fun getDesks(callBack: LoadDesksCallBack)
    fun addDesk(desk : Desk, callback: SaveTaskCallback)
    fun deleteDesk(desk : Desk, callback: DeleteTaskCallback)
    fun addDeskShared(desk : DeskShared, callback: SaveTaskCallback)
    fun getAllDesksWithCards(callback: LoadDesksWithCardsCallBack)
    fun getAllDesksSharedWithCards(callback: LoadDesksSharedWithCardsCallBack)
    fun uploadNumDownloadShareDesk(desk : DeskShared)
    fun deleteAllDesks()


}