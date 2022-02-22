package com.oscarvera.snail.model.domain

import androidx.room.*

@Entity(tableName = "desk")
data class Desk(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @Ignore var idRemote : String = "",
    var name: String = "",
    var isOnlineShared: Boolean = false,
    @Ignore var cards: List<Card> = listOf()
)

data class DeskWithCards(
    @Embedded var desk: Desk,
    @Relation(
        parentColumn = "id",
        entityColumn = "deskId"
    )
    var cards: List<Card>?
)

data class DeskShared(
    var name: String = "",
    var idRemote: String = "",
    var userName : String = "",
    var uploaded : String = "",
    var timesDownloaded : Int = 0,
    var cards: List<Card>? = listOf()
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