package com.oscarvera.snail.usecases.learning

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscarvera.snail.model.domain.CardWithData
import com.oscarvera.snail.model.domain.StatusCard
import com.oscarvera.snail.provider.CardDataSource
import com.oscarvera.snail.provider.SwichDataSource
import com.oscarvera.snail.provider.preferences.PrefManager
import com.oscarvera.snail.usecases.deskdetail.DeskDetailViewModel
import com.oscarvera.snail.usecases.home.desks.DesksViewModel
import com.oscarvera.snail.util.Utils
import com.oscarvera.snail.util.customs.Result
import com.oscarvera.snail.util.extensions.getStatusCard
import com.oscarvera.snail.util.sendErrorEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class LearningViewModel : ViewModel() {


    private val _card: MutableLiveData<CardWithData> by lazy {
        MutableLiveData<CardWithData>()
    }
    private val _noMoreCards: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val card: LiveData<CardWithData> get() = _card
    val noMoreCards: LiveData<Boolean> get() = _noMoreCards


    private var cardsToLearn : ArrayList<CardWithData> = ArrayList()
    private var cardsRest : ArrayList<CardWithData> = ArrayList()
    private var indexCards : Int = 0
    private var idDesk : String = ""

    fun getCards(idDesk : String) {
        this.idDesk = idDesk
        viewModelScope.launch(Dispatchers.IO) {
            SwichDataSource.cardData.getCardsAndData(idDesk)
                .catch {
                    emit(Result.error(it.message ?: "", null))
                    sendErrorEvent(LearningViewModel::class.java.name, it.message)
                }.cancellable()
                .collect {
                    it.data?.let { listCards ->
                        val splitLists = listCards.partition {
                            it.card.getStatusCard() == StatusCard.LEARNING
                        }
                        cardsToLearn = ArrayList(splitLists.second)
                        cardsRest = ArrayList(splitLists.first)
                        _card.postValue(cardsToLearn[indexCards])
                        cancel()
                    }
                }
        }
    }

    fun setResultCard(result : Boolean){
            val currentCard = cardsToLearn[indexCards]
            currentCard.card.date_checked = Utils.getNowDateFormatted()
            if (result){ // User remember the card
                currentCard.card.quantifier = currentCard.card.quantifier*PrefManager.quantifierNumber
            }else{ //User don't remember the card
                if (currentCard.card.quantifier>1) currentCard.card.quantifier/2
            }
            currentCard.card.date_to_check = Utils.getDateWithQuantifierApplied(currentCard.card.quantifier)

            cardsToLearn[indexCards] = currentCard
            //Update Card
            viewModelScope.launch(Dispatchers.IO) {

                val allCard = cardsToLearn + cardsRest

                SwichDataSource.cardData.updateCard(idDesk , allCard, indexCards, object : CardDataSource.SaveTaskCallback {
                    override fun onSaveSuccess() {
                        nextCard()
                    }

                    override fun onError(t: Throwable) {
                        sendErrorEvent(LearningViewModel::class.java.name,t.message)
                    }
                })
            }


    }

    fun nextCard(){
            indexCards++
            if (cardsToLearn.size > indexCards){
                _card.postValue(cardsToLearn[indexCards])
            }else{
                // It's already leaned the cards
                _noMoreCards.postValue(true)
            }

    }

}