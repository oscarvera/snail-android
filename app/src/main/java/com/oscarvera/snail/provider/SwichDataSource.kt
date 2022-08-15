package com.oscarvera.snail.provider

import com.oscarvera.snail.model.session.SessionManager
import com.oscarvera.snail.provider.local.LocalRepository
import com.oscarvera.snail.provider.services.firebase.FirebaseRepository

class SwichDataSource {

    companion object{
        val deskData: DeskDataSource
            get() {
                return if (SessionManager.isLogged()) {
                    FirebaseRepository()
                }else {
                    LocalRepository()
                }
            }

        val cardData: CardDataSource
            get() {
                return if (SessionManager.isLogged()) {
                    FirebaseRepository()
                }else {
                    LocalRepository()
                }
            }
    }

}