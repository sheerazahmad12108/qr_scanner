package com.example.qrcode.fragments

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.example.qrcode.activities.LanguageActivity
import com.google.android.material.bottomsheet.BottomSheetDialog


class SettingsFragment : Fragment() {

    private lateinit var llAppLanguage: LinearLayout
    private lateinit var llShareApp: LinearLayout
    private lateinit var llPrivacy: LinearLayout
    private lateinit var llFeedback: LinearLayout
    private lateinit var llRateUs: LinearLayout
    private var ratingStartList = ArrayList<ImageView>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        llAppLanguage = view.findViewById(R.id.llAppLanguage)
        llShareApp = view.findViewById(R.id.llShareApp)
        llPrivacy = view.findViewById(R.id.llPrivacy)
        llFeedback = view.findViewById(R.id.llFeedback)
        llRateUs = view.findViewById(R.id.llRateUs)

        llAppLanguage.setOnClickListener {
            val intent = Intent(activity, LanguageActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        llShareApp.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.setAction(Intent.ACTION_SEND)
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Hey check out my app at: https://play.google.com/store/apps/details?id=com.bld.qrscanner"
            )
            sendIntent.setType("text/plain")
            startActivity(sendIntent)
        }

        llPrivacy.setOnClickListener {
            val uri =
                Uri.parse("https://ibrainrank.com/qr-privacy-policy/") // missing 'http://' will cause crashed
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        llFeedback.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_settings_to_feedbackFragment)
        }

        llRateUs.setOnClickListener {
//            openRateUs()
            launchMarket()
        }
    }

    @SuppressLint("InflateParams", "MissingInflatedId")
    private fun openRateUs() {
        ratingStartList.clear()
        val dialog = context?.let { BottomSheetDialog(it) }
        val bottomSheet = layoutInflater.inflate(R.layout.bottom_dialog_rate_us, null)
        val btnGotIt = bottomSheet.findViewById<TextView>(R.id.tvCancel)
        val btnDone = bottomSheet.findViewById<TextView>(R.id.tvDone)
        val ivStar1 = bottomSheet.findViewById<ImageView>(R.id.ivStar1)
        val ivStar2 = bottomSheet.findViewById<ImageView>(R.id.ivStar2)
        val ivStar3 = bottomSheet.findViewById<ImageView>(R.id.ivStar3)
        val ivStar4 = bottomSheet.findViewById<ImageView>(R.id.ivStar4)
        val ivStar5 = bottomSheet.findViewById<ImageView>(R.id.ivStar5)

        ratingStartList.add(ivStar1)
        ratingStartList.add(ivStar2)
        ratingStartList.add(ivStar3)
        ratingStartList.add(ivStar4)
        ratingStartList.add(ivStar5)
        btnGotIt.setOnClickListener {
            dialog?.dismiss()
        }

        btnDone.setOnClickListener {
            dialog?.dismiss()
        }

        ivStar1.setOnClickListener {
            filledStar(1)
            unFilledStar(1)
        }

        ivStar2.setOnClickListener {
            filledStar(2)
            unFilledStar(2)
        }

        ivStar3.setOnClickListener {
            filledStar(3)
            unFilledStar(3)
        }

        ivStar4.setOnClickListener {
            filledStar(4)
            unFilledStar(4)
        }

        ivStar5.setOnClickListener {
            filledStar(5)
        }

        dialog?.setContentView(bottomSheet)
        dialog?.show()
    }

    private fun filledStar(num: Int){
        for(i in 0..<num){
            ratingStartList[i].setImageResource(R.drawable.star_rating_filled)
        }
    }

    private fun unFilledStar(num: Int){
        for(i in num..<ratingStartList.size){
            ratingStartList[i].setImageResource(R.drawable.star_rating_blank)
        }
    }

    private fun launchMarket() {
        val uri = Uri.parse("market://details?id=" + requireActivity().packageName)
        val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(myAppLinkToMarket)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), " unable to find market app", Toast.LENGTH_LONG).show()
        }
    }

}