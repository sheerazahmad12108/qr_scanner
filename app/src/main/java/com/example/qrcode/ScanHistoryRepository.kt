package com.example.qrcode

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.qrcode.data.AppDatabase
import com.example.qrcode.data.ScanHistory
import com.example.qrcode.data.ScanHistoryDao

class ScanHistoryRepository(private val scanHistoryDao: ScanHistoryDao) {
//    private val scanHistoryDao: ScanHistoryDao
//    init {
//        val appDatabase:AppDatabase = AppDatabase.getDatabase(app)
//        scanHistoryDao = appDatabase.ScanHistoryDao()
//    }

    val allScanHistory : LiveData<List<ScanHistory>> = scanHistoryDao.getAllRecords()

    suspend fun insertRecord(scanHistory: ScanHistory) = scanHistoryDao.insertRecord(scanHistory)
    suspend fun updateRecord(scanHistory: ScanHistory) = scanHistoryDao.updateRecord(scanHistory)
    suspend fun deleteRecord(scanHistory: ScanHistory) = scanHistoryDao.deleteRecord(scanHistory)
    suspend fun getSpecificRecord(id:Int) = scanHistoryDao.getSpecific(id)

    fun getAllRecords():LiveData<List<ScanHistory>> = scanHistoryDao.getAllRecords()
}