package com.oscarvera.snail.provider

import com.oscarvera.snail.model.session.SessionManager
import com.oscarvera.snail.provider.local.LocalRepository
import com.oscarvera.snail.provider.services.firebase.FirebaseRepository

class SwichDataSource(
    private val firebaseRepository: FirebaseRepository,
    private val localRepository: LocalRepository,
    private val sessionManager: SessionManager
) {
    fun deskData(): DeskDataSource = if(sessionManager.isLogged()) firebaseRepository else localRepository

    fun cardData() : CardDataSource = if(sessionManager.isLogged()) firebaseRepository else localRepository
    
}