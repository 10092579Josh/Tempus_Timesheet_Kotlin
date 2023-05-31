package com.example.opsc_part2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val homebtn = findViewById<ImageButton>(R.id.hometbtn)
        val breaksbtn = findViewById<ImageButton>(R.id.breakstbtn)
        val statsbtn = findViewById<ImageButton>(R.id.statstbtn)
        val settingsbtn = findViewById<ImageButton>(R.id.settingstbtn)

        homebtn.setOnClickListener({
            val homepage = Intent(this, MainActivity::class.java)
            startActivity(homepage)
            finish();
        })

        breaksbtn.setOnClickListener({
            val breakspage = Intent(this, BreaksActivity::class.java)
            startActivity(breakspage)
            finish();
        })

        statsbtn.setOnClickListener({
            val statspage = Intent(this, StatsActivity::class.java)
            startActivity(statspage)
            finish();
        })

        settingsbtn.setOnClickListener({
            val settingspage = Intent(this, SettingsActivity::class.java)
            startActivity(settingspage)
            finish();
        })

    }
}