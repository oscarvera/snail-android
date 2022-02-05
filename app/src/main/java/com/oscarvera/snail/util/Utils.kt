package com.oscarvera.snail.util

import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.StatusCard
import com.oscarvera.snail.util.extensions.getStatusCard
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

object Utils {

    fun dateIsAfter(date : String?): Boolean{
        return if (!date.isNullOrEmpty()) {
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val strDate: Date = formatter.parse(date)
            Date().after(strDate)
        }else{
            true //If it's empty It's consider not started, therefore to start now
        }
    }

    fun getNowDateFormatted() : String{
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        return formatter.format(Date())
    }

    fun countStatusCards(list: List<Card>) : HashMap<StatusCard,Int> {
        var hasmap = HashMap<StatusCard,Int>().apply {  }
        hasmap = hashMapOf(StatusCard.TO_LEARN to 0, StatusCard.LEARNING to 0, StatusCard.LEARNED to 0)
        for (card in list){

            when (card.getStatusCard()){
                StatusCard.TO_LEARN -> hasmap[StatusCard.TO_LEARN] = hasmap[StatusCard.TO_LEARN]!!+1
                StatusCard.LEARNING -> hasmap[StatusCard.LEARNING] = hasmap[StatusCard.LEARNING]!!+1
                StatusCard.LEARNED -> hasmap[StatusCard.LEARNED] = hasmap[StatusCard.LEARNED]!!+1
            }

        }
        return hasmap
    }
}