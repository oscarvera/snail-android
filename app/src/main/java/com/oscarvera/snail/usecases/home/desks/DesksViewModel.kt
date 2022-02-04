package com.oscarvera.snail.usecases.home.desks

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscarvera.snail.model.domain.Desk
import com.oscarvera.snail.model.domain.DeskWithCards
import com.oscarvera.snail.provider.DataRepository
import com.oscarvera.snail.provider.DeskDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DesksViewModel : ViewModel() {

    private val _desksToCheck: MutableLiveData<List<DeskWithCards>> by lazy {
        MutableLiveData<List<DeskWithCards>>()
    }

    private val _desksChecked: MutableLiveData<List<Desk>> by lazy {
        MutableLiveData<List<Desk>>()
    }


    val desksChecked: LiveData<List<Desk>> get() = _desksChecked
    val desksToCheck: LiveData<List<DeskWithCards>> get() = _desksToCheck

    //Not Use
    fun getDesks() {

        viewModelScope.launch(Dispatchers.IO) {
            DataRepository().getDesks(object : DeskDataSource.LoadDesksCallBack {
                override fun onDesksLoaded(desks: List<Desk>) {
                    //_desks.postValue(desks)
                }

                override fun onError(t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    fun getDeskWithCards(){

        viewModelScope.launch(Dispatchers.IO) {
            DataRepository().getAllDesksWithCards(object : DeskDataSource.LoadDesksWithCardsCallBack {
                override fun onDesksLoaded(desks: List<DeskWithCards>) {
                    //_desksCards.postValue(desks)
                    deliverStatusDesks(desks)
                }

                override fun onError(t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }


    }


    fun deliverStatusDesks(deskscards: List<DeskWithCards>){

        //var mutableDeskCards = deskscards.toMutableList()

        var deskSplit = deskscards.partition { deskWithCards -> //Split two list. 1 to check and 2 already checked
            var cardsToCheck = deskWithCards.cards?.partition { card -> //Split two list. 1 outdated cards and 2 updated cards
               //Duration.between(Instant.parse(""), Instant.now()).toDays()>1
                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                val strDate: Date = formatter.parse(card.date_to_check)
                Date().after(strDate)
            }
            !cardsToCheck?.first.isNullOrEmpty() //If the first list (outdated cards) is empty, the desk is not to check


            /*cardsToCheck?.let { if (cardsToCheck.toList().isNotEmpty()){
                mutableDeskCards.remove(deskWithCards)
                true
            } else {false}
            } ?: false*/
        }

        Log.d("Test","Desk to Check: ${deskSplit.first.size} ")
        Log.d("Test","Desk Checked: ${deskSplit.second.size} ")
    }

}