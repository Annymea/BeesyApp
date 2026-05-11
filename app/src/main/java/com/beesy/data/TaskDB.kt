package com.beesy.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Hier wird die Datenbank erstellt

@Database(entities = [Task::class], version = 1)
abstract class TaskDB : RoomDatabase() {
    abstract fun taskDAO(): TaskDAO

    companion object {
        @Volatile
        private var instance: TaskDB? = null

        fun getDatabase(context: Context): TaskDB {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return instance ?: synchronized(this) {
                Room
                    .databaseBuilder(context, TaskDB::class.java, "task_database")
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { instance = it }
            }
        }
    }
}