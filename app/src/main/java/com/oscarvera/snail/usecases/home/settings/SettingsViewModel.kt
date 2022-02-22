package com.oscarvera.snail.usecases.home.settings

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

class SettingsViewModel : ViewModel() {


    fun deleteAllDesks(){
        viewModelScope.launch(Dispatchers.IO) {
            SwichDataSource.deskData.deleteAllDesks()
        }
    }

}