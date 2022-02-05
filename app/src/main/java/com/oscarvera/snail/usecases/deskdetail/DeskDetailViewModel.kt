package com.oscarvera.snail.usecases.deskdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.CardWithData
import com.oscarvera.snail.model.domain.Desk
import com.oscarvera.snail.model.domain.DeskWithCards
import com.oscarvera.snail.provider.CardDataSource
import com.oscarvera.snail.provider.CardRepository
import com.oscarvera.snail.provider.DeskRepository
import com.oscarvera.snail.provider.DeskDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeskDetailViewModel : ViewModel() {

    private val _cards: MutableLiveData<List<CardWithData>> by lazy {
        MutableLiveData<List<CardWithData>>()
    }

    private val _desk: MutableLiveData<Desk> by lazy {
        MutableLiveData<Desk>()
    }

    val cards: LiveData<List<CardWithData>> get() = _cards

    val desk: LiveData<Desk> get() = _desk

    fun getDesk(idDesk : String) {

        viewModelScope.launch(Dispatchers.IO) {
            DeskRepository().getDesk(idDesk, object : DeskDataSource.LoadDeskCallBack {
                override fun onDeskLoaded(desk: Desk) {
                    _desk.postValue(desk)
                }

                override fun onError(t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    fun getCards(idDesk : String) {

        viewModelScope.launch(Dispatchers.IO) {
            CardRepository().getCardsAndData(idDesk, object : CardDataSource.LoadCardsAndDataCallBack {

                override fun onCardsLoaded(cards: List<CardWithData>) {
                    _cards.postValue(cards)
                }

                override fun onError(t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    fun addCard(idDesk : String, card: Card, onSuccess: () -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            CardRepository().addCard(idDesk,card = card, object : CardDataSource.SaveTaskCallback {
                override fun onSaveSuccess() {
                    onSuccess()
                }

                override fun onError(t: Throwable) {

                }


            })
        }
    }


}