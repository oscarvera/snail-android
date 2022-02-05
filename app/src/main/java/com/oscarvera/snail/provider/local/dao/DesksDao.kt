package com.oscarvera.snail.provider.local.dao

import androidx.room.*
import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.Desk
import com.oscarvera.snail.model.domain.DeskWithCards

@Dao
interface DesksDao {

    @Query("SELECT * FROM desk")
    fun getDesks(): List<Desk>

    @Query("SELECT * FROM desk Where id = :id")
    fun getDesk(id : Int): Desk

    @Transaction
    @Query("SELECT * FROM desk")
    fun getAllDeskswithCards(): List<DeskWithCards>

    @Insert
    fun addDesk( desk: Desk)

    @Delete
    fun delete(desk: Desk)

}