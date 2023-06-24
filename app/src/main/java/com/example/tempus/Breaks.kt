package com.example.tempus

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class Breaks : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.breaks)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@Breaks, Home::class.java)
                intent.putExtra("home", getIntent().getIntExtra("home", R.layout.home))
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()


            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        security()
        val home = findViewById<ImageButton>(R.id.hometbtn)
        val breaks = findViewById<ImageButton>(R.id.breakstbtn)
        val stats = findViewById<ImageButton>(R.id.statstbtn)
        val settings = findViewById<ImageButton>(R.id.settingstbtn)
        val add = findViewById<ImageButton>(R.id.addbtn)
        // Linking the buttons


        home.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.putExtra("home", getIntent().getIntExtra("home", R.layout.home))
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


    private fun security() {

        val auth = FirebaseAuth.getInstance()
        auth.addAuthStateListener { firebaseAuth ->
            when (firebaseAuth.currentUser) {
                null -> {

                    val sharedPreferences =
                        getSharedPreferences("preferences", Context.MODE_PRIVATE)
                    sharedPreferences.edit().putBoolean("isFirstLogin", true).apply()
                    AppSettings.Preloads.userSName = null
                    val intent = Intent(this@Breaks, Login::class.java)
                    intent.putExtra("login", R.layout.login)
                    overridePendingTransition(0, 0)
                    startActivity(intent)

                }
            }
        }

        val user = FirebaseAuth.getInstance().currentUser
        user?.reload()?.addOnCompleteListener { task ->
            when {
                task.isSuccessful -> {
                    // stuff to do

                }

                else -> {
                    when (val exception = task.exception) {
                        is FirebaseAuthInvalidUserException -> {
                            when (exception.errorCode) {
                                "ERROR_USER_NOT_FOUND" -> {
                                    val sharedPreferences =
                                        getSharedPreferences("preferences", Context.MODE_PRIVATE)
                                    sharedPreferences.edit().putBoolean("isFirstLogin", true)
                                        .apply()
                                    AppSettings.Preloads.userSName = null
                                    val intent = Intent(this@Breaks, Login::class.java)
                                    intent.putExtra("login", R.layout.login)
                                    overridePendingTransition(0, 0)
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}