package com.oscarvera.snail.usecases.deskdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.CardWithData
import com.oscarvera.snail.model.domain.Desk
import com.oscarvera.snail.provider.CardDataSource
import com.oscarvera.snail.provider.DeskDataSource
import com.oscarvera.snail.provider.SwichDataSource
import com.oscarvera.snail.util.customs.Result
import com.oscarvera.snail.util.sendErrorEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class DeskDetailViewModel : ViewModel() {

    private val _cards: MutableLiveData<Result<List<CardWithData>>> by lazy {
        MutableLiveData<Result<List<CardWithData>>>()
    }

    private val _desk: MutableLiveData<Result<Desk>> by lazy {
        MutableLiveData<Result<Desk>>()
    }

    val cards: LiveData<Result<List<CardWithData>>> get() = _cards

    val desk: LiveData<Result<Desk>> get() = _desk

    fun getDesk(idDesk: String) {

        viewModelScope.launch(Dispatchers.IO) {
            SwichDataSource.deskData.getDesk(idDesk)
                .catch {
                    emit(Result.error(it.message ?: "", null))
                    sendErrorEvent(DeskDetailViewModel::class.java.name, it.message)
                }
                .collect {
                    _desk.postValue(it)
                }
        }
    }

    fun getCards(idDesk: String) {
        viewModelScope.launch(Dispatchers.IO) {
            SwichDataSource.cardData.getCardsAndData(idDesk)
                .catch {
                    emit(Result.error(it.message ?: "", null))
                    sendErrorEvent(DeskDetailViewModel::class.java.name, it.message)
                }
                .collect {
                    _cards.postValue(it)
                }
        }
    }

    fun addCard(idDesk: String, card: Card, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            SwichDataSource.cardData.addCard(idDesk, card = card, object : CardDataSource.SaveTaskCallback {
                override fun onSaveSuccess() {
                    onSuccess()
                }

                override fun onError(t: Throwable) {
                    sendErrorEvent(DeskDetailViewModel::class.java.name, t.message)
                }


            })
        }
    }

    fun deleteDesk(onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            desk.value?.data?.let {
                SwichDataSource.deskData.deleteDesk(it, object : DeskDataSource.DeleteTaskCallback {

                    override fun onDeleteSuccess() {
                        onSuccess()
                    }

                    override fun onError(t: Throwable) {
                        sendErrorEvent(DeskDetailViewModel::class.java.name, t.message)
                    }


                })
            }

        }
    }


}