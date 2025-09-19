package com.example.qrcode.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.bld.qrscanner.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import java.util.Locale

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var bannerId: AdView
    private lateinit var progressBar: ProgressBar
    private var progressStatus = 0
    private val handler = Handler()

    @SuppressLint("UnsafeIntentLaunch")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        progressBar = findViewById(R.id.progressBar)

        MobileAds.initialize(this) {
            Log.d("Add", "onInitializationCompleted")
        }
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder().setTestDeviceIds(listOf("358074080311627")).build()
        )

        val adRequest = AdRequest.Builder().build()

        bannerId = findViewById(R.id.bannerAd)
//        bannerId.adUnitId = "ca-app-pub-3940256099942544/9214589741"
//        bannerId.setAdSize(AdSize.SMART_BANNER)

        bannerId.loadAd(adRequest)

        bannerId.adListener = object : AdListener() {
            override fun onAdClicked() {
                super.onAdClicked()
            }

            override fun onAdClosed() {
                super.onAdClosed()
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
            }

            override fun onAdImpression() {
                super.onAdImpression()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
            }

            override fun onAdOpened() {
                super.onAdOpened()
            }

            override fun onAdSwipeGestureClicked() {
                super.onAdSwipeGestureClicked()
            }
        }


        val preferences = getSharedPreferences("Settings", MODE_PRIVATE)
        val languageSelected = preferences.getString("app_lang", "")!!
        val locale = Locale(languageSelected)
        Locale.setDefault(locale)

        val configuration = Configuration()
        configuration.locale = locale
        baseContext.resources.updateConfiguration(
            configuration,
            baseContext.resources.displayMetrics
        )


        // Start long running operation in a background thread
        Thread {
            while (progressStatus < 100) {
                progressStatus += 1
                // Update the progress bar and display the
                //current value in the text view
                handler.post(Runnable {
                    progressBar.progress = progressStatus
                })
                try {
                    // Sleep for 200 milliseconds.
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
//            val intent = Intent(
//                this@SplashActivity,
//                HomeActivity::class.java
//            )
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
//            finish()
        }.start()

        Handler(Looper.getMainLooper()).postDelayed({
            progressStatus += 1
            progressBar.progress = progressStatus
            val onBoarding = preferences.getBoolean("on_boarding", false)
            val intent: Intent = if (onBoarding) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, OnboardingActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 10000)
    }

}