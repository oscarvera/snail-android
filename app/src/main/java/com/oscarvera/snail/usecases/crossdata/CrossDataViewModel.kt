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
import com.oscarvera.snail.provider.local.LocalRepository
import com.oscarvera.snail.usecases.learning.LearningViewModel
import com.oscarvera.snail.util.extensions.getProperId
import com.oscarvera.snail.util.sendErrorEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
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

            val localRepository = LocalRepository()
            localRepository.getAllDesksWithCards(object :
                DeskDataSource.LoadDesksWithCardsCallBack {
                override fun onDesksLoaded(desks: List<DeskWithCards>) {
                    listDesksLocal = desks
                    totalNumberOfDesks = desks.size
                    desks.forEach { deskWithCards ->
                        this@launch.launch {

                            localRepository.getCardsAndData(deskWithCards.getProperId())
                                .catch {
                                    sendErrorEvent(CrossDataViewModel::class.java.name, it.message)
                                }
                                .collect{
                                    it.data?.let { cards->
                                        var listCards = ArrayList<Card>()
                                        cards.forEach {
                                            val card = it.card
                                            card.cardswithdata = it.cardData ?: listOf()
                                            listCards.add(card)
                                        }
                                        deskWithCards.cards = listCards.toList()
                                        listDesksToExport.add(deskWithCards)
                                        numberOfDeskLoaded++
                                    }
                                }

                        }
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