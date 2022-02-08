package com.oscarvera.snail.usecases.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscarvera.snail.model.domain.Desk
import com.oscarvera.snail.provider.DeskDataSource
import com.oscarvera.snail.provider.SwichDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    fun addNewDesk(name: String, onSuccess: () -> Unit) {
        var desk = Desk()
        desk.name = name
        viewModelScope.launch(Dispatchers.IO) {
            SwichDataSource.deskData.addDesk(desk, object : DeskDataSource.SaveTaskCallback {
                override fun onSaveSuccess() {
                    onSuccess()
                }

                override fun onError(t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

}