package com.example.tempus

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import de.keyboardsurfer.android.widget.crouton.Crouton
import de.keyboardsurfer.android.widget.crouton.Style


class Registration : AppCompatActivity() {
    private val e = Errors()
    private val emptyEmail = Crouton.makeText(this, e.emailValidationEmptyError, Style.ALERT)
    private val noUserName = Crouton.makeText(this, e.emptyUserName, Style.ALERT)
    private val noMatchPass = Crouton.makeText(this, e.passwordNotMatch, Style.ALERT)
    private val confirmPassTooShort = Crouton.makeText(this, e.confirmPasswordTooShort, Style.ALERT)
    private val passTooShort = Crouton.makeText(this, e.passwordTooShort, Style.ALERT)
    private val noSName = Crouton.makeText(this, e.noSName, Style.ALERT)
    private val noFName = Crouton.makeText(this, e.noFName, Style.ALERT)
    private val emailRegAlready = Crouton.makeText(this, e.regEmailError, Style.ALERT)
    private val hashCharacter = Crouton.makeText(this, e.illegalCharacterHash, Style.ALERT)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration)
        val loginBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@Registration, Login::class.java)
                intent.putExtra("login", R.layout.login)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()


            }
        }
        onBackPressedDispatcher.addCallback(this, loginBack)
        notifications()
        input()
        FirebaseApp.initializeApp(this)

    }

    private fun notifications() {
        val back: ImageButton = findViewById(R.id.back_btn)
        back.setOnClickListener {

            val loginpage = Intent(this, Login::class.java)
            loginpage.putExtra("login", R.layout.login)


            startActivity(loginpage)
            finish()

        }
        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val regPopupView = layoutInflater.inflate(R.layout.popup_window, null)
        val newPopupWindow = PopupWindow(
            regPopupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val email1: com.google.android.material.textfield.TextInputLayout = findViewById(R.id.email)
        email1.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()
                when {
                    check(email) -> {

                        newPopupWindow.dismiss()
                        input()


                    }

                    else -> {

                        when {
                            !newPopupWindow.isShowing -> {
                                newPopupWindow.showAsDropDown(email1.editText, 0, 0)
                            }
                        }
                        when {
                            email.contains("#") -> {
                                hashCharacter.show()

                            }
                        }

                    }
                }
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {// stuff to do
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) { // stuff to do


            }

        })

        email1.editText?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {

                newPopupWindow.dismiss()
            }
        }


    }

    fun check(str: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }


    fun input() { //variables


        val submitting: Button = findViewById(R.id.sub)
        val name1: com.google.android.material.textfield.TextInputLayout =
            findViewById(R.id.firstName)
        val names = name1.editText

        val surname1: com.google.android.material.textfield.TextInputLayout =
            findViewById(R.id.surname)
        val surnames = surname1.editText
        val username1: com.google.android.material.textfield.TextInputLayout =
            findViewById(R.id.usernames)
        val user2 = username1.editText
        val password1: com.google.android.material.textfield.TextInputLayout =
            findViewById(R.id.enterPassword)
        val pass = password1.editText
        val confirmPassword: com.google.android.material.textfield.TextInputLayout =
            findViewById(R.id.confirm_password)
        val pass2 = confirmPassword.editText
        val email1: com.google.android.material.textfield.TextInputLayout = findViewById(R.id.email)
        val emails = email1.editText





        submitting.setOnClickListener()

        {

            //action statements tp check fields if empty
            when {
                names?.text.toString().isNullOrEmpty() -> {
                    noFName.show()
                }

                surnames?.text.toString().isEmpty() -> {
                    noSName.show()
                }

                user2?.text.toString().isEmpty() -> {
                    noUserName.show()
                }

                pass?.text.toString().length < 7 -> {
                    passTooShort.show()
                }

                pass2?.text.toString().length < 7 -> {
                    confirmPassTooShort.show()

                }

                emails?.text.toString().isEmpty() -> {
                    emptyEmail.show()
                }

                pass?.text.toString() != pass2?.text.toString() -> {
                    noMatchPass.show()

                }

                else -> { // move to the next screen if filled

                    val database = FirebaseDatabase.getInstance()
                    val myRef = database.getReference("UserDetails")
                    //stuff

                    val auth = Firebase.auth
                    // Capture user details
                    val name = names?.text.toString().replace("\\s".toRegex(), "")
                    val surname = surnames?.text.toString().replace("\\s".toRegex(), "")
                    val usersName = user2?.text.toString().replace("\\s".toRegex(), "")
                    val email = emails?.text.toString().replace("\\s".toRegex(), "")
                    val password = pass?.text.toString().replace("\\s".toRegex(), "")
                    val confirm = pass2?.text.toString().replace("\\s".toRegex(), "")
                    val userId = ""


                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            val auth = FirebaseAuth.getInstance()
                            val currentUser = auth.currentUser
                            Log.d("MyApp", "$currentUser")
                            val exception = task.exception
                            when {
                                task.isSuccessful && currentUser != null -> {

                                    auth.currentUser

                                    val users = User(
                                        name,
                                        surname,
                                        usersName,
                                        email,
                                        password,
                                        confirm,
                                        userId
                                    )
                                    myRef.child(usersName + password).setValue(users)
                                    val message = "USER ${user2?.text} HAS REGISTERED "
                                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
                                        .show()


                                    val homepage = Intent(this, Home::class.java)
                                    homepage.putExtra("home", R.layout.home)
                                    startActivity(homepage)
                                    overridePendingTransition(0, 0)
                                    finish()


                                }

                                task.isSuccessful && currentUser == null -> {


                                    val loginpage = Intent(this, Login::class.java)
                                    loginpage.putExtra("login", R.layout.login)

                                    overridePendingTransition(0, 0)
                                    startActivity(loginpage)
                                    finish()

                                }

                                exception is FirebaseAuthException && exception.errorCode == "ERROR_EMAIL_ALREADY_IN_USE" -> {
                                    emailRegAlready.show()


                                }

                                else -> {
                                    // STUFF TO DO

                                }
                            }
                        }


                }
            }


        }
    }


}

