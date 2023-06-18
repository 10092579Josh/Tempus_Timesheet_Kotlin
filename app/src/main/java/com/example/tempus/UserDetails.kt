package com.example.tempus

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import de.keyboardsurfer.android.widget.crouton.Crouton
import de.keyboardsurfer.android.widget.crouton.Style

class UserDetails : AppCompatActivity() {
    private val e = Errors()
    private val M = messages()
    private val emailEmpty = Crouton.makeText(this, e.EmailValidationEmptyError, Style.ALERT)
    private val passwordEmpty = Crouton.makeText(this, e.PasswordCantBeEmpty, Style.ALERT)
    private val confirmEmpty = Crouton.makeText(this, e.ConfirmPasswordCantBeEmpty, Style.ALERT)
    private val passwordMatch = Crouton.makeText(this, e.PasswordNotMatch, Style.ALERT)
    private val validMessage = Crouton.makeText(this, M.ConfirmedLogin, Style.INFO)
    private val Returns = Crouton.makeText(this, e.NewSignInRequired, Style.INFO)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_details)
        FirebaseApp.initializeApp(this)
        Security()
        details()
        val homebtn = findViewById<ImageButton>(R.id.hometbtn)
        val breaksbtn = findViewById<ImageButton>(R.id.breakstbtn)
        val statsbtn = findViewById<ImageButton>(R.id.statstbtn)
        val settingsbtn = findViewById<ImageButton>(R.id.settingstbtn)
        val addbtn = findViewById<ImageButton>(R.id.addbtn)


        homebtn.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.putExtra("home", getIntent().getIntExtra("home", R.layout.home))
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        breaksbtn.setOnClickListener {
            val breakspage = Intent(this, Breaks::class.java)
            startActivity(breakspage)
            overridePendingTransition(0, 0)
            finish()
        }

        statsbtn.setOnClickListener {
            val statspage = Intent(this, Statistics::class.java)
            startActivity(statspage)
            overridePendingTransition(0, 0)
            finish()
        }

        settingsbtn.setOnClickListener {
            val settingspage = Intent(this, AppSettings::class.java)
            startActivity(settingspage)
            overridePendingTransition(0, 0)
            finish()
        }

        addbtn.setOnClickListener {
            val tform = Intent(this, CatergoryForm::class.java)
            startActivity(tform)
            overridePendingTransition(0, 0)
            finish()

        }


    }

    fun Security() {

        val auth = FirebaseAuth.getInstance()
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {

                val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
                sharedPreferences.edit().putBoolean("isFirstLogin", true).apply()
                AppSettings.preloads.usersname = null
                val intent = Intent(this@UserDetails, Login::class.java)
                intent.putExtra("login", R.layout.login)
                overridePendingTransition(0, 0)
                startActivity(intent)

            }
        }

        val user = FirebaseAuth.getInstance().currentUser
        user?.reload()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {

            } else {
                val exception = task.exception
                if (exception is FirebaseAuthInvalidUserException) {
                    val errorCode = exception.errorCode
                    if (errorCode == "ERROR_USER_NOT_FOUND") {
                        val sharedPreferences =
                            getSharedPreferences("preferences", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putBoolean("isFirstLogin", true).apply()
                        AppSettings.preloads.usersname = null
                        val intent = Intent(this@UserDetails, Login::class.java)
                        intent.putExtra("login", R.layout.login)
                        overridePendingTransition(0, 0)
                        startActivity(intent)
                    }
                }
            }
        }

    }

    fun details() {
        var username: EditText = findViewById(R.id.userName_text)
        var password: EditText = findViewById(R.id.passwordEditText)
        var conpassword: EditText = findViewById(R.id.confirmPasswordEditText)
        var name: EditText = findViewById(R.id.nameEditText)
        var surname: EditText = findViewById(R.id.surnameEditText)
        var email: EditText = findViewById(R.id.emailEditText)
        var savebutton: Button = findViewById(R.id.saveButton)

        username.setText(AppSettings.preloads.usersname)
        password.setText(AppSettings.preloads.pass)
        name.setText(AppSettings.preloads.names)
        surname.setText(AppSettings.preloads.surname)
        email.setText(AppSettings.preloads.emails)
        conpassword.setText(AppSettings.preloads.conpass)





        savebutton.setOnClickListener {


            if (password.text.toString() != conpassword.text.toString()) {
                var message = " passwords do not match"
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

            } else if (name.text.toString().isEmpty()) {
                var message = " no firstname entered "
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            } else if (surname.text.toString().isEmpty()) {
                var message = " no surname entered "
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            } else if (password.text.toString().length < 7) {
                var message = " enter password is too short"
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            } else if (conpassword.text.toString().length < 7) {
                var message = " confirmkey password is too short "
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

            } else if (email.text.toString().isEmpty()) {
                var message = " no emailaddress entered "
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            } else { // move to the next screen if filled

                val database = Firebase.database
                val userid = FirebaseAuth.getInstance().currentUser?.uid
                val myRef = database.getReference("UserDetails")

                val user = FirebaseAuth.getInstance().currentUser
                user?.let {
                    it.updateEmail((email.text.toString().replace("\\s".toRegex(), "")))
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                myRef.child(userid.toString()).child("displayname").setValue(
                                    username.text.toString().replace("\\s".toRegex(), "")
                                )
                                myRef.child(userid.toString()).child("password").setValue(
                                    password.text.toString().replace("\\s".toRegex(), "")
                                )
                                myRef.child(userid.toString()).child("confirmkey").setValue(
                                    conpassword.text.toString().replace("\\s".toRegex(), "")
                                )
                                myRef.child(userid.toString()).child("firstname")
                                    .setValue(name.text.toString().replace("\\s".toRegex(), ""))
                                myRef.child(userid.toString()).child("surname")
                                    .setValue(surname.text.toString().replace("\\s".toRegex(), ""))
                                myRef.child(userid.toString()).child("emailaddress")
                                    .setValue(email.text.toString().replace("\\s".toRegex(), ""))

                                Toast.makeText(
                                    this,
                                    "${username.text}:New Details Captured",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                            }
                        }.addOnFailureListener { exception ->
                            val errorCode = (exception as? FirebaseAuthException)?.errorCode
                            Log.d("emailerror", errorCode.toString())
                            if (errorCode == "ERROR_REQUIRES_RECENT_LOGIN") {
                                Authorise()

                            } else if (errorCode == "ERROR_USER_TOKEN_EXPIRED") {
                                Returns.show()
                                FirebaseAuth.getInstance().signOut()


                                var message = " ${AppSettings.preloads.usersname} HAS LOGGED OUT!"
                                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                                val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
                                sharedPreferences.edit().putBoolean("isFirstLogin", true).apply()
                                AppSettings.preloads.usersname = null
                                val intent = Intent(this@UserDetails, Login::class.java)
                                intent.putExtra("login", R.layout.login)
                                overridePendingTransition(0, 0)
                                startActivity(intent)


                            } else {

                            }
                        }

                    it.updatePassword(password.text.toString().replace("\\s".toRegex(), ""))
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                Toast.makeText(
                                    this,
                                    "${username.text}:New Details Captured",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }.addOnFailureListener { exception ->
                            val errorCode = (exception as? FirebaseAuthException)?.errorCode
                            Log.d("emailerror", errorCode.toString())
                            if (errorCode == "ERROR_REQUIRES_RECENT_LOGIN") {
                                Authorise()

                            } else if (errorCode == "ERROR_USER_TOKEN_EXPIRED") {
                                Returns.show()
                                FirebaseAuth.getInstance().signOut()


                                var message = " ${AppSettings.preloads.usersname} HAS LOGGED OUT!"
                                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                                val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
                                sharedPreferences.edit().putBoolean("isFirstLogin", true).apply()
                                AppSettings.preloads.usersname = null
                                val intent = Intent(this@UserDetails, Login::class.java)
                                intent.putExtra("login", R.layout.login)
                                overridePendingTransition(0, 0)
                                startActivity(intent)

                            } else {

                            }
                        }
                }


            }
        }
    }

    private var isDialogOpen = false
    private fun Authorise() {

        if (!isDialogOpen) {
            isDialogOpen = true
            val builder = AlertDialog.Builder(this)

            val titleView = layoutInflater.inflate(R.layout.authenticate_title, null)
            builder.setCustomTitle(titleView)

            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL

            // Set up the username input
            val emailverification = EditText(this)
            emailverification.hint = "Email"
            emailverification.inputType =
                InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS or InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS
            layout.addView(emailverification)

            // Set up the password input
            val passwordInput = EditText(this)
            passwordInput.hint = "password"
            passwordInput.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            layout.addView(passwordInput)
            val confirmInput = EditText(this)
            confirmInput.hint = "password"
            confirmInput.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            layout.addView(confirmInput)

            builder.setView(layout)

            with(builder) {
                setNegativeButtonIcon(
                    ContextCompat.getDrawable(
                        context, R.drawable.baseline_cancel_presentation_24
                    )
                ).setNegativeButton("CLOSE") { _, _ ->
                    Log.d("MyTag", "closing")
                    val alertDialog = builder.create()
                    alertDialog.dismiss()
                    Log.d("MyTag", "closed")
                    isDialogOpen = false
                    Log.d("MyTag", "$isDialogOpen")
                }
                setPositiveButtonIcon(
                    ContextCompat.getDrawable(
                        context, R.drawable.baseline_check_box_24
                    )
                ).setPositiveButton("SUBMIT") { _, _ ->
                    val username = emailverification.text.toString().trim()
                    val password = confirmInput.text.toString().trim()
                    val verify = username + password
                    val database = Firebase.database
                    val userid = FirebaseAuth.getInstance().currentUser?.uid



                    if (emailverification.text.isNullOrEmpty()) {

                        emailEmpty.show()


                    } else if (confirmInput.text.isNullOrEmpty()) {
                        confirmEmpty.show()

                    } else if (passwordInput.text.isNullOrEmpty()) {
                        passwordEmpty.show()

                    } else if (emailverification.text.isNullOrEmpty() && confirmInput.text.isNullOrEmpty()) {
                        emailEmpty.show()
                        confirmEmpty.show()


                    } else if (passwordInput.text.toString() != confirmInput.text.toString()) {
                        passwordMatch.show()


                    } else {

                        val user = FirebaseAuth.getInstance().currentUser
                        val credential = EmailAuthProvider.getCredential(
                            emailverification.text.toString().trim(),
                            confirmInput.text.toString().trim()
                        )
                        user?.reauthenticate(credential)?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                validMessage.show()


                            } else {
                                // Failed to re-authenticate user
                            }
                        }

                    }
                    isDialogOpen = false
                }
            }.create()

            val alertDialog = builder.create()
            alertDialog.show()
            val button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
            val layoutParams = button.layoutParams as LinearLayout.LayoutParams
            with(button) {

                setPadding(0, 0, 20, 0)
                setTextColor(Color.WHITE)
                setBackgroundColor(Color.WHITE)

                layoutParams.weight = 10f
                button.layoutParams = layoutParams
            }
            alertDialog.show()
            val buttons = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            val layoutParams2 = buttons.layoutParams as LinearLayout.LayoutParams
            with(buttons) {
                setPadding(250, 0, 20, 0)
                setTextColor(Color.WHITE)
                layoutParams2.weight = 10f
                buttons.layoutParams = layoutParams2
            }

            builder.setOnCancelListener { isDialogOpen = false }
        }

    }
}