package com.oscarvera.snail.model.domain

import androidx.room.*
import com.oscarvera.snail.provider.preferences.PrefManager
import com.oscarvera.snail.util.Utils

@Entity(tableName = "card")
data class Card(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var deskId: Int = 0,
    var date_added: String = "",
    var date_checked: String = "",
    var date_to_check: String = "",
    var quantifier: Int = 0,
    @Ignore var cardswithdata : List<CardData> = listOf(CardData(0,0,"Adios"), CardData(0,0,"Bye")),
    @Ignore var idRemote : String = "",
)

fun getEj(): Card{
    val card = Card()
    card.date_added = Utils.getNowDateFormatted()
    return card
}

@Entity(tableName = "carddata")
data class CardData(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var cardId: Int = 0,
    var text: String = ""
)


data class CardWithData(
    @Embedded val card: Card,
    @Relation(
        parentColumn = "id",
        entityColumn = "cardId"
    )
    val cardData: List<CardData>?
)

enum class StatusCard {
    TO_LEARN,
    LEARNING,
    LEARNED
}


