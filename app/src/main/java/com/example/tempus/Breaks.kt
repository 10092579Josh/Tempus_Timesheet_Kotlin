package com.example.tempus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class Breaks : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.breaks)

        val home = findViewById<ImageButton>(R.id.hometbtn)
        val breaks = findViewById<ImageButton>(R.id.breakstbtn)
        val stats = findViewById<ImageButton>(R.id.statstbtn)
        val settings = findViewById<ImageButton>(R.id.settingstbtn)
        val add = findViewById<ImageButton>(R.id.addbtn)

        home.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.putExtra("home", getIntent().getIntExtra("home",R.layout.home))
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()

        }

        breaks.setOnClickListener {
            val breakspage = Intent(this, Breaks::class.java)
            startActivity(breakspage)
            overridePendingTransition(0, 0)
            finish()
        }

        stats.setOnClickListener {
            val statspage = Intent(this, Statistics::class.java)
            startActivity(statspage)
            overridePendingTransition(0, 0)
            finish()
        }

        settings.setOnClickListener {
            val settingspage = Intent(this, AppSettings::class.java)
            startActivity(settingspage)
            overridePendingTransition(0, 0)
            finish()
        }
        add.setOnClickListener()
        {
            val taskform = Intent(this, TaskForm::class.java)
            startActivity(taskform)
            overridePendingTransition(0, 0)
            finish()

        }

    }
}