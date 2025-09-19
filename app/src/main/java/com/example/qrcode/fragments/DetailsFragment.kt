package com.example.qrcode.fragments

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bld.qrscanner.R
import com.budiyev.android.codescanner.BarcodeUtils
import com.example.qrcode.data.AppDatabase.Companion.getDatabase
import com.example.qrcode.data.ScanHistory
import com.example.qrcode.data.ScanHistoryDao
import com.example.qrcode.viewModel.ScanHistoryViewModel
import com.google.zxing.BarcodeFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {

    private lateinit var ivBack: ImageView
    private lateinit var ivQRCode: ImageView
    private lateinit var ivFavourite: ImageView
    private lateinit var tvLink: TextView
    private lateinit var tvDate: TextView
    private lateinit var llCopy: LinearLayout
    private lateinit var llShare: LinearLayout
    private lateinit var llFavourite: LinearLayout
    private lateinit var scanHistoryDao: ScanHistoryDao
    private lateinit var record: ScanHistory
    private lateinit var viewModel: ScanHistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[ScanHistoryViewModel::class.java]

        scanHistoryDao = context?.let { getDatabase(it).ScanHistoryDao() }!!
        ivBack = view.findViewById(R.id.ivBack)
        ivQRCode = view.findViewById(R.id.ivQRCode)
        ivFavourite = view.findViewById(R.id.ivFavourite)
        tvLink = view.findViewById(R.id.tvLink)
        tvDate = view.findViewById(R.id.tvDate)
        llCopy = view.findViewById(R.id.llCopy)
        llShare = view.findViewById(R.id.llShare)
        llFavourite = view.findViewById(R.id.llFavourite)

        val id = arguments?.getInt("id")
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
            findNavController().popBackStack()
        }
        val bmp = BarcodeUtils.encodeBitmap(record.link!!, record.format!!, 400, 400)
        ivQRCode.setImageBitmap(bmp)
        tvLink.text = record.link

        tvDate.text = record.date

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
    }

    private fun getRecord(id: Int?) {
        record = scanHistoryDao.getSpecific(id!!)
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

    private fun updateDb() {
        record.favourite = !record.favourite!!
        scanHistoryDao.update(record)
        if (record.favourite == true) {
            ivFavourite.setImageResource(R.drawable.icon_favourite)
        } else {
            ivFavourite.setImageResource(R.drawable.icon_favourite_unselect)
        }

        Toast.makeText(context, "Record added to Favourite", Toast.LENGTH_LONG).show()
    }

}