package com.oscarvera.snail.usecases.home.shared

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscarvera.snail.model.domain.Desk
import com.oscarvera.snail.model.domain.DeskShared
import com.oscarvera.snail.model.domain.DeskWithCards
import com.oscarvera.snail.model.domain.StatusCard
import com.oscarvera.snail.provider.DeskDataSource
import com.oscarvera.snail.provider.SwichDataSource
import com.oscarvera.snail.util.Utils
import com.oscarvera.snail.util.extensions.getStatusCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class SharedViewModel : ViewModel() {

    private val _desksShared: MutableLiveData<List<DeskShared>> by lazy {
        MutableLiveData<List<DeskShared>>()
    }
    private val _isDesksShared: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val desksShared: LiveData<List<DeskShared>> get() = _desksShared
    val isDesksShared: LiveData<Boolean> get() = _isDesksShared


    fun getDeskShared(){

        viewModelScope.launch(Dispatchers.IO) {
            SwichDataSource.deskData.getAllDesksSharedWithCards(object : DeskDataSource.LoadDesksSharedWithCardsCallBack {
                override fun onDesksLoaded(desks: List<DeskShared>) {
                    _desksShared.postValue(desks)
                }

                override fun onError(t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    fun downloadDesk(deskShared: DeskShared){

        val desk = Desk()
        desk.name = deskShared.name
        deskShared.cards?.let {
            it.forEach { card ->
                card.date_added = Utils.getNowDateFormatted()
            }
            desk.cards = it
        }

        viewModelScope.launch(Dispatchers.IO) {
            SwichDataSource.deskData.addDesk(desk,object : DeskDataSource.SaveTaskCallback {

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