package com.example.tempus_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.opsc_part2.R

class BreaksActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breaks)

        val home = findViewById<ImageButton>(R.id.hometbtn)
        val breaks = findViewById<ImageButton>(R.id.breakstbtn)
        val stats = findViewById<ImageButton>(R.id.statstbtn)
        val settings = findViewById<ImageButton>(R.id.settingstbtn)
        val add = findViewById<ImageButton>(R.id.addbtn)

        home.setOnClickListener {
            val homepage = Intent(this, MainActivity::class.java)
            startActivity(homepage)
            finish()
        }

        breaks.setOnClickListener {
            val breakspage = Intent(this, BreaksActivity::class.java)
            startActivity(breakspage)
            finish()
        }

        stats.setOnClickListener {
            val statspage = Intent(this, StatsActivity::class.java)
            startActivity(statspage)
            finish()
        }

        settings.setOnClickListener {
            val settingspage = Intent(this, SettingsActivity::class.java)
            startActivity(settingspage)
            finish()
        }
        add.setOnClickListener()
        {
            val taskform = Intent(this, TaskForm::class.java)
            startActivity(taskform)
            finish()

        }

    }
}