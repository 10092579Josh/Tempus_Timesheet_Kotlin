package com.example.tempus_project

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import android.content.Context
import android.view.View
import com.example.tempus_project.R


class registrationActivty : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration)
notifications()
        FirebaseApp.initializeApp(this)

    }
fun notifications()
{
    var back:ImageButton = findViewById(R.id.back_btn)
    back.setOnClickListener(){

        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish();

    }
    val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val regpopupView = layoutInflater.inflate(R.layout.popup_window, null)
    val newpopupWindow = PopupWindow(
        regpopupView,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    var email1: com.google.android.material.textfield.TextInputLayout = findViewById(R.id.email)
    email1.editText?.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val email = s.toString()
            if (check(email)) {
                // email is valid
                newpopupWindow.dismiss()
                input()






            } else {
                // email is not valid
                if (!newpopupWindow.isShowing) {
                    newpopupWindow.showAsDropDown(email1.editText, 0, 0)
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

    email1.editText?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
            // email EditText field lost focus
            newpopupWindow.dismiss()
        } }


}
    fun check(str: String): Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }

    object SourceClass {
        val rows = 5
        val columns = 7

        val datas = Array(rows) { arrayOfNulls<String>(columns) }
    }


    fun input ()
    { //variables



        var submitting: Button = findViewById(R.id.sub)
        var name1: com.google.android.material.textfield.TextInputLayout = findViewById(R.id.firstName)
        var names = name1.editText

        var surname1: com.google.android.material.textfield.TextInputLayout =
            findViewById(R.id.surname)
        var surnames = surname1.editText
        var username1: com.google.android.material.textfield.TextInputLayout =
            findViewById(R.id.usernames)
        var user2 = username1.editText
        var password1: com.google.android.material.textfield.TextInputLayout =
            findViewById(R.id.enterPassword)
        var pass = password1.editText
        var confirmpassword: com.google.android.material.textfield.TextInputLayout =
            findViewById(R.id.confirm_password)
        var pass2 = confirmpassword.editText
        var email1: com.google.android.material.textfield.TextInputLayout = findViewById(R.id.email)
        var emails = email1.editText





        submitting.setOnClickListener()

        {

            //action statements tp check fields if empty
            if (pass?.text.toString() != pass2?.text.toString())
            { var message  = " passwords do not match"
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

            }
            else if(names?.text.toString().isEmpty())
            {
                var message  = " no name entered "
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            }
            else if(surnames?.text.toString().isEmpty())
            {
                var message  = " no surname entered "
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            }
            else if(user2?.text.toString().isEmpty())
            {
                var message  = " no username entered "
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            }
            else if(pass?.text.toString().length <7)
            {
                var message  = " enter password is too short"
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            }
            else if(pass2?.text.toString().length <7)
            { var message  = " confirm password is too short "
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

            }
            else if(emails?.text.toString().isEmpty())
            {
                var message  = " no email entered "
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            }
            else
            { // move to the next screen if filled

                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference("users")
//stuff

                val auth = Firebase.auth
// Capture user details
                val name = names?.text.toString().replace("\\s".toRegex(), "")
                val surname = surnames?.text.toString().replace("\\s".toRegex(), "")
                val usersname = user2?.text.toString().replace("\\s".toRegex(), "")
                val email = emails?.text.toString().replace("\\s".toRegex(), "")
                val password = pass?.text.toString().replace("\\s".toRegex(), "")
                val confirm = pass2?.text.toString().replace("\\s".toRegex(), "")



// Create a new user with email and password
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // User account created
                            val intent = Intent(this, Login::class.java)
                            startActivity(intent)
                            finish()
                            val user = auth.currentUser

                            var message  = "USER ${user2?.text} HAS REGISTERED "
                            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

                        } else {
                            // Account creation failed\
                            var message  = "USER ${user2?.text} is not registred "
                            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                val userid = auth.currentUser?.uid.toString()
                val user = User(name,surname,usersname, email, password, confirm,userid)



// Save the user data to the database
                myRef.child(name+surname).setValue(user)


// Create a User object to hold the captured data






            }



        }
    }


}

