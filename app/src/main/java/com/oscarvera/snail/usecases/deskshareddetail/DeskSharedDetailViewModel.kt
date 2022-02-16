package com.oscarvera.snail.usecases.deskshareddetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.CardWithData
import com.oscarvera.snail.model.domain.Desk
import com.oscarvera.snail.model.domain.DeskShared
import com.oscarvera.snail.provider.CardDataSource
import com.oscarvera.snail.provider.DeskDataSource
import com.oscarvera.snail.provider.SwichDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeskSharedDetailViewModel : ViewModel() {

    private val _cards: MutableLiveData<List<Card>> by lazy {
        MutableLiveData<List<Card>>()
    }

    private val _desk: MutableLiveData<DeskShared> by lazy {
        MutableLiveData<DeskShared>()
    }

    val cards: LiveData<List<Card>> get() = _cards

    val desk: LiveData<DeskShared> get() = _desk

    fun getSharedDesk(idRemoteDesk : String) {

        viewModelScope.launch(Dispatchers.IO) {
            SwichDataSource.deskData.getRemoteDesk(idRemoteDesk, object : DeskDataSource.LoadSharedDeskCallBack {
                override fun onDeskLoaded(desk: DeskShared) {
                    _desk.postValue(desk)
                    _cards.postValue(desk.cards)
                }

                override fun onError(t: Throwable) {
                    //TODO("Not yet implemented")
                }

            })
        }
    }

}