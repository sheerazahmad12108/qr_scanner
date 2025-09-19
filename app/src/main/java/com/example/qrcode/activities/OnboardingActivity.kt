package com.example.qrcode.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.size
import androidx.viewpager2.widget.ViewPager2
import com.bld.qrscanner.R
import com.example.qrcode.onboarding.OnboardingItem
import com.example.qrcode.onboarding.OnboardingItemsAdapter


class OnboardingActivity : AppCompatActivity() {

    private lateinit var onboardingItemsAdapter: OnboardingItemsAdapter
    private lateinit var dotIndicator: LinearLayout
    private lateinit var btnNext: ImageView
    private lateinit var tvSkip: TextView
    private lateinit var onboardingViewPager: ViewPager2
    private val params = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    ).apply {
        setMargins(8, 0, 8, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(
            window,
            false
        )
        setContentView(R.layout.activity_onboarding)
        dotIndicator = findViewById(R.id.dot_indicator)
        btnNext = findViewById(R.id.btn_next)
        tvSkip = findViewById(R.id.tvSkip)
        setOnboardingItems()

        tvSkip.setOnClickListener {
            val intent = Intent(this, LanguageActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnNext.setOnClickListener {
            when (onboardingViewPager.currentItem) {
                0 -> {
                    onboardingViewPager.currentItem += 1
                }

                1 -> {
                    onboardingViewPager.currentItem += 1
                }

                2 -> {
                    val intent = Intent(this, LanguageActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun setOnboardingItems() {
        val itemList = listOf(
            OnboardingItem(
                R.drawable.onboarding_1,
                getString(R.string.onboarding_one_title),
                getString(R.string.onboarding_one_description)
            ),
            OnboardingItem(
                R.drawable.onboarding_2,
                getString(R.string.onboarding_second_title),
                getString(R.string.onboarding_second_description)
            ),
            OnboardingItem(
                R.drawable.onboarding_3,
                getString(R.string.onboarding_third_title),
                getString(R.string.onboarding_third_description)
            )
        )
        onboardingItemsAdapter = OnboardingItemsAdapter(
            itemList
        )
        onboardingViewPager = findViewById<ViewPager2>(R.id.onboardingViewPager)
        onboardingViewPager.adapter = onboardingItemsAdapter
        createDotIndicator(itemList.size)
        onboardingViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDotIndicator(position)
                updateNextButton(position)
            }
        })
    }

    private fun createDotIndicator(count: Int) {
        for (i in 0 until count) {
            val dot = ImageView(this)
            dot.setImageResource(R.drawable.dot_selector)
            dotIndicator.addView(dot, params)
        }
    }

    private fun updateDotIndicator(position: Int) {
        for (i in 0 until dotIndicator.size) {
            val dot = dotIndicator.getChildAt(i) as ImageView
            dot.isSelected = i == position
        }
    }

    private fun updateNextButton(position: Int) {
        when (position) {
            0 -> {
                btnNext.setImageResource(R.drawable.btn_onboarding_1)
            }

            1 -> {
                btnNext.setImageResource(R.drawable.btn_onboarding_2)
            }

            2 -> {
                btnNext.setImageResource(R.drawable.btn_onboarding_3)
            }
        }
    }

}