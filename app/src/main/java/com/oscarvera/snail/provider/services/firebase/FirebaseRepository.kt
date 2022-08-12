package com.oscarvera.snail.provider.services.firebase

import com.google.firebase.firestore.FieldValue
import com.oscarvera.snail.MyApplication
import com.oscarvera.snail.model.domain.*
import com.oscarvera.snail.model.session.SessionManager
import com.oscarvera.snail.provider.CardDataSource
import com.oscarvera.snail.provider.DeskDataSource
import com.oscarvera.snail.util.customs.Result
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseRepository : DeskDataSource, CardDataSource {

    override fun getDesk(id: String): Flow<Result<Desk>> {
        return callbackFlow {
            trySend(Result.loading())

            val document = MyApplication.firebaseDatabase
                .collection("users")
                .document(SessionManager.getIdFirebase())
                .collection("desks")
                .document(id)

            val subscription = document.addSnapshotListener { snapshot, _ ->
                if (snapshot!!.exists()) {
                    val desk: Desk? = snapshot.toObject(Desk::class.java)
                    desk?.idRemote = document.id
                    trySend(Result.success(desk))
                }
            }
            awaitClose { subscription.remove() }
        }
    }

    override fun getRemoteDesk(idRemote: String): Flow<Result<DeskShared>> {

        return callbackFlow {
            trySend(Result.loading())

            val document = MyApplication.firebaseDatabase
                .collection("shared").document(idRemote)

            val subscription = document.addSnapshotListener { snapshot, _ ->
                if (snapshot!!.exists()) {
                    val deskShared = snapshot.toObject(DeskShared::class.java)
                    trySend(Result.success(deskShared))
                }
            }
            awaitClose { subscription.remove() }
        }
    }

    override fun getDesks(): Flow<Result<List<Desk>>> {

        return callbackFlow {
            trySend(Result.loading())

            val document = MyApplication.firebaseDatabase
                .document(SessionManager.getIdFirebase()).collection("desks")

            val subscription = document.addSnapshotListener { snapshot, _ ->
                snapshot?.let {
                    val list: ArrayList<Desk> = ArrayList()
                    for (documentDesk in snapshot.documents) {
                        val desk = documentDesk.toObject(Desk::class.java)
                        desk?.idRemote = documentDesk.id
                        desk?.let {
                            list.add(desk)
                        }
                    }
                    trySend(Result.success(list))
                }
            }
            awaitClose { subscription.remove() }
        }
    }

    override suspend fun addDesk(desk: Desk, callback: DeskDataSource.SaveTaskCallback) {
        MyApplication.firebaseDatabase.collection("users").document(SessionManager.getIdFirebase()).collection("desks").add(desk)
            .addOnSuccessListener {
                callback.onSaveSuccess(it.id)
            }.addOnFailureListener {
                callback.onError(it)
            }
    }

    override suspend fun deleteDesk(desk: Desk, callback: DeskDataSource.DeleteTaskCallback) {
        MyApplication.firebaseDatabase.collection("users").document(SessionManager.getIdFirebase()).collection("desks").document(desk.idRemote)
            .delete()
            .addOnSuccessListener {
                callback.onDeleteSuccess()
            }.addOnFailureListener {
                callback.onError(it)
            }
    }

    override fun addDeskShared(desk: DeskShared, callback: DeskDataSource.SaveTaskCallback) {
        MyApplication.firebaseDatabase.collection("shared").add(desk)
            .addOnSuccessListener {
                callback.onSaveSuccess(it.id)
            }.addOnFailureListener {
                callback.onError(it)
            }
    }

    override suspend fun getAllDesksWithCards(callback: DeskDataSource.LoadDesksWithCardsCallBack) {
        MyApplication.firebaseDatabase.collection("users").document(SessionManager.getIdFirebase()).collection("desks").get()
            .addOnSuccessListener { documents ->
                val list: ArrayList<DeskWithCards> = ArrayList()
                for (document in documents) {
                    var desk = document.toObject(Desk::class.java)
                    desk.idRemote = document.id
                    list.add(DeskWithCards(desk, desk.cards))
                }
                callback.onDesksLoaded(list)
            }.addOnFailureListener {
                callback.onError(it)
            }
    }

    override fun getAllDesksSharedWithCards(): Flow<Result<List<DeskShared>>> {

        return callbackFlow {
            trySend(Result.loading())

            val document = MyApplication.firebaseDatabase
                .collection("shared")

            val subscription = document.addSnapshotListener { snapshot, _ ->
                snapshot?.let {
                    val list: ArrayList<DeskShared> = ArrayList()
                    for (documentDesk in snapshot.documents) {
                        val desk = documentDesk.toObject(DeskShared::class.java)
                        desk?.idRemote = documentDesk.id
                        desk?.let {
                            list.add(desk)
                        }
                    }
                    trySend(Result.success(list))
                }
            }
            awaitClose { subscription.remove() }
        }
    }

    override fun uploadNumDownloadShareDesk(desk: DeskShared) {
        MyApplication.firebaseDatabase.collection("shared").document(desk.idRemote).update("timesDownloaded", desk.timesDownloaded + 1)
    }

    override fun deleteAllDesks() {
        MyApplication.firebaseDatabase.collection("users").document(SessionManager.getIdFirebase()).delete()
    }

    override suspend fun getDeskandCards(idDesk: String, callBack: CardDataSource.LoadCardsCallBack) {
        MyApplication.firebaseDatabase.collection("users").document(SessionManager.getIdFirebase()).collection("desks").document(idDesk).get()
            .addOnSuccessListener { documents ->
                var desk = documents.toObject(Desk::class.java)
                desk?.let {
                    var deskWithCards = DeskWithCards(it, it.cards)
                    callBack.onCardsLoaded(deskWithCards)
                } ?: callBack.onError(NullPointerException())

            }.addOnFailureListener {
                callBack.onError(it)
            }
    }

    override suspend fun getCardsAndData(
        idDesk: String
    ): Flow<Result<List<CardWithData>>> {

        return callbackFlow {
            trySend(Result.loading())

            val document = MyApplication.firebaseDatabase
                .collection("users")
                .document(SessionManager.getIdFirebase())
                .collection("desks")
                .document(idDesk)

            val subscription = document.addSnapshotListener { snapshot, _ ->
                if (snapshot!!.exists()) {
                    val list: ArrayList<CardWithData> = ArrayList()
                    val desk = snapshot.toObject(Desk::class.java)
                    desk?.let {
                        for (card in desk.cards) {
                            list.add(CardWithData(card, card.cardswithdata))
                        }
                    }
                    trySend(Result.success(list))

                }

            }

            awaitClose { subscription.remove() }
        }

    }

    override suspend fun addCard(idDesk: String, card: Card, callback: CardDataSource.SaveTaskCallback) {
        MyApplication.firebaseDatabase.collection("users").document(SessionManager.getIdFirebase()).collection("desks").document(idDesk)
            .update("cards", FieldValue.arrayUnion(card)).addOnSuccessListener {
                callback.onSaveSuccess()
            }.addOnFailureListener {
                callback.onError(it)
            }
    }

    override suspend fun updateCard(
        idDesk: String,
        listCards: List<CardWithData>,
        indexToUpload: Int,
        callback: CardDataSource.SaveTaskCallback
    ) {
        val list = ArrayList<Card>()
        for (item in listCards) {
            list.add(item.card)
        }

        MyApplication.firebaseDatabase.collection("users").document(SessionManager.getIdFirebase()).collection("desks").document(idDesk)
            .update("cards", list).addOnSuccessListener {
                callback.onSaveSuccess()
            }.addOnFailureListener {
                callback.onError(it)
            }
    }


}