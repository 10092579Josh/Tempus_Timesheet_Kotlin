package com.example.opsc_part2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.opsc_part2.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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