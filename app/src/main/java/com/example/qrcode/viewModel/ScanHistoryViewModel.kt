package com.example.qrcode.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.qrcode.ScanHistoryRepository
import com.example.qrcode.data.AppDatabase
import com.example.qrcode.data.ScanHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScanHistoryViewModel (application: Application): AndroidViewModel(application) {

    val allScanHistory: LiveData<List<ScanHistory>>
//    val specificScanHistory: ScanHistory
    val repository: ScanHistoryRepository

    init {
        val dao = AppDatabase.getDatabase(application).ScanHistoryDao()
        repository = ScanHistoryRepository(dao)
        allScanHistory = repository.allScanHistory
//        specificScanHistory = repository.getSpecificRecord()
    }

    fun deleteHistory(scanHistory: ScanHistory) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteRecord(scanHistory)
    }

    fun updateHistory(scanHistory: ScanHistory) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateRecord(scanHistory)
    }

    fun addHistory(scanHistory: ScanHistory) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertRecord(scanHistory)
    }

    fun addHistory(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.getSpecificRecord(id)
    }
}