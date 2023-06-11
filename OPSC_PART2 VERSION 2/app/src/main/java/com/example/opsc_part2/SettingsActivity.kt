package com.example.opsc_part2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        var name: EditText = findViewById(R.id.nameEditText)
        var surname: EditText = findViewById(R.id.surnameEditText)
        var username: EditText = findViewById(R.id.userName_text)
        var names = registrationActivty.SourceClass.datas[0][2].toString()
        name.setText(names.toString())
        var unames = registrationActivty.SourceClass.datas[0][0].toString()
        username.setText(unames.toString())
        surname.setText(registrationActivty.SourceClass.datas[0][3].toString())
        var email : EditText = findViewById(R.id.emailEditText)
        email.setText(registrationActivty.SourceClass.datas[0][4].toString())

        val homebtn = findViewById<ImageButton>(R.id.hometbtn)
        val breaksbtn = findViewById<ImageButton>(R.id.breakstbtn)
        val statsbtn = findViewById<ImageButton>(R.id.statstbtn)
        val settingsbtn = findViewById<ImageButton>(R.id.settingstbtn)
       val confirm  = findViewById<Button>(R.id.editButton)
        val addbtn = findViewById<ImageButton>(R.id.addbtn)
        val logout = findViewById<Button>(R.id.logoutButton)
        homebtn.setOnClickListener {
            val homepage = Intent(this, MainActivity::class.java)
            startActivity(homepage)
            finish();
        }

        breaksbtn.setOnClickListener {
            val breakspage = Intent(this, BreaksActivity::class.java)
            startActivity(breakspage)
            finish();
        }

        statsbtn.setOnClickListener {
            val statspage = Intent(this, StatsActivity::class.java)
            startActivity(statspage)
            finish();
        }

        settingsbtn.setOnClickListener {
            val settingspage = Intent(this, SettingsActivity::class.java)
            startActivity(settingspage)
            finish();
        }
        confirm.setOnClickListener()
         {   val details = Intent(this, EditDetails::class.java)
             startActivity(details)
             finish();

         }

        addbtn.setOnClickListener()
        {
            val tform = Intent(this, TaskForm::class.java)
            startActivity(tform)
            finish()

        }
        logout.setOnClickListener()
        {
            val Logout = Intent(this, Login::class.java)
            startActivity(Logout)
            finish();
            var username: EditText = findViewById(R.id.userName_text)
            var message = " ${username.text.toString()} HAS LOGGED OUT!"
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }

    }
}