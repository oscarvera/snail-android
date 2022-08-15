package com.oscarvera.snail.usecases.addcard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.provider.CardDataSource
import com.oscarvera.snail.provider.SwichDataSource
import com.oscarvera.snail.util.extensions.getStatusCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class AddCardViewModel : ViewModel() {


    private val _isCardAddedSuccessfully: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    private val _isCardAddedError: MutableLiveData<Throwable> by lazy {
        MutableLiveData<Throwable>()
    }

    val isCardAddedSuccessfully: LiveData<Boolean> get() = _isCardAddedSuccessfully
    val isCardAddedError: LiveData<Throwable> get() = _isCardAddedError

    //Not Used
    fun addCard(idDesk : String, card : Card) {
        viewModelScope.launch(Dispatchers.IO) {
            SwichDataSource.cardData.addCard(idDesk, card ,object : CardDataSource.SaveTaskCallback {
                override fun onSaveSuccess() {
                    _isCardAddedSuccessfully.postValue(true)
                }

                override fun onError(t: Throwable) {
                    _isCardAddedError.postValue(t)
                }

            })
        }
    }

}