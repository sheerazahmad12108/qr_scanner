package com.example.qrcode.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ScanHistoryDao {

    @Query("SELECT * FROM ScanHistory ")
    fun getAll(): List<ScanHistory>

    @Query("SELECT * FROM ScanHistory WHERE scan == 1")
    fun getScanned(): List<ScanHistory>

    @Query("SELECT * FROM ScanHistory WHERE scan == 0")
    fun getCreated(): List<ScanHistory>

    @Query("SELECT * FROM ScanHistory WHERE favourite == 1")
    fun getFavourite(): List<ScanHistory>

    @Query("SELECT * FROM ScanHistory ORDER BY id DESC LIMIT 1")
    fun getLatest(): ScanHistory

    @Query("SELECT * FROM ScanHistory WHERE id = :id")
    fun getSpecific(id: Int): ScanHistory

    @Insert
    fun insertAll(tabScanItem: List<ScanHistory>)

    @Delete
    fun delete(tabScanItem: ScanHistory)

    @Update
    fun update(tabScanItem: ScanHistory)


//    Testing

    @Insert
    suspend fun insertRecord(tabScanItem: ScanHistory)

    @Update
    suspend fun updateRecord(tabScanItem: ScanHistory)

    @Delete
    suspend fun deleteRecord(tabScanItem: ScanHistory)

    @Query("SELECT * FROM ScanHistory WHERE scan == 0 order by id ASC")
    fun getAllRecords(): LiveData<List<ScanHistory>>
}