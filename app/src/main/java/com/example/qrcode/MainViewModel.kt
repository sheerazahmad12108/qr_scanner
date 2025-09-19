package com.example.qrcode

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.qrcode.data.AppDatabase
import com.example.qrcode.data.ScanHistory
import com.example.qrcode.data.ScanHistoryDao
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {

//    private val dao = AppDatabase.getDatabase(app.applicationContext).ScanHistoryDao()

//    private val scanHistoryRepository = ScanHistoryRepository(app)

//    fun insertRecord(scanHistory: ScanHistory) = viewModelScope.launch {
//        scanHistoryRepository.insertRecord(scanHistory)
//    }
//
//    fun updateRecord(scanHistory: ScanHistory) = viewModelScope.launch {
//        scanHistoryRepository.updateRecord(scanHistory)
//    }
//
//    fun deleteRecord(scanHistory: ScanHistory) = viewModelScope.launch {
//        scanHistoryRepository.deleteRecord(scanHistory)
//    }


    private val _listRecords = MutableLiveData<List<ScanHistory>>()
    val listRecords:LiveData<List<ScanHistory>> get() = _listRecords

    fun getAllRecords(dao: ScanHistoryDao){
      _listRecords.postValue(dao.getScanned())
    }

    private val _listCreatedRecords = MutableLiveData<List<ScanHistory>>()
    val listCreatedRecords:LiveData<List<ScanHistory>> get() = _listCreatedRecords

    fun getAllCreatedRecords(dao: ScanHistoryDao){
        _listCreatedRecords.postValue(dao.getCreated())
    }


    private val _listToDelete = MutableLiveData<List<ScanHistory>>()
    val listToDelete: LiveData<List<ScanHistory>> get() =  _listToDelete

    private var list: List<ScanHistory> = listOf()


//    fun getAll(): LiveData<List<ScanHistory>> = scanHistoryRepository.getAllRecords()

    fun setSelectedList(list: List<ScanHistory>) {
        _listToDelete.postValue(list)
    }

    fun getSelectedList(): LiveData<List<ScanHistory>> {
        return listToDelete
    }

//    class ScanHistoryViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
//                return MainViewModel(app) as T
//            }
//            throw IllegalArgumentException("Unable construct viewmodel")
//        }
//    }


}