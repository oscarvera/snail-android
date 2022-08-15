package com.oscarvera.snail.provider.local.dao

import androidx.room.*
import com.oscarvera.snail.model.domain.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CardsDao {

    @Transaction
    @Query("SELECT * FROM desk WHERE id is :idDesk")
    suspend fun getCards(idDesk: Int): DeskWithCards

    @Transaction
    @Query("SELECT * FROM card where deskId is :idDesk")
    fun getCardsAndData(idDesk: Int): Flow<List<CardWithData>>

    @Insert
    suspend fun addCard( card: Card) : Long

    @Insert
    suspend fun addCardData( card: CardData)

    @Update
    suspend fun updateCard(card : Card)



}