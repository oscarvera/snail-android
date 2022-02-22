package com.oscarvera.snail.usecases.crossdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.CardWithData
import com.oscarvera.snail.model.domain.DeskWithCards
import com.oscarvera.snail.provider.CardDataSource
import com.oscarvera.snail.provider.DeskDataSource
import com.oscarvera.snail.provider.SwichDataSource
import com.oscarvera.snail.util.extensions.getProperId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CrossDataViewModel : ViewModel() {

    private val _doSignIn: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    private val _migrationFinished: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }


    val doSignIn: LiveData<Boolean> get() = _doSignIn
    val migrationFinished: LiveData<Boolean> get() = _migrationFinished

    var listDesksLocal: List<DeskWithCards> = listOf()
    var listDesksToExport: ArrayList<DeskWithCards> = ArrayList()

    var totalNumberOfDesks: Int = 0
    var numberOfDeskLoaded: Int = 0
        set(number) {
            field = number
            if (field == totalNumberOfDesks) {
                //Terminada descarga
                _doSignIn.postValue(true)
            }
        }


    fun getLocalDesks() {

        viewModelScope.launch(Dispatchers.IO) {
            SwichDataSource.deskData.getAllDesksWithCards(object :
                DeskDataSource.LoadDesksWithCardsCallBack {
                override fun onDesksLoaded(desks: List<DeskWithCards>) {
                    listDesksLocal = desks
                    totalNumberOfDesks = desks.size

                    desks.forEach {
                        SwichDataSource.cardData.getCardsAndData(it.getProperId(), object :
                            CardDataSource.LoadCardsAndDataCallBack {
                            override fun onCardsLoaded(cards: List<CardWithData>) {
                                var listCards = ArrayList<Card>()
                                cards.forEach {
                                    val card = it.card
                                    card.cardswithdata = it.cardData ?: listOf()
                                    listCards.add(card)
                                }
                                it.cards = listCards.toList()
                                listDesksToExport.add(it)
                                numberOfDeskLoaded++
                            }

                            override fun onError(t: Throwable) {

                            }

                        })
                    }

                }

                override fun onError(t: Throwable) {
                }
            })
        }

    }

    fun setLocaltoRemote() {

        val numDesks = listDesksToExport.size

        listDesksLocal.forEach {

            val desk = it.desk
            it.cards?.let { cards ->
                desk.cards = cards
            }
            viewModelScope.launch(Dispatchers.IO) {
                SwichDataSource.deskData.addDesk(desk, object : DeskDataSource.SaveTaskCallback {

                    override fun onSaveSuccess(newId: String?) {
                        numDesks - 1
                        if (numDesks == 0) {
                            _migrationFinished.postValue(true)
                        }
                    }

                    override fun onError(t: Throwable) {
                    }
                })
            }
        }


    }

}