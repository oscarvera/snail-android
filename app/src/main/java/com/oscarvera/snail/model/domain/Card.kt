package com.oscarvera.snail.model.domain

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "card")
data class Card(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val deskId : Int = 0,
    val date_added: String = "",
    val date_checked: String = "",
    val date_to_check: String = "",
    val quantifier: Int = 0,
)

@Entity(tableName = "carddata")
data class CardData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cardId: Int = 0,
    val text: String = ""
)


data class CardWithData(
    @Embedded val card: Card,
    @Relation(
        parentColumn = "id",
        entityColumn = "cardId"
    )
    val cardData: List<CardData>?
)
