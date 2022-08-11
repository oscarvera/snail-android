package com.oscarvera.snail.provider.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.oscarvera.snail.model.domain.*
import com.oscarvera.snail.provider.local.dao.CardsDao
import com.oscarvera.snail.provider.local.dao.DesksDao

const val DATABASE_VERSION = 1

@Database(entities = [Desk::class, Card::class, CardData::class], version = DATABASE_VERSION)
abstract class SnailDatabase : RoomDatabase() {

    companion object{

        private const val DATABASE_NAME = "database-snail"

        fun buildDatabase(context: Context): SnailDatabase{
            return Room.databaseBuilder(context, SnailDatabase::class.java, DATABASE_NAME).build()
        }
    }

    abstract fun deskDao() : DesksDao

    abstract fun cardDao() : CardsDao

}