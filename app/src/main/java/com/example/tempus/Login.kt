package com.example.tempus

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.keyboardsurfer.android.widget.crouton.Crouton
import de.keyboardsurfer.android.widget.crouton.Style

class Login : AppCompatActivity() {
    private val e = Errors()
    private val emptypass = Crouton.makeText(this, e.NoNullsPassWord, Style.ALERT)
    private val emptyemail = Crouton.makeText(this, e.EmailValidationEmptyError, Style.ALERT)
    private val nofields = Crouton.makeText(this, e.NoDetailsEntered, Style.ALERT)


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loginLayoutId = intent.getIntExtra("login", 0)
        val loginLayout = layoutInflater.inflate(loginLayoutId, null)
        setContentView(loginLayout)

        permissions()
        signup()
        notifications()

        FirebaseApp.initializeApp(this)

    }

    fun notifications() {
        val pass: com.google.android.material.textfield.TextInputLayout =
            findViewById(R.id.passwords)
        val usernames1: com.google.android.material.textfield.TextInputLayout =
            findViewById(R.id.usernames)
        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val emailpopup = layoutInflater.inflate(R.layout.popup_window, null)
        val emailWindow = PopupWindow(
            emailpopup,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT

        )

        val crouton = Crouton.makeText(this, e.IllegalCharacterHash, Style.ALERT)
        usernames1.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()
                if (isValidString(email) && pass.editText.toString() != null) {


                    login()
                    emailWindow.dismiss()


                } else {

                    if (!emailWindow.isShowing) {
                        emailWindow.showAsDropDown(usernames1.editText, 0, 0)
                    }
                    if (email.contains("#")) {

                        crouton.show()


                    }


                }
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {// YET TO REUSED
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {//NO IDEA
            }

        })

        usernames1.editText?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {

                emailWindow.dismiss()
            }
        }


    }

    fun isValidString(str: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }


    fun signup() {
        val signup: Button = findViewById(R.id.signup)
        signup.setOnClickListener()
        {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
            finish()

        }

    }

    private fun login() {
        val usernames1: com.google.android.material.textfield.TextInputLayout =
            findViewById(R.id.usernames)
        val usernames = usernames1.editText
        val pass: com.google.android.material.textfield.TextInputLayout =
            findViewById(R.id.passwords)
        val pass2 = pass.editText

        val signButton: Button = findViewById(R.id.insign)
        signButton.setOnClickListener()
        {
            if (pass2?.text.isNullOrEmpty()) {
                emptypass.show()

            } else if (usernames?.text.isNullOrEmpty()) {

               emptyemail.show()

            } else if (pass2?.text.isNullOrEmpty() && usernames?.text.isNullOrEmpty()) {

                nofields.show()

            } else {

                val security = Firebase.auth


                security.signInWithEmailAndPassword(
                    usernames?.text.toString().trim(),
                    pass2?.text.toString().trim()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            val homepage = Intent(this, Home::class.java)
                            homepage.putExtra(
                                "home",
                                getIntent().getIntExtra("home", R.layout.home)
                            )
                            startActivity(homepage)
                            overridePendingTransition(0, 0)
                            finish()
                        } else {
                            val crouton = Crouton.makeText(this, e.LoginError, Style.ALERT)
                            crouton.show()
                        }
                    }


            }
        }

    }

    fun forgotpassword() {
// need front end UI
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun permissions() {
        val code = 0

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_MEDIA_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_AUDIO
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_VIDEO
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_MEDIA_LOCATION,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.POST_NOTIFICATIONS
                ), code
            )
        }
    }


}