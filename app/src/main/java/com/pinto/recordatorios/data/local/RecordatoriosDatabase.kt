package com.pinto.recordatorios.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RecordatorioEntity::class], version = 1, exportSchema = false)
abstract class RecordatoriosDatabase : RoomDatabase() {
    abstract fun recordatorioDao(): RecordatorioDao

    companion object {
        @Volatile
        private var INSTANCE: RecordatoriosDatabase? = null

        fun getInstance(context: Context): RecordatoriosDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    RecordatoriosDatabase::class.java,
                    "recordatorios.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}