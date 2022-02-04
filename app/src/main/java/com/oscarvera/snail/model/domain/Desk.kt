package com.oscarvera.snail.model.domain

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "desk")
data class Desk(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String = "",
    val isOnlineShared: Boolean = false,

)

data class DeskWithCards(
    @Embedded val desk: Desk,
    @Relation(
        parentColumn = "id",
        entityColumn = "deskId"
    )
    val cards: List<Card>?
)


/* TEST MODE
data class Desk (
    val name: String,
    val description: String = "",
    val listCards : List<Card>
)

data class Card(
    val firtText : String,
    val secondText : String
)

val testDesks = listOf(
    Desk("nombre desk 1","descripcion 1", listOf(Card("Hola","Hello"),Card("Adios","Bye")))
)
val testDesksToLearnList = listOf(
    DeskList("nombre desk 1",12, 4,3,"24 Enero"),
    DeskList("nombre desk 2", 7,2,5,"23 Enero")
)
val testDesksAllList = listOf(
    DeskList("nombre desk 3", null,null,null,null),
    DeskList("nombre desk 4", null,null,null,null)

)

data class DeskList(
    val name: String,
    val numberTotalCards: Int?,
    val numberCardsToLearn: Int?,
    val numberCardsLearned: Int?,
    val lastDayLearned: String?,
)

*/