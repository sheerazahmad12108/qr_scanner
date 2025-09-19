package com.example.qrcode.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bld.qrscanner.R
import com.bld.qrscanner.databinding.FragmentFeedbackBinding


class FeedbackFragment : Fragment() {

    private lateinit var binding: FragmentFeedbackBinding
    private var ratingStartList = ArrayList<ImageView>()
    private var btnEnable: Boolean = false
    private var starEnable: Boolean = false
    private var problemEncountered: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedbackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        ratingStartList.add(binding.ivStar1)
        ratingStartList.add(binding.ivStar2)
        ratingStartList.add(binding.ivStar3)
        ratingStartList.add(binding.ivStar4)
        ratingStartList.add(binding.ivStar5)

        binding.ivStar1.setOnClickListener {
            filledStar(1)
            unFilledStar(1)
            enableButton(btnEnable, starEnable, binding.edtFeedback)
        }

        binding.ivStar2.setOnClickListener {
            filledStar(2)
            unFilledStar(2)
            enableButton(btnEnable, starEnable, binding.edtFeedback)
        }

        binding.ivStar3.setOnClickListener {
            filledStar(3)
            unFilledStar(3)
            enableButton(btnEnable, starEnable, binding.edtFeedback)
        }

        binding.ivStar4.setOnClickListener {
            filledStar(4)
            unFilledStar(4)
            enableButton(btnEnable, starEnable, binding.edtFeedback)
        }

        binding.ivStar5.setOnClickListener {
            filledStar(5)
            enableButton(btnEnable, starEnable, binding.edtFeedback)
        }

        binding.mcvTooManyAds.setOnClickListener {
            problemEncountered = binding.tvTooManyAds.text.toString()
            btnEnable = true
            enableButton(true, starEnable, binding.edtFeedback)
            binding.mcvTooManyAds.setBackgroundResource(R.drawable.btn_bg)
            binding.tvTooManyAds.setTextColor(resources.getColor(R.color.white))

            binding.mcvSlowApp.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvSlow.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvScanningTakesLong.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvScanningTakesLong.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvScanningFailed.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvScanningFailed.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvInvalidScanResult.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvInvalidScanResult.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvOther.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvOther.setTextColor(resources.getColor(R.color.clr97))

        }

        binding.mcvSlowApp.setOnClickListener {
            problemEncountered = binding.tvSlow.text.toString()
            btnEnable = true
            enableButton(true, starEnable, binding.edtFeedback)
            binding.mcvTooManyAds.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvTooManyAds.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvSlowApp.setBackgroundResource(R.drawable.btn_bg)
            binding.tvSlow.setTextColor(resources.getColor(R.color.white))

            binding.mcvScanningTakesLong.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvScanningTakesLong.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvScanningFailed.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvScanningFailed.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvInvalidScanResult.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvInvalidScanResult.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvOther.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvOther.setTextColor(resources.getColor(R.color.clr97))

        }

        binding.mcvScanningTakesLong.setOnClickListener {
            problemEncountered = binding.tvScanningTakesLong.text.toString()
            btnEnable = true
            enableButton(true, starEnable, binding.edtFeedback)
            binding.mcvTooManyAds.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvTooManyAds.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvSlowApp.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvSlow.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvScanningTakesLong.setBackgroundResource(R.drawable.btn_bg)
            binding.tvScanningTakesLong.setTextColor(resources.getColor(R.color.white))

            binding.mcvScanningFailed.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvScanningFailed.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvInvalidScanResult.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvInvalidScanResult.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvOther.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvOther.setTextColor(resources.getColor(R.color.clr97))

        }

        binding.mcvScanningFailed.setOnClickListener {
            problemEncountered = binding.tvScanningFailed.text.toString()
            btnEnable = true
            enableButton(true, starEnable, binding.edtFeedback)
            binding.mcvTooManyAds.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvTooManyAds.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvSlowApp.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvSlow.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvScanningTakesLong.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvScanningTakesLong.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvScanningFailed.setBackgroundResource(R.drawable.btn_bg)
            binding.tvScanningFailed.setTextColor(resources.getColor(R.color.white))

            binding.mcvInvalidScanResult.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvInvalidScanResult.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvOther.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvOther.setTextColor(resources.getColor(R.color.clr97))

        }

        binding.mcvInvalidScanResult.setOnClickListener {
            problemEncountered = binding.tvInvalidScanResult.text.toString()
            btnEnable = true
            enableButton(true, starEnable, binding.edtFeedback)
            binding.mcvTooManyAds.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvTooManyAds.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvSlowApp.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvSlow.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvScanningTakesLong.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvScanningTakesLong.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvScanningFailed.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvScanningFailed.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvInvalidScanResult.setBackgroundResource(R.drawable.btn_bg)
            binding.tvInvalidScanResult.setTextColor(resources.getColor(R.color.white))

            binding.mcvOther.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvOther.setTextColor(resources.getColor(R.color.clr97))

        }

        binding.mcvOther.setOnClickListener {
            problemEncountered = binding.tvOther.text.toString()
            btnEnable = true
            enableButton(true, starEnable, binding.edtFeedback)
            binding.mcvTooManyAds.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvTooManyAds.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvSlowApp.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvSlow.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvScanningTakesLong.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvScanningTakesLong.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvScanningFailed.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvScanningFailed.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvInvalidScanResult.setBackgroundResource(R.drawable.btn_bg_white)
            binding.tvInvalidScanResult.setTextColor(resources.getColor(R.color.clr97))

            binding.mcvOther.setBackgroundResource(R.drawable.btn_bg)
            binding.tvOther.setTextColor(resources.getColor(R.color.white))

        }

        binding.edtFeedback.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                enableButton(btnEnable, starEnable, binding.edtFeedback)
            }

        })

        binding.tvSubmit.setOnClickListener {
            val email = Intent(
                Intent.ACTION_SENDTO
            )
//            email.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
            email.setType("text/email")
            email.putExtra(Intent.EXTRA_EMAIL, arrayOf("qrfeedback@ibrainrank.com"))
            email.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
            email.putExtra(
                Intent.EXTRA_TEXT,
                "Problem Encountered: $problemEncountered. \n More Details: ${
                    binding.edtFeedback.text.trim().toString()
                }."
            )
            startActivityForResult(email, 101)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            101 -> {
                findNavController().popBackStack()
            }
        }
    }

    private fun filledStar(num: Int) {
        starEnable = true
        for (i in 0..<num) {
            ratingStartList[i].setImageResource(R.drawable.star_rating_filled)
        }
    }

    private fun unFilledStar(num: Int) {
        for (i in num..<ratingStartList.size) {
            ratingStartList[i].setImageResource(R.drawable.star_rating_blank)
        }
    }

    private fun enableButton(btnEnable: Boolean, starEnable: Boolean, editText: EditText) {
        if (btnEnable && starEnable && editText.text.isNotEmpty()) {
            binding.tvSubmit.isEnabled = true
            binding.tvSubmit.setBackgroundResource(R.drawable.btn_curve)
            binding.tvSubmit.setTextColor(resources.getColor(R.color.white))
        } else {
            binding.tvSubmit.isEnabled = false
            binding.tvSubmit.setBackgroundResource(R.drawable.btn_curve_disable)
            binding.tvSubmit.setTextColor(resources.getColor(R.color.clr22))
        }
    }

}