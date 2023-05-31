package com.example.opsc_part2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
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
        signup()
        signin()

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
    fun signup()
    {
        var signup: Button = findViewById(R.id.signup)
        signup.setOnClickListener()
        {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

    }
    fun forgotpassword()
    {  var usernames1: com.google.android.material.textfield.TextInputLayout = findViewById(R.id.usernames)
        var usernames = usernames1.editText
        var pass: com.google.android.material.textfield.TextInputLayout = findViewById(R.id.passwords)
        var pass2 = usernames1.editText
        var forgotpassword: Button = findViewById(R.id.fpass)
        forgotpassword.setOnClickListener()
        {

            //do some database code to update the fields in question here
            //or
            //val intent = Intent(this, Login::class.java)
            //startActivity(intent)
            // create forgotten password fields
        }


    }

    fun signin()
    { var usernames1: com.google.android.material.textfield.TextInputLayout = findViewById(R.id.usernames)
        var usernames = usernames1.editText
        var pass: com.google.android.material.textfield.TextInputLayout = findViewById(R.id.passwords)
        var pass2 = pass.editText

        var  check1 = Login.SourceClass.datas[0].toString()
        var  check2 = Login.SourceClass.datas[1].toString()
        var signin: Button = findViewById(R.id.insign)
        signin.setOnClickListener()
        {

            // do database check and authentication here
            // read from and compare stored pass to entered pass
            if(pass2?.text.toString() == check2 && usernames?.text.toString() ==check1)
            {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }
            else if(pass2?.text.toString() !=check2 && usernames?.text.toString() != check1)
            {
                var message  = "incorrect password please try again "
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            }

            var message  =  check1
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }

    }


}