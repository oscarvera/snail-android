package com.oscarvera.snail.provider

import com.oscarvera.snail.model.domain.Desk
import com.oscarvera.snail.model.domain.DeskShared
import com.oscarvera.snail.model.domain.DeskWithCards
import com.oscarvera.snail.util.customs.Result
import kotlinx.coroutines.flow.Flow

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

    fun getDesk(id: String) : Flow<Result<Desk>>
    fun getRemoteDesk(idRemote: String) : Flow<Result<DeskShared>>
    fun getDesks() : Flow<Result<List<Desk>>>
    suspend fun addDesk(desk : Desk, callback: SaveTaskCallback)
    suspend fun deleteDesk(desk : Desk, callback: DeleteTaskCallback)
    fun addDeskShared(desk : DeskShared, callback: SaveTaskCallback)
    suspend fun getAllDesksWithCards(callback: LoadDesksWithCardsCallBack)
    fun getAllDesksSharedWithCards() : Flow<Result<List<DeskShared>>>
    fun uploadNumDownloadShareDesk(desk : DeskShared)
    fun deleteAllDesks()


}