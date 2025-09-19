package com.example.qrcode.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bld.qrscanner.R
import com.example.qrcode.language.LanguageItem
import com.example.qrcode.language.LanguageItemAdapter
import java.util.Locale


class LanguageActivity : AppCompatActivity() {
    private lateinit var languageItemAdapter: LanguageItemAdapter
    private lateinit var rvLanguage: RecyclerView
    private lateinit var listLanguage: ArrayList<LanguageItem>
    private lateinit var ivSelectLanguage: ImageView
    private var positionSelected: Int = 0
    private var languageSelected: String = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = this.resources.getColor(R.color.white)
        setContentView(R.layout.activity_language)

        val preferences = getSharedPreferences("Settings", MODE_PRIVATE)
        positionSelected = preferences.getInt("app_position", 0)
        languageSelected = preferences.getString("app_lang", "")!!

        setLocale(languageSelected, positionSelected)
        rvLanguage = findViewById(R.id.rv_language)
        ivSelectLanguage = findViewById(R.id.ivSelectLanguage)
        getList()
        setAdapter()

        ivSelectLanguage.setOnClickListener {
            val editor = getSharedPreferences("Settings", MODE_PRIVATE).edit()
            editor.putBoolean("on_boarding", true)
            editor.apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setAdapter() {

        languageItemAdapter = LanguageItemAdapter(listLanguage)
        rvLanguage.layoutManager = LinearLayoutManager(this)
        rvLanguage.adapter = languageItemAdapter

        languageItemAdapter.setSingleSelection(positionSelected)
        // Applying OnClickListener to our Adapter
        languageItemAdapter.setOnClickListener(object :
            LanguageItemAdapter.OnClickListener {
            override fun onClick(position: Int, model: LanguageItem?) {
                when (position) {
                    0 -> {
                        setLocale("en", position)
                        recreate()
                    }

                    1 -> {
                        setLocale("ar", position)
                        recreate()
                    }

                    2 -> {
                        setLocale("af", position)
                        recreate()
                    }

                    3 -> {
                        setLocale("zh", position)
                        recreate()
                    }

                    4 -> {
                        setLocale("sv", position)
                        recreate()
                    }

                    5 -> {
                        setLocale("de", position)
                        recreate()
                    }

                    6 -> {
                        setLocale("fr", position)
                        recreate()
                    }

                    7 -> {
                        setLocale("hi", position)
                        recreate()
                    }

                    8 -> {
                        setLocale("es", position)
                        recreate()
                    }

                    9 -> {
                        setLocale("bn", position)
                        recreate()
                    }

                    10 -> {
                        setLocale("ru", position)
                        recreate()
                    }

                    11 -> {
                        setLocale("pt", position)
                        recreate()
                    }

                    12 -> {
                        setLocale("ur", position)
                        recreate()
                    }

                    13 -> {
                        setLocale("in", position)
                        recreate()
                    }

                    14 -> {
                        setLocale("ja", position)
                        recreate()
                    }

                    15 -> {
                        setLocale("sw", position)
                        recreate()
                    }

                    16 -> {
                        setLocale("tr", position)
                        recreate()
                    }

                    17 -> {
                        setLocale("ko", position)
                        recreate()
                    }

                    18 -> {
                        setLocale("vi", position)
                        recreate()
                    }

                }
//                Toast.makeText(applicationContext, "Clicked Item: $position", Toast.LENGTH_LONG)
//                    .show()
            }
        })
    }

    private fun getList() {
        listLanguage = ArrayList()
        listLanguage.add(LanguageItem(R.drawable.united_states, getString(R.string.english)))
        listLanguage.add(LanguageItem(R.drawable.saudi_arabia, getString(R.string.arabic)))
        listLanguage.add(LanguageItem(R.drawable.south_africa, getString(R.string.afrikaans)))
        listLanguage.add(LanguageItem(R.drawable.china, getString(R.string.china)))
        listLanguage.add(LanguageItem(R.drawable.denmark, getString(R.string.danish)))
        listLanguage.add(LanguageItem(R.drawable.germany, getString(R.string.dutch)))
        listLanguage.add(LanguageItem(R.drawable.france, getString(R.string.french)))
        listLanguage.add(LanguageItem(R.drawable.india, getString(R.string.hindi)))
        listLanguage.add(LanguageItem(R.drawable.spain, getString(R.string.spanish)))
        listLanguage.add(LanguageItem(R.drawable.bangladesh, getString(R.string.bengali)))
        listLanguage.add(LanguageItem(R.drawable.russia, getString(R.string.russian)))
        listLanguage.add(LanguageItem(R.drawable.portugal, getString(R.string.portuguese)))
        listLanguage.add(LanguageItem(R.drawable.pakistan, getString(R.string.urdu)))
        listLanguage.add(LanguageItem(R.drawable.indonesia, getString(R.string.indonesian)))
        listLanguage.add(LanguageItem(R.drawable.japan, getString(R.string.japanese)))
        listLanguage.add(LanguageItem(R.drawable.comoros, getString(R.string.swahili)))
        listLanguage.add(LanguageItem(R.drawable.turkey, getString(R.string.turkish)))
        listLanguage.add(LanguageItem(R.drawable.south_korea, getString(R.string.korean)))
        listLanguage.add(LanguageItem(R.drawable.vietnam, getString(R.string.vietnamese)))
    }

    @SuppressLint("CommitPrefEdits")
    private fun setLocale(language: String, position: Int) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val configuration = Configuration()
        configuration.locale = locale
        baseContext.resources.updateConfiguration(
            configuration,
            baseContext.resources.displayMetrics
        )

        val editor = getSharedPreferences("Settings", MODE_PRIVATE).edit()
        editor.putString("app_lang", language)
        editor.putInt("app_position", position)
        editor.apply()
    }
}