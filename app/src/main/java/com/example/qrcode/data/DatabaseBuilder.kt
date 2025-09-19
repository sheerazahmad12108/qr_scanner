package com.example.qrcode.data

import android.content.Context
import android.provider.CalendarContract.Instances
import androidx.room.Room

object DatabaseBuilder {
    private var Instances: AppDatabase? = null
    const val DATABASE_NAME = "scan_history_database"

    fun getInstance(context: Context): AppDatabase {
        if (Instances == null) {
            synchronized(AppDatabase::class) {
                Instances = buildRoomDB(context)
            }
        }
        return Instances!!
    }

    private fun buildRoomDB(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}