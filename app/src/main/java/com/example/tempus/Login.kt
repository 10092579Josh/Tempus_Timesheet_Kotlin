package com.example.tempus
//login file is the registration page cause i needed that first
//luca forget to add a go back to sign in if someone accidentally clicked sign up , please fix advise?
//opengl error but doesn't seem to affect anything
//forgot password can only be complete when database is done as it requires updating the database
// need a screen for forgot password as it will require username to find the user on the database, new password confirmkey password
// alternative would be to grab password from login screen but runs the risk of repeated data and mistaken pressing
// no delete user ? please advise
// no remember me ? please advise
// sign up colour needs to be changed due to can not see if it works
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
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
        var pass: com.google.android.material.textfield.TextInputLayout =
            findViewById(R.id.passwords)
        var usernames1: com.google.android.material.textfield.TextInputLayout =
            findViewById(R.id.usernames)
        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val emailpopup = layoutInflater.inflate(R.layout.popup_window, null)
        val emailWindow = PopupWindow(
            emailpopup,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT

        )


        usernames1.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()
                if (isValidString(email) && pass.editText.toString() != null) {


                    signin()
                    emailWindow.dismiss()


                } else {
                    // emailaddress is not valid
                    if (!emailWindow.isShowing) {
                        emailWindow.showAsDropDown(usernames1.editText, 0, 0)
                    }
                    if (email.contains("#")) {
                        // emailaddress contains invalid character '#'
                    }


                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })

        usernames1.editText?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // emailaddress EditText field lost focus
                emailWindow.dismiss()
            }
        }


    }

    fun isValidString(str: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }


    fun signup() {
        var signup: Button = findViewById(R.id.signup)
        signup.setOnClickListener()
        {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
            finish()

        }

    }

    fun signin() {
        var usernames1: com.google.android.material.textfield.TextInputLayout =
            findViewById(R.id.usernames)
        var usernames = usernames1.editText
        var pass: com.google.android.material.textfield.TextInputLayout =
            findViewById(R.id.passwords)
        var pass2 = pass.editText

        var signin: Button = findViewById(R.id.insign)
        signin.setOnClickListener()
        {
            if (pass2?.text.isNullOrEmpty()) {

                val crouton = Crouton.makeText(this,e.NoNullsPassWord , Style.ALERT)
                crouton.show()

            } else {

                val security = Firebase.auth

// Sign in with emailaddress and password
                security.signInWithEmailAndPassword(
                    usernames?.text.toString().trim(),
                    pass2?.text.toString().trim()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success
                            //user id if you wanna use it for cyber security
                            val appuser = security.currentUser
                            // shows the logged in user

                            val homepage = Intent(this@Login, Home::class.java)
                            homepage.putExtra(
                                "home",
                                getIntent().getIntExtra("home", R.layout.home)
                            )
                            startActivity(homepage)
                            overridePendingTransition(0, 0)
                            finish()
                        } else {
                            val crouton = Crouton.makeText(this,e.LoginError , Style.ALERT)
                            crouton.show()
                        }
                    }


            }
        }

    }

    fun forgotpassword() {

    }


    fun permissions() {
        val MY_PERMISSIONS_REQUEST_CODE = 0

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
                ), MY_PERMISSIONS_REQUEST_CODE
            )
        }
    }






}