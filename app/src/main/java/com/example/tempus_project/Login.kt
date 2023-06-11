package com.example.tempus_project
//login file is the registration page cause i needed that first
//luca forget to add a go back to sign in if someone accidentally clicked sign up , please fix advise?
//opengl error but doesn't seem to affect anything
//forgot password can only be complete when database is done as it requires updating the database
// need a screen for forgot password as it will require username to find the user on the database, new password confirm password
// alternative would be to grab password from login screen but runs the risk of repeated data and mistaken pressing
// no delete user ? please advise
// no remember me ? please advise
// sign up colour needs to be changed due to can not see if it works
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.opsc_part2.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        signup()
        notifications()

        FirebaseApp.initializeApp(this)
    }
    fun notifications()
    {
        var usernames1: com.google.android.material.textfield.TextInputLayout = findViewById(R.id.usernames)
        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = layoutInflater.inflate(R.layout.popup_window, null)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )


        usernames1.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()
                if (isValidString(email)) {
                    // email is valid

                    signin()
                    popupWindow.dismiss()







                } else {
                    // email is not valid
                    if (!popupWindow.isShowing) {
                        popupWindow.showAsDropDown(usernames1.editText, 0, 0)
                    }
                    if (email.contains("#")) {
                        // email contains invalid character '#'
                    }
                    // add additional checks for other invalid characters here
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })

        usernames1.editText?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // email EditText field lost focus
                popupWindow.dismiss()
            } }


    }
    fun isValidString(str: String): Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
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
            // firebase authorisations
            val security = Firebase.auth

// Sign in with email and password
            security.signInWithEmailAndPassword(usernames?.text.toString().trim(), pass2?.text.toString().trim())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success
                        //user id if you wanna use it for cyber security
                        val appuser = security.currentUser
                        // shows the logged in user

                       // moves to the next page
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        // Sign in failed
                        Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show()
                    }
                }




        }

    }
    fun forgotpassword ()
    {

    }






}