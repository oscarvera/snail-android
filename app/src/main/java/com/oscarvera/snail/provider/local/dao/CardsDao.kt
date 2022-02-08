package com.oscarvera.snail.provider.local.dao

import androidx.room.*
import com.oscarvera.snail.model.domain.*

@Dao
interface CardsDao {

    @Transaction
    @Query("SELECT * FROM desk WHERE id is :idDesk")
    fun getCards(idDesk: Int): DeskWithCards

    @Transaction
    @Query("SELECT * FROM card where deskId is :idDesk")
    fun getCardsAndData(idDesk: Int): List<CardWithData>

    @Insert
    fun addCard( card: Card) : Long

    @Insert
    fun addCardData( card: CardData)

    @Update
    fun updateCard(card : Card)



}