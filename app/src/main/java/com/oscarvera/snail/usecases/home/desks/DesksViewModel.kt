package com.oscarvera.snail.usecases.home.desks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscarvera.snail.model.domain.Desk
import com.oscarvera.snail.model.domain.DeskWithCards
import com.oscarvera.snail.model.domain.StatusCard
import com.oscarvera.snail.provider.DeskDataSource
import com.oscarvera.snail.provider.SwichDataSource
import com.oscarvera.snail.util.extensions.getStatusCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class DesksViewModel : ViewModel() {

    private val _desksToCheck: MutableLiveData<List<DeskWithCards>> by lazy {
        MutableLiveData<List<DeskWithCards>>()
    }

    private val _desksChecked: MutableLiveData<List<DeskWithCards>> by lazy {
        MutableLiveData<List<DeskWithCards>>()
    }

    private val _noDesks: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }


    val desksChecked: LiveData<List<DeskWithCards>> get() = _desksChecked
    val desksToCheck: LiveData<List<DeskWithCards>> get() = _desksToCheck
    val noDesks: LiveData<Boolean> get() = _noDesks


    fun getDeskWithCards(){

        viewModelScope.launch(Dispatchers.IO) {
            SwichDataSource.deskData.getAllDesksWithCards(object : DeskDataSource.LoadDesksWithCardsCallBack {
                override fun onDesksLoaded(desks: List<DeskWithCards>) {
                    //_desksCards.postValue(desks)
                    if (desks.isEmpty()){
                        _noDesks.postValue(true)
                    }else{
                        splitStatusDesks(desks)
                        _noDesks.postValue(false)
                    }

                }

                override fun onError(t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }


    }


    fun splitStatusDesks(deskscards: List<DeskWithCards>){
        val deskSplit = deskscards.partition { deskWithCards -> //Split two list. 1 to check and 2 already checked
            val cardsToCheck = deskWithCards.cards?.partition { card -> //Split two list. 1 outdated cards and 2 updated cards
                card.getStatusCard() == StatusCard.TO_LEARN
            }
            !cardsToCheck?.first.isNullOrEmpty() //If the first list (outdated cards) is empty, the desk is not to check
        }
        _desksToCheck.postValue(deskSplit.first)
        _desksChecked.postValue(deskSplit.second)
    }

    fun addNewDesk(name: String, onSuccess: (newId : String?) -> Unit) {
        val desk = Desk()
        desk.name = name
        viewModelScope.launch(Dispatchers.IO) {
            SwichDataSource.deskData.addDesk(desk, object : DeskDataSource.SaveTaskCallback {
                override fun onSaveSuccess(newId: String?) {
                    onSuccess(newId)
                }

                override fun onError(t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

}