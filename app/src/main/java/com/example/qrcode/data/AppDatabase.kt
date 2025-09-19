package com.example.qrcode.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ScanHistory::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ScanHistoryDao(): ScanHistoryDao

    companion object {
        const val DATABASE_NAME = "scan_history_database"

        @JvmStatic
        fun getDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME
            )
//                .addCallback(object : Callback() {
//                override fun onCreate(db: SupportSQLiteDatabase) {
//                    super.onCreate(db)
//                    populateDB(context)
//                }
//            })
                .allowMainThreadQueries().build()
        }

        private fun populateDB(context: Context) {
            CoroutineScope(Dispatchers.IO).launch {
//                val data: List<ScanHistory> =
//                    listOf(
//                        ScanHistory(
//                            1,
//                            "https//:www.abc.com/abc/abc/abc/abc/abc/abc",
//                            "11/06/2009",
//                            true
//                        ),
//                        ScanHistory(
//                            2,
//                            "https://www.google.com/search?q=populate+room+database+from+list&sca_esv=636238c710bc0fc7&sxsrf=ADLYWIJd6e7rtrFH4ZySlRgsBeoZVNmkpw%3A1733913417069&ei=SWtZZ4f3A8_9i-gPj8Xy8Qc&oq=populate+room+database+from+lis&gs_lp=Egxnd3Mtd2l6LXNlcnAiH3BvcHVsYXRlIHJvb20gZGF0YWJhc2UgZnJvbSBsaXMqAggAMgUQIRigATIFECEYoAEyBRAhGKABMgQQIRgVMgUQIRifBUja5wVQ2QlYhtwFcAR4AZABAJgBnAOgAekeqgEIMi0xMS4yLjG4AQPIAQD4AQGYAhKgAt0fwgIKEAAYsAMY1gQYR8ICBhAAGAcYHsICCxAAGIAEGIYDGIoFwgIIEAAYgAQYogTCAgYQABgWGB6YAwCIBgGQBgiSBwo0LjAuMTEuMS4yoAevUQ&sclient=gws-wiz-serp",
//                            "20/12/2021",
//                            false
//                        )
//                    )
//                val scanHistoryDao = getDatabase(context).ScanHistoryDao()
//                scanHistoryDao.insertAll(data)
            }

        }
    }
}