package com.oscarvera.snail.provider.local.dao

import androidx.room.*
import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.Desk
import com.oscarvera.snail.model.domain.DeskWithCards
import kotlinx.coroutines.flow.Flow

@Dao
interface DesksDao {

    @Query("SELECT * FROM desk")
    fun getDesks(): Flow<List<Desk>>

    @Query("SELECT * FROM desk Where id = :id")
    fun getDesk(id : Int): Flow<Desk>

    @Transaction
    @Query("SELECT * FROM desk")
    suspend fun getAllDeskswithCards(): List<DeskWithCards>

    @Insert
    suspend fun addDesk( desk: Desk) : Long

    @Delete
    suspend fun delete(desk: Desk)

}