package com.example.qrcode.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bld.qrscanner.R
import com.bld.qrscanner.databinding.FragmentScanBinding
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.zxing.BinaryBitmap
import com.google.zxing.LuminanceSource
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader


class ScanFragment : Fragment() {

    private lateinit var binding: FragmentScanBinding
    private val permissionRequestCode = 101
    private lateinit var permissionScreen: LinearLayout
    private lateinit var scanScreen: ConstraintLayout
    private lateinit var btnAllow: TextView
    private lateinit var codeScanner: CodeScanner
    private lateinit var seekBar: SeekBar
    private lateinit var ivMinus: ImageView
    private lateinit var ivPlus: ImageView
    private lateinit var llFlashLight: LinearLayout
    private lateinit var llGallery: LinearLayout
    private lateinit var llManual: LinearLayout
    private lateinit var rlFailed: RelativeLayout
    private lateinit var ivFlash: ImageView

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        val galleryUri = it
        try {
            val inputStream = context?.contentResolver?.openInputStream(galleryUri!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            val intArray = IntArray(bitmap.width * bitmap.height)
            bitmap.getPixels(intArray, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

            val source: LuminanceSource = RGBLuminanceSource(bitmap.width, bitmap.height, intArray)
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

            val reader = QRCodeReader()
            val result = reader.decode(binaryBitmap)
            val bundle = Bundle()
            bundle.putString("text", result.text)
            bundle.putSerializable("format", result.barcodeFormat)
            findNavController().navigate(
                R.id.action_navigation_scan_to_resultFragment,
                bundle
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScanBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permissionScreen = view.findViewById(R.id.permission_screen)
        scanScreen = view.findViewById(R.id.scan_screen)
        btnAllow = view.findViewById(R.id.btnAllow)
        llFlashLight = view.findViewById(R.id.llFlashLight)
        llGallery = view.findViewById(R.id.llGalleryLayout)
        llManual = view.findViewById(R.id.llManual)
        rlFailed = view.findViewById(R.id.rlFailed)
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        seekBar = view.findViewById(R.id.seekBar)
        ivMinus = view.findViewById(R.id.ivMinus)
        ivPlus = view.findViewById(R.id.ivPlus)
        ivFlash = view.findViewById(R.id.ivFlash)

        Handler(Looper.getMainLooper()).postDelayed({
            rlFailed.visibility = View.VISIBLE
        }, 10000)
        codeScanner = context?.let { CodeScanner(it, scannerView) }!!

        codeScanner.startPreview()

        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        codeScanner.decodeCallback = DecodeCallback {
            activity?.runOnUiThread {
                it.rawBytes
                Toast.makeText(context, "Scan Successfully", Toast.LENGTH_LONG).show()
                val bundle = Bundle()
                bundle.putString("text", it.text)
                bundle.putSerializable("format", it.barcodeFormat)
                findNavController().navigate(
                    R.id.action_navigation_scan_to_resultFragment,
                    bundle
                )
                Log.d("Scanner Success: ", "Successfully Scanned result ${it.text}")
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            activity?.runOnUiThread {
//                Toast.makeText(
//                    context, "Camera initialization error: ${it.message}",
//                    Toast.LENGTH_LONG
//                ).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                codeScanner.zoom = p1
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })

        ivMinus.setOnClickListener {
            seekBar.progress -= 10
        }
        ivPlus.setOnClickListener {
            seekBar.progress += 10
        }

        llFlashLight.setOnClickListener {
            codeScanner.isFlashEnabled = !codeScanner.isFlashEnabled
            if (codeScanner.isFlashEnabled) {
                ivFlash.setImageResource(R.drawable.icon_flash)
            } else {
                ivFlash.setImageResource(R.drawable.icon_flash_off)
            }
        }

        rlFailed.setOnClickListener {
            openHelp()
        }

        llGallery.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        llManual.setOnClickListener {
            showDialog()
        }

        if (checkPermission()) {
            scanScreen.visibility = View.VISIBLE
            permissionScreen.visibility = View.INVISIBLE
        } else {
            scanScreen.visibility = View.INVISIBLE
            permissionScreen.visibility = View.VISIBLE
            btnAllow.setOnClickListener {
                requestPermission()
            }
        }
    }

    @SuppressLint("InflateParams", "MissingInflatedId")
    private fun openHelp() {
        val dialog = context?.let { BottomSheetDialog(it) }
        val bottomSheet = layoutInflater.inflate(R.layout.bottom_dialog, null)

        val btnGotIt = bottomSheet.findViewById<TextView>(R.id.btnGotIt)
        btnGotIt.setOnClickListener {
            dialog?.dismiss()
        }

        dialog?.setContentView(bottomSheet)
        dialog?.show()
    }

    private fun checkPermission(): Boolean {
        // Permission is not granted
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.CAMERA
                )
            } == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            permissionRequestCode
        )
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("ObsoleteSdkInt")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            permissionRequestCode -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT)
//                    .show()
                val editor = activity?.getSharedPreferences("Settings", MODE_PRIVATE)?.edit()
                editor?.putBoolean("permission", true)
                editor?.apply()
                scanScreen.visibility = View.VISIBLE
                permissionScreen.visibility = View.INVISIBLE
                // main logic
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT)
                    .show()
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (context?.let {
//                            ContextCompat.checkSelfPermission(
//                                it,
//                                Manifest.permission.CAMERA
//                            )
//                        }
//                        != PackageManager.PERMISSION_GRANTED
//                    ) {
//                        showMessageOKCancel("You need to allow access permissions",
//                            DialogInterface.OnClickListener { dialog, which ->
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                    requestPermission()
//                                }
//                            })
//                    }
//                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun showDialog() {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.custom_layout)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val cancel: RelativeLayout? = dialog?.findViewById(R.id.rlCancel)
        cancel?.setOnClickListener {
            dialog.dismiss()
        }

        dialog?.show()
    }
}