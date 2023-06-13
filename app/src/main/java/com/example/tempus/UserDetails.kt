package com.example.tempus

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_details)
        details()
        val homebtn = findViewById<ImageButton>(R.id.hometbtn)
        val breaksbtn = findViewById<ImageButton>(R.id.breakstbtn)
        val statsbtn = findViewById<ImageButton>(R.id.statstbtn)
        val settingsbtn = findViewById<ImageButton>(R.id.settingstbtn)
        val addbtn = findViewById<ImageButton>(R.id.addbtn)


        homebtn.setOnClickListener {
            val homepage = Intent(this, Home::class.java)
            startActivity(homepage)
            finish();
        }

        breaksbtn.setOnClickListener {
            val breakspage = Intent(this, Breaks::class.java)
            startActivity(breakspage)
            finish();
        }

        statsbtn.setOnClickListener {
            val statspage = Intent(this, Statistics::class.java)
            startActivity(statspage)
            finish();
        }

        settingsbtn.setOnClickListener {
            val settingspage = Intent(this, AppSettings::class.java)
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

        username.setText(    AppSettings.preloads.usersname)
        password.setText(Registration.SourceClass.datas[0][1].toString())
        name.setText(  AppSettings.preloads.names )
        surname.setText(  AppSettings.preloads.surname)
        email.setText(  AppSettings.preloads.emails )
        conpassword.setText(Registration.SourceClass.datas[0][5].toString())





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

                val database = Firebase.database
                val userid = FirebaseAuth.getInstance().currentUser?.uid
                val myRef = database.getReference("users")
                myRef.child(userid.toString()).child("username").setValue( username?.text.toString().replace("\\s".toRegex(), ""))
                myRef.child(userid.toString()).child("password").setValue(  password?.text.toString().replace("\\s".toRegex(), ""))
                myRef.child(userid.toString()).child("confirm").setValue( conpassword?.text.toString().replace("\\s".toRegex(), ""))
                myRef.child(userid.toString()).child("confirm").setValue(   name?.text.toString().replace("\\s".toRegex(), ""))


                    surname?.text.toString().replace("\\s".toRegex(), "")

                    email?.text.toString().replace("\\s".toRegex(), "")


                Toast.makeText(this, ":New Details Captured", Toast.LENGTH_SHORT).show()


            }
        }
    }
}