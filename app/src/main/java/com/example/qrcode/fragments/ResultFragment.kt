package com.example.qrcode.fragments

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bld.qrscanner.R
import com.budiyev.android.codescanner.BarcodeUtils
import com.example.qrcode.data.AppDatabase.Companion.getDatabase
import com.example.qrcode.data.ScanHistory
import com.example.qrcode.data.ScanHistoryDao
import com.google.zxing.BarcodeFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date


class ResultFragment : Fragment() {

    private lateinit var ivBack: ImageView
    private lateinit var ivQRCode: ImageView
    private lateinit var ivDelete: ImageView
    private lateinit var ivFavourite: ImageView
    private lateinit var tvLink: TextView
    private lateinit var tvDate: TextView
    private lateinit var llCopy: LinearLayout
    private lateinit var llShare: LinearLayout
    private lateinit var llFavourite: LinearLayout
    private lateinit var btnLink: TextView
    private lateinit var scanHistoryDao: ScanHistoryDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scanHistoryDao = context?.let { getDatabase(it).ScanHistoryDao() }!!
        ivBack = view.findViewById(R.id.ivBack)
        ivDelete = view.findViewById(R.id.deleteIcon)
        ivFavourite = view.findViewById(R.id.ivFavourite)
        ivQRCode = view.findViewById(R.id.ivQRCode)
        tvLink = view.findViewById(R.id.tvLink)
        tvDate = view.findViewById(R.id.tvDate)
        llCopy = view.findViewById(R.id.llCopy)
        llShare = view.findViewById(R.id.llShare)
        llFavourite = view.findViewById(R.id.llFavourite)
        btnLink = view.findViewById(R.id.btnLink)

        val name: String = arguments?.getString("text", "") ?: ""
        val format = arguments?.getSerializable("format") as BarcodeFormat
        ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        val bmp = BarcodeUtils.encodeBitmap(name, format, 400, 400)
        ivQRCode.setImageBitmap(bmp)
        tvLink.text = name

        val sdf = SimpleDateFormat("MM/dd/yyyy HH:mm a")
        val currentDateAndTime = sdf.format(Date())

        tvDate.text = currentDateAndTime

        addDb(name, currentDateAndTime, format)
        llCopy.setOnClickListener {
            copyClipBoard(name)
        }

        llShare.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.setAction(Intent.ACTION_SEND)
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                name
            )
            sendIntent.setType("text/plain")
            startActivity(sendIntent)
        }

        llFavourite.setOnClickListener {
            updateDb()
        }

        ivDelete.setOnClickListener {
            deleteRecord()
        }

        if (name.contains("https://")) {
            btnLink.visibility = View.VISIBLE
        } else {
            btnLink.visibility = View.GONE
        }

        btnLink.setOnClickListener {
            openLink(name)
        }

    }

    private fun updateDb() {
        val latestRecord = scanHistoryDao.getLatest()
        latestRecord.favourite = !latestRecord.favourite!!
        scanHistoryDao.update(latestRecord)
        if (latestRecord.favourite == true) {
            ivFavourite.setImageResource(R.drawable.icon_favourite)
        } else {
            ivFavourite.setImageResource(R.drawable.icon_favourite_unselect)
        }
//        Toast.makeText(context, "Record added to Favourite", Toast.LENGTH_LONG).show()
    }

    private fun addDb(name: String, date: String, format: BarcodeFormat) {
        CoroutineScope(Dispatchers.IO).launch {
            val data: List<ScanHistory> =
                listOf(
                    ScanHistory(
                        null,
                        name,
                        date,
                        false,
                        true,
                        format
                    )
                )
            scanHistoryDao.insertAll(data)
        }
    }

    private fun copyClipBoard(name: String) {
        val clipboardManager =
            activity?.applicationContext?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("qr-code", name).apply {
            description.extras = PersistableBundle().apply {
                putBoolean(ClipDescription.MIMETYPE_TEXT_PLAIN, true)
            }
        }
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(context, "Copied to Clipboard", Toast.LENGTH_LONG).show()
    }

    private fun deleteRecord() {
        val latestRecord = scanHistoryDao.getLatest()
        CoroutineScope(Dispatchers.IO).launch {
            scanHistoryDao.delete(latestRecord)
        }

        Toast.makeText(context, "Record deleted successfully", Toast.LENGTH_LONG).show()
        findNavController().popBackStack()
    }

    private fun openLink(link: String) {
        val uri =
            Uri.parse(link) // missing 'http://' will cause crashed
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}