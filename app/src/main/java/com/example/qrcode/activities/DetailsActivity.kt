package com.example.qrcode.activities

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bld.qrscanner.R
import com.budiyev.android.codescanner.BarcodeUtils
import com.example.qrcode.data.AppDatabase.Companion.getDatabase
import com.example.qrcode.data.ScanHistory
import com.example.qrcode.data.ScanHistoryDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsActivity : AppCompatActivity() {

    private lateinit var ivBack: ImageView
    private lateinit var ivDelete: ImageView
    private lateinit var ivQRCode: ImageView
    private lateinit var ivFavourite: ImageView
    private lateinit var tvLink: TextView
    private lateinit var tvDate: TextView
    private lateinit var llCopy: LinearLayout
    private lateinit var llShare: LinearLayout
    private lateinit var llFavourite: LinearLayout
    private lateinit var btnLink: TextView
    private lateinit var scanHistoryDao: ScanHistoryDao
    private lateinit var record: ScanHistory

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_details)

        scanHistoryDao = getDatabase(this).ScanHistoryDao()
        ivBack = findViewById(R.id.ivBack)
        ivDelete = findViewById(R.id.deleteIcon)
        ivQRCode = findViewById(R.id.ivQRCode)
        ivFavourite = findViewById(R.id.ivFavourite)
        tvLink = findViewById(R.id.tvLink)
        tvDate = findViewById(R.id.tvDate)
        llCopy = findViewById(R.id.llCopy)
        llShare = findViewById(R.id.llShare)
        llFavourite = findViewById(R.id.llFavourite)
        btnLink = findViewById(R.id.btnLink)

        val bundle = intent.extras
        val id = bundle?.getInt("id")

        getRecord(id)
        if (record.favourite == true) {
            ivFavourite.setImageResource(R.drawable.icon_favourite)
        } else {
            ivFavourite.setImageResource(R.drawable.icon_favourite_unselect)
        }

        ivFavourite.setOnClickListener {
            updateDb()
        }
        ivBack.setOnClickListener {
            onBackPressed()
        }
        llCopy.setOnClickListener {
            copyClipBoard(record.link!!)
        }
        llShare.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.setAction(Intent.ACTION_SEND)
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                record.link
            )
            sendIntent.setType("text/plain")
            startActivity(sendIntent)
        }
        ivDelete.setOnClickListener {
            deleteRecord()
        }

        val bmp = BarcodeUtils.encodeBitmap(record.link!!, record.format!!, 400, 400)
        ivQRCode.setImageBitmap(bmp)
        tvLink.text = record.link

        tvDate.text = record.date
        if (record.link!!.contains("https://")) {
            btnLink.visibility = View.VISIBLE
        } else {
            btnLink.visibility = View.GONE
        }

        btnLink.setOnClickListener {
            openLink(record.link!!)
        }

    }

    private fun getRecord(id: Int?) {
        record = scanHistoryDao.getSpecific(id!!)
    }

    private fun copyClipBoard(name: String) {
        val clipboardManager =
            applicationContext?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("qr-code", name).apply {
            description.extras = PersistableBundle().apply {
                putBoolean(ClipDescription.MIMETYPE_TEXT_PLAIN, true)
            }
        }
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this, "Copied to Clipboard", Toast.LENGTH_LONG).show()
    }

    private fun updateDb() {
        record.favourite = !record.favourite!!
        scanHistoryDao.update(record)
        if (record.favourite == true) {
            ivFavourite.setImageResource(R.drawable.icon_favourite)
        } else {
            ivFavourite.setImageResource(R.drawable.icon_favourite_unselect)
        }

//        Toast.makeText(this, "Record added to Favourite", Toast.LENGTH_LONG).show()
    }

    private fun deleteRecord() {
//        CoroutineScope(Dispatchers.IO).launch {
            scanHistoryDao.delete(record)
//        }

        Toast.makeText(this, "Record deleted successfully", Toast.LENGTH_LONG).show()
        onBackPressed()
    }

    private fun openLink(link: String) {
        val uri =
            Uri.parse(link) // missing 'http://' will cause crashed
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}