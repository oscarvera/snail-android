package com.oscarvera.snail.provider

import com.google.firebase.firestore.FirebaseFirestore
import com.oscarvera.snail.MyApplication
import com.oscarvera.snail.model.domain.Desk
import com.oscarvera.snail.model.session.SessionManager
import com.oscarvera.snail.provider.preferences.PrefManager

class DataRepository : DeskDataSource {

    override fun getDesks(callBack: DeskDataSource.LoadDesksCallBack) {
        if (SessionManager.isLogged()){
            //Firebase
            val db = FirebaseFirestore.getInstance()
            PrefManager.userId?.let { userId ->
                db.collection("users").document(userId).collection("desks").get()
                    .addOnSuccessListener { documents ->
                        val list : ArrayList<Desk> = ArrayList()
                        for (document in documents) {
                            list.add(document.toObject(Desk::class.java))
                        }
                        callBack.onDesksLoaded(list)
                    }.addOnFailureListener {
                        callBack.onError(it)
                    }
            }
        }else{
            //Localmode
            val desks = MyApplication.localDatabase.deskDao().getDesks()
            callBack.onDesksLoaded(desks = desks)
        }
    }

    override fun addDesk(desk: Desk, callback: DeskDataSource.SaveTaskCallback) {
        if (SessionManager.isLogged()){
            //Firebase
            val db = FirebaseFirestore.getInstance()
            PrefManager.userId?.let { userId ->
                db.collection("users").document(userId).collection("desks").add(desk).addOnSuccessListener {
                    callback.onSaveSuccess()
                }.addOnFailureListener {
                    callback.onError(it)
                }
            }
        }else{
            //Localmode
            MyApplication.localDatabase.deskDao().addDesk(desk = desk)
            callback.onSaveSuccess()
        }
    }

    override fun getAllDesksWithCards(callback: DeskDataSource.LoadDesksWithCardsCallBack) {
        if (SessionManager.isLogged()){
            //Firebase
            /*val db = FirebaseFirestore.getInstance()
            PrefManager.userId?.let { userId ->
                db.collection("users").document(userId).collection("desks").get()
                    .addOnSuccessListener { documents ->
                        val list : ArrayList<Desk> = ArrayList()
                        for (document in documents) {
                            list.add(document.toObject(Desk::class.java))
                        }
                        callBack.onDesksLoaded(list)
                    }.addOnFailureListener {
                        callBack.onError(it)
                    }
            }*/
        }else{
            //Localmode
            val desks = MyApplication.localDatabase.deskDao().getAllDeskswithCards()
            callback.onDesksLoaded(desks = desks)

        }
    }

}