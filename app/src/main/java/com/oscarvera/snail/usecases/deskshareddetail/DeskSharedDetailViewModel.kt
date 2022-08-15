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
import com.oscarvera.snail.usecases.deskdetail.DeskDetailViewModel
import com.oscarvera.snail.util.customs.Result
import com.oscarvera.snail.util.extensions.setResultEnclosed
import com.oscarvera.snail.util.sendErrorEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class DeskSharedDetailViewModel : ViewModel() {

    private val _cards: MutableLiveData<Result<List<Card>>> by lazy {
        MutableLiveData<Result<List<Card>>>()
    }

    private val _desk: MutableLiveData<Result<DeskShared>> by lazy {
        MutableLiveData<Result<DeskShared>>()
    }

    val cards: LiveData<Result<List<Card>>> get() = _cards

    val desk: LiveData<Result<DeskShared>> get() = _desk

    fun getSharedDesk(idRemoteDesk : String) {
        viewModelScope.launch(Dispatchers.IO) {
            SwichDataSource.deskData.getRemoteDesk(idRemoteDesk)
                .catch {
                    emit(Result.error(it.message ?: "", null))
                    sendErrorEvent(DeskSharedDetailViewModel::class.java.name, it.message)
                }
                .collect { result ->
                    result.data?.cards?.let {
                        val cardsResult = Result(result.status,it, null, null)
                        _cards.postValue(cardsResult)
                    }
                    _desk.postValue(result)
                }
        }
    }

}