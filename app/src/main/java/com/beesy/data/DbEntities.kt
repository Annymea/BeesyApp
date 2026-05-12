package com.beesy.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "checked") val checked: Boolean,
)

// Damit er keinen Fehler schmeißt. Sollen iwann die anderen Tabellen werden
@Entity
data class User(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "checked") val checked: Boolean?,
)