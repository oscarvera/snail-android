package com.oscarvera.snail.provider.services.firebase

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.CardWithData
import com.oscarvera.snail.model.domain.Desk
import com.oscarvera.snail.model.domain.DeskWithCards
import com.oscarvera.snail.model.session.SessionManager
import com.oscarvera.snail.provider.CardDataSource
import com.oscarvera.snail.provider.DeskDataSource

class FirebaseRepository : DeskDataSource, CardDataSource {

    override fun getDesk(id: String, callBack: DeskDataSource.LoadDeskCallBack) {
        val db = FirebaseFirestore.getInstance()
        SessionManager.getIdFirebase()?.let { userId ->
            db.collection("users").document(userId).collection("desks").document(id).get()
                .addOnSuccessListener { document ->
                    val desk = document.toObject(Desk::class.java)
                    desk?.let {
                        callBack.onDeskLoaded(it)
                    } ?: callBack.onError(NullPointerException())
                }.addOnFailureListener {
                    callBack.onError(it)
                }
        }
    }

    override fun getDesks(callBack: DeskDataSource.LoadDesksCallBack) {
        val db = FirebaseFirestore.getInstance()
        SessionManager.getIdFirebase()?.let { userId ->
            db.collection("users").document(userId).collection("desks").get()
                .addOnSuccessListener { documents ->
                    val list: ArrayList<Desk> = ArrayList()
                    for (document in documents) {
                        val desk = document.toObject(Desk::class.java)
                        desk.idRemote = document.id
                        list.add(desk)
                    }
                    callBack.onDesksLoaded(list)
                }.addOnFailureListener {
                    callBack.onError(it)
                }
        }
    }

    override fun addDesk(desk: Desk, callback: DeskDataSource.SaveTaskCallback) {
        val db = FirebaseFirestore.getInstance()
        SessionManager.getIdFirebase()?.let { userId ->
            db.collection("users").document(userId).collection("desks").add(desk)
                .addOnSuccessListener {
                    callback.onSaveSuccess()
                }.addOnFailureListener {
                    callback.onError(it)
                }
        }
    }

    override fun getAllDesksWithCards(callback: DeskDataSource.LoadDesksWithCardsCallBack) {
        val db = FirebaseFirestore.getInstance()
        SessionManager.getIdFirebase()?.let { userId ->
            db.collection("users").document(userId).collection("desks").get()
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
    }

    override fun getDeskandCards(idDesk: String, callBack: CardDataSource.LoadCardsCallBack) {
        val db = FirebaseFirestore.getInstance()
        SessionManager.getIdFirebase()?.let { userId ->
            db.collection("users").document(userId).collection("desks").document(idDesk).get()
                .addOnSuccessListener { documents ->
                    var desk = documents.toObject(Desk::class.java)
                    desk?.let {
                        var deskWithCards = DeskWithCards(it,it.cards)
                        callBack.onCardsLoaded(deskWithCards)
                    }?: callBack.onError(NullPointerException())

                }.addOnFailureListener {
                    callBack.onError(it)
                }
        }
    }

    override fun getCardsAndData(
        idDesk: String,
        callback: CardDataSource.LoadCardsAndDataCallBack
    ) {
        val db = FirebaseFirestore.getInstance()
        SessionManager.getIdFirebase()?.let { userId ->
            db.collection("users").document(userId).collection("desks").document(idDesk).get()
                .addOnSuccessListener { documents ->
                    val list : ArrayList<CardWithData> = ArrayList()
                    val desk = documents.toObject(Desk::class.java)
                    desk?.let {
                        for (card in desk.cards){
                            list.add(CardWithData(card,card.cardswithdata))
                        }
                    }

                    /*for (document in documents) {

                        card.idRemote = document.id
                        list.add(CardWithData(card,card.cardswithdata))
                    }*/

                    callback.onCardsLoaded(list)

                }.addOnFailureListener {
                    callback.onError(it)
                }
        }
    }

    override fun addCard(idDesk: String, card: Card, callback: CardDataSource.SaveTaskCallback) {
        val db = FirebaseFirestore.getInstance()
        SessionManager.getIdFirebase()?.let { userId ->
            db.collection("users").document(userId).collection("desks").document(idDesk).update("cards", FieldValue.arrayUnion(card)).addOnSuccessListener {
                callback.onSaveSuccess()
            }.addOnFailureListener {
                callback.onError(it)
            }
        }
    }

    override fun updateCard(
        idDesk: String,
        listCards: List<CardWithData>,
        indexToUpload: Int,
        callback: CardDataSource.SaveTaskCallback
    ) {
        val db = FirebaseFirestore.getInstance()
        SessionManager.getIdFirebase()?.let { userId ->
            val list = ArrayList<Card>()
            for ( item in listCards){
                list.add(item.card)
            }

            db.collection("users").document(userId).collection("desks").document(idDesk).update("cards", list).addOnSuccessListener {
                callback.onSaveSuccess()
            }.addOnFailureListener {
                callback.onError(it)
            }
        }
    }


}