package com.oscarvera.snail.provider.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.oscarvera.snail.model.domain.*
import com.oscarvera.snail.provider.local.dao.DesksDao

@Database(entities = [Desk::class, Card::class, CardData::class], version = 1)
abstract class DesksDatabase : RoomDatabase() {

    abstract fun deskDao() : DesksDao

}