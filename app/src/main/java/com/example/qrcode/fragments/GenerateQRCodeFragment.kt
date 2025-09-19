package com.example.qrcode.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bld.qrscanner.R
import com.bld.qrscanner.databinding.FragmentGenerateQRCodeBinding
import com.budiyev.android.codescanner.BarcodeUtils
import com.example.qrcode.data.AppDatabase.Companion.getDatabase
import com.example.qrcode.data.ScanHistory
import com.example.qrcode.data.ScanHistoryDao
import com.github.ybq.android.spinkit.SpinKitView
import com.github.ybq.android.spinkit.style.FadingCircle
import com.google.zxing.BarcodeFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class GenerateQRCodeFragment : Fragment() {

    private lateinit var binding: FragmentGenerateQRCodeBinding
    private var bmp: Bitmap? = null
    private var name: String = ""
    private var position: Int = -1
    private var data: String = ""
    private val myCalendar: Calendar = Calendar.getInstance()
    private val myCalendarEndDate: Calendar = Calendar.getInstance()
    private lateinit var scanHistoryDao: ScanHistoryDao
    private lateinit var progressBar: SpinKitView
    private var openLink: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGenerateQRCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scanHistoryDao = context?.let { getDatabase(it).ScanHistoryDao() }!!
        progressBar = binding.progressBar
        val circle = FadingCircle()
        progressBar.setIndeterminateDrawable(circle)
        name = arguments?.getString("title", "").toString()
        position = arguments?.getInt("position", 0)!!
        binding.tvTitle.text = name
        binding.deleteIcon.visibility = View.GONE
        binding.llQrCode.visibility = View.GONE

        setLayout(position)

        binding.mcvHttps.setOnClickListener {
            binding.mcvHttps.setCardBackgroundColor(resources.getColor(R.color.b4))
            binding.tvHttps.setTextColor(resources.getColor(R.color.white))
            binding.edtUrl.setText(binding.tvHttps.text)
            binding.edtUrl.setSelection(binding.edtUrl.text.length)
        }

        binding.mcvWww.setOnClickListener {
            binding.mcvWww.setCardBackgroundColor(resources.getColor(R.color.b4))
            binding.tvWww.setTextColor(resources.getColor(R.color.white))
            binding.edtUrl.text.insert(binding.edtUrl.selectionStart, "WWW.")
        }

        binding.mcvCom.setOnClickListener {
            binding.mcvCom.setCardBackgroundColor(resources.getColor(R.color.b4))
            binding.tvCom.setTextColor(resources.getColor(R.color.white))
            binding.edtUrl.text.insert(binding.edtUrl.selectionStart, ".COM")
        }

        binding.tvWPA.setOnClickListener {
            binding.tvWPA.setTextColor(resources.getColor(R.color.white))
            binding.tvWep.setTextColor(resources.getColor(R.color.clr97))
            binding.tvNone.setTextColor(resources.getColor(R.color.clr97))
            binding.tvWPA.background = resources.getDrawable(R.drawable.btn_curve_small)
            binding.tvWep.background = resources.getDrawable(R.drawable.btn_blank)
            binding.tvNone.background = resources.getDrawable(R.drawable.btn_blank)
        }

        binding.tvWep.setOnClickListener {
            binding.tvWep.setTextColor(resources.getColor(R.color.white))
            binding.tvWPA.setTextColor(resources.getColor(R.color.clr97))
            binding.tvNone.setTextColor(resources.getColor(R.color.clr97))
            binding.tvWep.background = resources.getDrawable(R.drawable.btn_curve_small)
            binding.tvWPA.background = resources.getDrawable(R.drawable.btn_blank)
            binding.tvNone.background = resources.getDrawable(R.drawable.btn_blank)
        }

        binding.tvNone.setOnClickListener {
            binding.tvNone.setTextColor(resources.getColor(R.color.white))
            binding.tvWep.setTextColor(resources.getColor(R.color.clr97))
            binding.tvWPA.setTextColor(resources.getColor(R.color.clr97))
            binding.tvNone.background = resources.getDrawable(R.drawable.btn_curve_small)
            binding.tvWep.background = resources.getDrawable(R.drawable.btn_blank)
            binding.tvWPA.background = resources.getDrawable(R.drawable.btn_blank)
        }

        binding.tvFacebookUrl.setOnClickListener {
            binding.tvFbUsername.setTextColor(resources.getColor(R.color.clr97))
            binding.tvFacebookUrl.setTextColor(resources.getColor(R.color.white))
            binding.edtFacebookId.setText("")
            binding.tvFacebookUrl.background = resources.getDrawable(R.drawable.btn_curve_small)
            binding.tvFbUsername.background = resources.getDrawable(R.drawable.btn_blank)
            when (position) {
                3 -> {
                    binding.tvIdName.text = getString(R.string.facebookUrl)
                }

                4 -> {
                    binding.tvIdName.text = getString(R.string.youtubeUrl)
                }

                12 -> {
                    binding.tvIdName.text = getString(R.string.paypalUrl)
                }

                13 -> {
                    binding.tvIdName.text = getString(R.string.instagramUrl)
                }

                15 -> {
                    binding.tvIdName.text = getString(R.string.twitterUrl)
                }

            }
        }

        binding.tvFbUsername.setOnClickListener {
            binding.tvFbUsername.background = resources.getDrawable(R.drawable.btn_curve_small)
            binding.tvFacebookUrl.background = resources.getDrawable(R.drawable.btn_blank)
            binding.tvFacebookUrl.setTextColor(resources.getColor(R.color.clr97))
            binding.tvFbUsername.setTextColor(resources.getColor(R.color.white))
            when (position) {
                3 -> {
                    binding.tvIdName.text = getString(R.string.facebookId)
                    binding.edtFacebookId.setText("facebook.com/")
                }

                4 -> {
                    binding.tvIdName.text = getString(R.string.youtubeId)
                    binding.edtFacebookId.setText("youtube.com/")
                }

                12 -> {
                    binding.tvIdName.text = getString(R.string.paypalLink)
                }

                13 -> {
                    binding.tvIdName.text = getString(R.string.instagramId)
                    binding.edtFacebookId.setText("instagram.com/")
                }

                15 -> {
                    binding.tvIdName.text = getString(R.string.twitterId)
                    binding.edtFacebookId.setText("twitter.com/")
                }
            }
            binding.edtFacebookId.setSelection(binding.edtFacebookId.text.length)
        }

        binding.btnLink.setOnClickListener {
           if(openLink){
               val uri =
                   Uri.parse(data) // missing 'http://' will cause crashed
               val intent = Intent(Intent.ACTION_VIEW, uri)
               startActivity(intent)
           } else {
               getData()
               if (data.isNotEmpty() || data.isNotBlank()) {
                   progressBar.visibility = View.VISIBLE
                   generateQRCode(data)
                   binding.btnLink.text = getString(R.string.open_link)
                   hideAll()
                   binding.deleteIcon.visibility = View.VISIBLE
                   binding.llQrCode.visibility = View.VISIBLE
                   if(position == 5){
                       binding.btnLink.visibility = View.VISIBLE
                   } else {
                       binding.btnLink.visibility = View.GONE
                   }
               } else {
                   Toast.makeText(requireContext(), "Please Enter required data", Toast.LENGTH_LONG)
                       .show()
               }
           }

        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.llDownload.setOnClickListener {
            bmp?.let { it1 -> storeImage(it1) }
        }

        val date =
            OnDateSetListener { _, year, month, day ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, day)
                updateLabel()
            }

        val dateEnd =
            OnDateSetListener { _, year, month, day ->
                myCalendarEndDate.set(Calendar.YEAR, year)
                myCalendarEndDate.set(Calendar.MONTH, month)
                myCalendarEndDate.set(Calendar.DAY_OF_MONTH, day)
                updateLabelEndDate()
            }

        binding.mcvStartDate.setOnClickListener {
            DatePickerDialog(
                requireContext(), date,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH], myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        binding.mcvEndDate.setOnClickListener {
            DatePickerDialog(
                requireContext(), dateEnd,
                myCalendarEndDate[Calendar.YEAR],
                myCalendarEndDate[Calendar.MONTH], myCalendarEndDate[Calendar.DAY_OF_MONTH]
            ).show()
        }

        binding.llFavourite.setOnClickListener {
            updateDb()
        }

        binding.deleteIcon.setOnClickListener {
            deleteRecord()
        }

        binding.llShare.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.setAction(Intent.ACTION_SEND)
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                name
            )
            sendIntent.setType("text/plain")
            startActivity(sendIntent)
        }

    }

    private fun deleteRecord() {
        val latestRecord = scanHistoryDao.getLatest()
        CoroutineScope(Dispatchers.IO).launch {
            scanHistoryDao.delete(latestRecord)
        }

        Toast.makeText(context, "Record deleted successfully", Toast.LENGTH_LONG).show()
        findNavController().popBackStack()
    }

    private fun updateLabelEndDate() {
        val myFormat = "MM/dd/yy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        binding.tvEndDate.text = dateFormat.format(myCalendarEndDate.time)
    }

    private fun updateLabel() {
        val myFormat = "MM/dd/yy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        binding.tvStartDate.text = dateFormat.format(myCalendar.time)
    }

    private fun getData() {
        data = ""
        when (position) {
            0 -> {
                data = binding.edtUrl.text.trim().toString()
            }

            1 -> {
                data = binding.edtClipboard.text.trim().toString()
            }

            2 -> {
                data =
                    binding.edtNetworkName.text.trim().toString() + binding.edtPassword.text.trim()
                        .toString()
            }

            3 -> {
                data = binding.edtFacebookId.text.trim().toString()
            }

            4 -> {
                data = binding.edtFacebookId.text.trim().toString()
            }

            5 -> {
                data = "https://api.whatsapp.com/send/?phone="+binding.countryCode.selectedCountryCode + binding.edtPhoneNumber.text.trim()
                    .toString()
            }

            6 -> {
                data = binding.edtClipboard.text.trim().toString()
            }

            7 -> {
                data =
                    binding.ccContactNumber.selectedCountryCode + binding.edtContactNumber.text.trim()
                        .toString() + binding.ccContactPhoneNumber.selectedCountryCode + binding.edtContactPhoneNumber.text.trim()
                        .toString() + binding.edtContactEmail.text.trim().toString()
            }

            8 -> {
                data = binding.countryCode.selectedCountryCode + binding.edtPhoneNumber.text.trim()
                    .toString()
            }

            9 -> {
                data = binding.edtEmail.text.trim().toString() + binding.edtSubject.text.trim()
                    .toString() + binding.edtContent.text.trim().toString()
            }

            10 -> {
                data =
                    binding.ccSMSPhoneNumber.selectedCountryCode + binding.edtSMSPhoneNumber.text.trim()
                        .toString() + binding.edtTextMassage.text.trim().toString()
            }

            11 -> {
                data = binding.edtMyCardName.text.trim()
                    .toString() + binding.edtMyCardEmail.text.trim()
                    .toString() + binding.ccMyCardPhoneNumber.selectedCountryCode + binding.edtMyCardPhoneNumber.text.trim()
                    .toString() + binding.edtMyCardNote.text.trim().toString()
            }

            12 -> {
                data = binding.edtFacebookId.text.trim().toString()
            }

            13 -> {
                data = binding.edtFacebookId.text.trim().toString()
            }

            14 -> {
                data = binding.countryCode.selectedCountryCode + binding.edtPhoneNumber.text.trim()
                    .toString()
            }

            15 -> {
                data = binding.edtFacebookId.text.trim().toString()
            }

            16 -> {
                data = binding.edtCalendarTitle.text.trim().toString() +
                        binding.edtCalendarLocation.text.trim().toString() +
                        binding.tvStartDate.text.trim().toString() +
                        binding.tvEndDate.text.trim().toString() +
                        binding.edtCalendarDescription.text.trim().toString()
            }

            17 -> {
                data = binding.edtArtistName.text.trim().toString() +
                        binding.edtSongName.text.trim().trim()
            }
        }
    }

    private fun setLayout(position: Int) {
        hideAll()
        binding.tvTitle.text = name
        when (position) {
            0 -> {
                binding.llWebsite.visibility = View.VISIBLE
                binding.edtUrl.requestFocus()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.edtUrl, InputMethodManager.SHOW_IMPLICIT)
            }

            1 -> {
                binding.llClipboard.visibility = View.VISIBLE
                binding.edtClipboard.requestFocus()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.edtClipboard, InputMethodManager.SHOW_IMPLICIT)
            }

            2 -> {
                binding.llWifi.visibility = View.VISIBLE
                binding.edtNetworkName.requestFocus()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.edtNetworkName, InputMethodManager.SHOW_IMPLICIT)
            }

            3 -> {
                binding.llUsernameUrl.visibility = View.VISIBLE
                binding.tvIdName.text = getString(R.string.facebookId)
                binding.edtFacebookId.setText("facebook.com/")
                binding.edtFacebookId.requestFocus()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.edtFacebookId, InputMethodManager.SHOW_IMPLICIT)
            }

            4 -> {
                binding.llUsernameUrl.visibility = View.VISIBLE
                binding.tvIdName.text = getString(R.string.youtubeId)
                binding.edtFacebookId.setText("youtube.com/")
                binding.edtFacebookId.requestFocus()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.edtFacebookId, InputMethodManager.SHOW_IMPLICIT)
            }

            5 -> {
                binding.llWhatsapp.visibility = View.VISIBLE
                binding.edtPhoneNumber.requestFocus()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.edtPhoneNumber, InputMethodManager.SHOW_IMPLICIT)

            }

            6 -> {
                binding.llClipboard.visibility = View.VISIBLE
                binding.tvClipboard.text = getString(R.string.text)
                binding.edtClipboard.requestFocus()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.edtClipboard, InputMethodManager.SHOW_IMPLICIT)
            }

            7 -> {
                binding.llContact.visibility = View.VISIBLE
                binding.edtContactNumber.requestFocus()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.edtContactNumber, InputMethodManager.SHOW_IMPLICIT)
            }

            8 -> {
                binding.llWhatsapp.visibility = View.VISIBLE
                binding.edtPhoneNumber.requestFocus()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.edtPhoneNumber, InputMethodManager.SHOW_IMPLICIT)
            }

            9 -> {
                binding.llEmail.visibility = View.VISIBLE
                binding.edtEmail.requestFocus()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.edtEmail, InputMethodManager.SHOW_IMPLICIT)
            }

            10 -> {
                binding.llSMS.visibility = View.VISIBLE
                binding.edtTextMassage.requestFocus()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.edtTextMassage, InputMethodManager.SHOW_IMPLICIT)
            }

            11 -> {
                binding.llMyCard.visibility = View.VISIBLE
                binding.edtMyCardName.requestFocus()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.edtMyCardName, InputMethodManager.SHOW_IMPLICIT)
            }

            12 -> {
                binding.llUsernameUrl.visibility = View.VISIBLE
                binding.tvIdName.text = getString(R.string.paypalLink)
                binding.edtFacebookId.requestFocus()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.edtFacebookId, InputMethodManager.SHOW_IMPLICIT)
            }

            13 -> {
                binding.llUsernameUrl.visibility = View.VISIBLE
                binding.tvIdName.text = getString(R.string.instagramId)
                binding.edtFacebookId.setText("instagram.com/")
                binding.edtFacebookId.requestFocus()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.edtFacebookId, InputMethodManager.SHOW_IMPLICIT)
            }

            14 -> {
                binding.llWhatsapp.visibility = View.VISIBLE
                binding.edtPhoneNumber.requestFocus()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.edtPhoneNumber, InputMethodManager.SHOW_IMPLICIT)
            }

            15 -> {
                binding.llUsernameUrl.visibility = View.VISIBLE
                binding.tvIdName.text = getString(R.string.twitter)
                binding.edtFacebookId.setText("X.com/")
                binding.edtFacebookId.requestFocus()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.edtFacebookId, InputMethodManager.SHOW_IMPLICIT)
            }

            16 -> {
                binding.llCalendar.visibility = View.VISIBLE
                binding.edtCalendarTitle.requestFocus()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.edtCalendarTitle, InputMethodManager.SHOW_IMPLICIT)
            }

            17 -> {
                binding.llSpotify.visibility = View.VISIBLE
                binding.edtArtistName.requestFocus()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.edtArtistName, InputMethodManager.SHOW_IMPLICIT)
            }

        }
        binding.edtFacebookId.setSelection(binding.edtFacebookId.text.length)
        binding.btnLink.text = getString(R.string.create)
    }

    private fun hideAll() {
        binding.llWebsite.visibility = View.GONE
        binding.llClipboard.visibility = View.GONE
        binding.llWifi.visibility = View.GONE
        binding.llUsernameUrl.visibility = View.GONE
        binding.llWhatsapp.visibility = View.GONE
        binding.llContact.visibility = View.GONE
        binding.llEmail.visibility = View.GONE
        binding.llSMS.visibility = View.GONE
        binding.llMyCard.visibility = View.GONE
        binding.llCalendar.visibility = View.GONE
        binding.llSpotify.visibility = View.GONE
    }

    @SuppressLint("SimpleDateFormat")
    private fun generateQRCode(name: String) {
        val sdf = SimpleDateFormat("MM/dd/yyyy HH:mm a")
        val currentDateAndTime = sdf.format(Date())

        bmp = BarcodeUtils.encodeBitmap(name, BarcodeFormat.QR_CODE, 400, 400)
        binding.ivQRCode.setImageBitmap(bmp)
        binding.tvLink.text = name
        binding.tvDate.text = currentDateAndTime
        openLink = true
        addDb(name, currentDateAndTime, BarcodeFormat.QR_CODE)
    }

    private fun addDb(name: String, date: String, format: BarcodeFormat) {
//        CoroutineScope(Dispatchers.IO).launch {
        val data: List<ScanHistory> =
            listOf(
                ScanHistory(
                    null,
                    name,
                    date,
                    false, false,
                    format
                )
            )
        scanHistoryDao.insertAll(data)
        progressBar.visibility = View.GONE
//        }
    }

    private fun updateDb() {
        val latestRecord = scanHistoryDao.getLatest()
        latestRecord.favourite = !latestRecord.favourite!!
        scanHistoryDao.update(latestRecord)
        if (latestRecord.favourite == true) {
            binding.ivFavourite.setImageResource(R.drawable.icon_favourite)
        } else {
            binding.ivFavourite.setImageResource(R.drawable.icon_favourite_unselect)
        }
//        Toast.makeText(context, "Record added to Favourite", Toast.LENGTH_LONG).show()
    }

    private fun storeImage(image: Bitmap) {
        val pictureFile: File? = getOutputMediaFile()
//        val dir = File(
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//            "MyFolder"
//        )
        try {
            val fos = FileOutputStream(pictureFile)
            image.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.close()
            Toast.makeText(requireContext(), "Image Saved Successfully", Toast.LENGTH_LONG).show()
        } catch (e: FileNotFoundException) {
            Log.d(TAG, "File not found: " + e.message)
            Toast.makeText(requireContext(), "File not found: " + e.message, Toast.LENGTH_LONG)
                .show()
        } catch (e: IOException) {
            Log.d(TAG, "Error accessing file: " + e.message)
            Toast.makeText(
                requireContext(),
                "Error accessing file: " + e.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /** Create a File for saving an image or video  */
    @SuppressLint("SimpleDateFormat")
    private fun getOutputMediaFile(): File? {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        val mediaStorageDir = File(
            ((Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                .toString() + "/Android/data/"
                    + activity?.applicationContext?.packageName
                    ).toString() + "/Files")
        )


        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }

        // Create a media file name
        val timeStamp: String = SimpleDateFormat("ddMMyyyy_HHmm").format(Date())
        val mediaFile: File
        val mImageName = "MI_$timeStamp.jpg"
        mediaFile = File(mediaStorageDir.path + File.separator + mImageName)
        return mediaFile
    }

}