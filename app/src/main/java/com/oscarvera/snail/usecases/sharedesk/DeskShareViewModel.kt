package com.oscarvera.snail.usecases.sharedesk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscarvera.snail.model.domain.DeskShared
import com.oscarvera.snail.model.domain.DeskWithCards
import com.oscarvera.snail.provider.CardDataSource
import com.oscarvera.snail.provider.DeskDataSource
import com.oscarvera.snail.provider.SwichDataSource
import com.oscarvera.snail.provider.preferences.PrefManager
import com.oscarvera.snail.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeskShareViewModel : ViewModel() {

    private val _desks: MutableLiveData<List<DeskWithCards>> by lazy {
        MutableLiveData<List<DeskWithCards>>()
    }
    private val _desksShared: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }


    val desks: LiveData<List<DeskWithCards>> get() = _desks
    val desksShared: LiveData<Boolean> get() = _desksShared


    fun getDeskWithCards() {

        viewModelScope.launch(Dispatchers.IO) {
            SwichDataSource.deskData.getAllDesksWithCards(object :
                DeskDataSource.LoadDesksWithCardsCallBack {
                override fun onDesksLoaded(desks: List<DeskWithCards>) {
                    _desks.postValue(desks)
                }

                override fun onError(t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }


    }

    fun shareDesk(desk : DeskWithCards, username : String){

        val deskToShare = DeskShared()
        deskToShare.cards = desk.cards
        deskToShare.name = desk.desk.name
        deskToShare.uploaded = Utils.getNowDateFormatted()
        deskToShare.userName = username

        deskToShare.cards?.let {
            it.forEach { card ->
                card.date_to_check = ""
                card.date_checked = ""
                card.date_added = ""
                card.quantifier = 1

            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            SwichDataSource.deskData.addDeskShared(deskToShare, object : DeskDataSource.SaveTaskCallback {
                override fun onSaveSuccess(newId: String?) {
                    _desksShared.postValue(true)
                }

                override fun onError(t: Throwable) {
                    _desksShared.postValue(false)
                }
            })
        }


    }

}