package com.example.opsc_part2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edits)
        details()
        val homebtn = findViewById<ImageButton>(R.id.hometbtn)
        val breaksbtn = findViewById<ImageButton>(R.id.breakstbtn)
        val statsbtn = findViewById<ImageButton>(R.id.statstbtn)
        val settingsbtn = findViewById<ImageButton>(R.id.settingstbtn)
        val addbtn = findViewById<ImageButton>(R.id.addbtn)


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

        addbtn.setOnClickListener()
        {
            val tform = Intent(this, CatergoryForm::class.java)
            startActivity(tform)
            finish()

        }



    }


    fun details()
    {
        var username: EditText = findViewById(R.id.userName_text)
        var password: EditText = findViewById(R.id.passwordEditText)
        var conpassword: EditText = findViewById(R.id.confirmPasswordEditText)
        var name: EditText = findViewById(R.id.nameEditText)
        var surname: EditText = findViewById(R.id.surnameEditText)
        var email : EditText = findViewById(R.id.emailEditText)
        var savebutton: Button = findViewById(R.id.saveButton)

        username.setText(registrationActivty.SourceClass.datas[0][0].toString())
        password.setText(registrationActivty.SourceClass.datas[0][1].toString())
        name.setText(registrationActivty.SourceClass.datas[0][2].toString())
        surname.setText(registrationActivty.SourceClass.datas[0][3].toString())
        email.setText(registrationActivty.SourceClass.datas[0][4].toString())
        conpassword.setText(registrationActivty.SourceClass.datas[0][5].toString())
        savebutton.setOnClickListener() {


            if (password?.text.toString() != conpassword?.text.toString()) {
                var message = " passwords do not match"
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

            } else if (name?.text.toString().isEmpty()) {
                var message = " no name entered "
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            } else if (surname?.text.toString().isEmpty()) {
                var message = " no surname entered "
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            } else if (password?.text.toString().length < 7) {
                var message = " enter password is too short"
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            } else if (conpassword?.text.toString().length < 7) {
                var message = " confirm password is too short "
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

            } else if (email?.text.toString().isEmpty()) {
                var message = " no email entered "
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            } else { // move to the next screen if filled

                registrationActivty.SourceClass.datas[0][0] =
                    username?.text.toString().replace("\\s".toRegex(), "")
                registrationActivty.SourceClass.datas[0][1] =
                    password?.text.toString().replace("\\s".toRegex(), "")
                registrationActivty.SourceClass.datas[0][2] =
                    name?.text.toString().replace("\\s".toRegex(), "")
                registrationActivty.SourceClass.datas[0][3] =
                    surname?.text.toString().replace("\\s".toRegex(), "")
                registrationActivty.SourceClass.datas[0][4] =
                    email?.text.toString().replace("\\s".toRegex(), "")
                registrationActivty.SourceClass.datas[0][5] =
                    conpassword?.text.toString().replace("\\s".toRegex(), "")
                Toast.makeText(this, ":New Details Captured", Toast.LENGTH_SHORT).show()


            }
        }
    }
}