package com.oscarvera.snail.usecases.home.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscarvera.snail.model.domain.Desk
import com.oscarvera.snail.model.domain.DeskShared
import com.oscarvera.snail.provider.DeskDataSource
import com.oscarvera.snail.provider.SwichDataSource
import com.oscarvera.snail.util.EventType
import com.oscarvera.snail.util.Utils
import com.oscarvera.snail.util.customs.Result
import com.oscarvera.snail.util.sendErrorEvent
import com.oscarvera.snail.util.sendEventWithDeskId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {

    private val _desksShared: MutableLiveData<Result<List<DeskShared>>> by lazy {
        MutableLiveData<Result<List<DeskShared>>>()
    }
    private val _isDesksShared: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val desksShared: LiveData<Result<List<DeskShared>>> get() = _desksShared
    val isDesksShared: LiveData<Boolean> get() = _isDesksShared


    fun getDeskShared() {
        viewModelScope.launch(Dispatchers.IO) {

            SwichDataSource.deskData.getAllDesksSharedWithCards()
                .catch {
                    emit(Result.error(it.message ?: "", null))
                    sendErrorEvent(SharedViewModel::class.java.name, it.message)
                }
                .collect {
                    _desksShared.postValue(it)
                }
        }

    }

    fun downloadDesk(deskShared: DeskShared) {

        sendEventWithDeskId(EventType.DOWNLOADDESK, deskShared.idRemote)
        val desk = Desk()
        desk.name = deskShared.name
        deskShared.cards?.let {
            it.forEach { card ->
                card.date_added = Utils.getNowDateFormatted()
            }
            desk.cards = it
        }

        viewModelScope.launch(Dispatchers.IO) {
            SwichDataSource.deskData.addDesk(desk, object : DeskDataSource.SaveTaskCallback {

                override fun onSaveSuccess(newId: String?) {
                    _isDesksShared.postValue(true)
                }

                override fun onError(t: Throwable) {
                    _isDesksShared.postValue(false)
                }
            })
            SwichDataSource.deskData.uploadNumDownloadShareDesk(deskShared)
        }

    }

}