package com.example.opsc_part2
//login file is the registration page cause i needed that first
//luca forget to add a go back to sign in if someone accidentally clicked sign up , please fix advise?
//opengl error but doesn't seem to affect anything
//forgot password can only be complete when database is done as it requires updating the database
// need a screen for forgot password as it will require username to find the user on the database, new password confirm password
// alternative would be to grab password from login screen but runs the risk of repeated data and mistaken pressing
// no delete user ? please advise
// no remember me ? please advise
// sign up colour needs to be changed due to can not see if it works
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        signup()
        signin()
        FirebaseApp.initializeApp(this)
    }


    fun signup()
    {
        var signup: Button = findViewById(R.id.signup)
        signup.setOnClickListener()
        {
            val intent = Intent(this, registrationActivty::class.java)
            startActivity(intent)
            finish()

        }

    }

    fun signin()
    { var usernames1: com.google.android.material.textfield.TextInputLayout = findViewById(R.id.usernames)
        var usernames = usernames1.editText
        var pass: com.google.android.material.textfield.TextInputLayout = findViewById(R.id.passwords)
        var pass2 = pass.editText

        var signin: Button = findViewById(R.id.insign)
        signin.setOnClickListener()
        {
            val auth = Firebase.auth

// Sign in with email and password
            auth.signInWithEmailAndPassword(usernames?.text.toString().trim(), pass2?.text.toString().trim())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success
                        val user = auth.currentUser
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                        var message  = "USER ${usernames?.text.toString()} HAS LOGGED IN "
                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                    } else {
                        // Sign in failed
                        Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show()
                    }
                }




        }

    }







}