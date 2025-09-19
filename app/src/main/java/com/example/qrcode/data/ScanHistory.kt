package com.example.qrcode.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.zxing.BarcodeFormat

@Entity
data class ScanHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val link: String?,
    val date: String?,
    var favourite: Boolean?,
    var scan: Boolean?,
    var format: BarcodeFormat?
)
