package com.oscarvera.snail.model.domain

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

data class User(
    @PrimaryKey(autoGenerate = false)
    val id : Int = 0,
)








