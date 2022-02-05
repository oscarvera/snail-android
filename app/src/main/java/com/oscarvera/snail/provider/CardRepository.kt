package com.oscarvera.snail.provider

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.oscarvera.snail.MyApplication
import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.CardWithData
import com.oscarvera.snail.model.domain.Desk
import com.oscarvera.snail.model.domain.DeskWithCards
import com.oscarvera.snail.model.session.SessionManager
import com.oscarvera.snail.provider.preferences.PrefManager

class CardRepository : CardDataSource {
    override fun getDeskandCards(idDesk : String, callBack: CardDataSource.LoadCardsCallBack) {
        if (SessionManager.isLogged()){
            //Firebase
            val db = FirebaseFirestore.getInstance()
            PrefManager.userId?.let { userId ->
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
        }else{
            //Localmode
            val deskcards = MyApplication.localDatabase.cardDao().getCards(idDesk.toInt())
            callBack.onCardsLoaded(deskcards)
        }
    }

    override fun getCardsAndData(
        idDesk: String,
        callback: CardDataSource.LoadCardsAndDataCallBack
    ) {
        if (SessionManager.isLogged()){
            //Firebase
            val db = FirebaseFirestore.getInstance()
            PrefManager.userId?.let { userId ->
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
        }else{
            //Localmode
            val deskcards = MyApplication.localDatabase.cardDao().getCardsAndData(idDesk.toInt())
            callback.onCardsLoaded(deskcards)
        }

    }

    override fun addCard(idDesk: String, card: Card, callback: CardDataSource.SaveTaskCallback) {
        if (SessionManager.isLogged()){
            //Firebase
            val db = FirebaseFirestore.getInstance()
            PrefManager.userId?.let { userId ->
                db.collection("users").document(userId).collection("desks").document(idDesk).update("cards", FieldValue.arrayUnion(card)).addOnSuccessListener {
                    callback.onSaveSuccess()
                }.addOnFailureListener {
                    callback.onError(it)
                }
            }
        }else{
            //Localmode
            card.deskId = idDesk.toInt()
            var idCard = MyApplication.localDatabase.cardDao().addCard(card)
            for (carddata in card.cardswithdata) {
                carddata.cardId = idCard.toInt()
                MyApplication.localDatabase.cardDao().addCardData(carddata)
            }
            callback.onSaveSuccess()
        }
    }


}