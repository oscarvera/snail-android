package com.oscarvera.snail.util

import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.CardWithData
import com.oscarvera.snail.model.domain.StatusCard
import com.oscarvera.snail.util.extensions.getStatusCard
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun dateIsAfter(date: String?): Boolean {
        return if (!date.isNullOrEmpty()) {
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val strDate: Date = formatter.parse(date)
            Date().after(strDate)
        } else {
            true //If it's empty It's consider not started, therefore to start now
        }
    }

    fun getLatestDate(cards: List<Card>, textWhenEmpty: String): String {
        val dates = ArrayList<Date>()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        cards.forEach {
            if (it.date_checked.isNotEmpty()) {
                dates.add(formatter.parse(it.date_checked))
            }
        }
        dates.sortByDescending { it }

        return if (dates.isEmpty()) {
            textWhenEmpty
        } else {
            formatter.format(dates.first())
        }
    }

    fun getNowDateFormatted(): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        return formatter.format(Date())
    }

    fun getDateFormatted(date: Date): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        return formatter.format(date)
    }

    fun getDateWithQuantifierApplied(quantifier: Int): String {
        val now = Date()
        val c = Calendar.getInstance()
        c.time = now
        c.add(Calendar.DATE, quantifier)
        val dateToCheck = c.time
        return getDateFormatted(dateToCheck)
    }

    fun countStatusCards(list: List<Card>): HashMap<StatusCard, Int> {
        var hasmap = HashMap<StatusCard, Int>().apply { }
        hasmap =
            hashMapOf(StatusCard.TO_LEARN to 0, StatusCard.LEARNING to 0, StatusCard.LEARNED to 0)
        for (card in list) {

            when (card.getStatusCard()) {
                StatusCard.TO_LEARN -> hasmap[StatusCard.TO_LEARN] =
                    hasmap[StatusCard.TO_LEARN]!! + 1
                StatusCard.LEARNING -> hasmap[StatusCard.LEARNING] =
                    hasmap[StatusCard.LEARNING]!! + 1
                StatusCard.LEARNED -> hasmap[StatusCard.LEARNED] = hasmap[StatusCard.LEARNED]!! + 1
            }

        }
        return hasmap
    }

    fun countStatusDeskWithCards(list: List<CardWithData>): HashMap<StatusCard, Int> {

        val listCards: ArrayList<Card> = ArrayList()

        for (cardwdata in list) {
            listCards.add(cardwdata.card)
        }
        return countStatusCards(listCards)
    }
}