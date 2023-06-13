package com.example.tempus

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import de.keyboardsurfer.android.widget.crouton.Crouton
import de.keyboardsurfer.android.widget.crouton.Style

class AppSettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_settings)

        populatefields()


        val homebtn = findViewById<ImageButton>(R.id.hometbtn)
        val breaksbtn = findViewById<ImageButton>(R.id.breakstbtn)
        val statsbtn = findViewById<ImageButton>(R.id.statstbtn)
        val settingsbtn = findViewById<ImageButton>(R.id.settingstbtn)
       val confirm  = findViewById<Button>(R.id.editButton)
        val addbtn = findViewById<ImageButton>(R.id.addbtn)
        val logout = findViewById<Button>(R.id.logoutButton)
        homebtn.setOnClickListener {
            val homepage = Intent(this, Home::class.java)
            startActivity(homepage)
            finish();
        }
//
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
        confirm.setOnClickListener()
         {    var username2: EditText = findViewById(R.id.userName_text)

              if (username2.text.isNullOrEmpty()) {

              }
             else {
                  val details = Intent(this, UserDetails::class.java)
                  startActivity(details)
                  finish();

              }

         }

        addbtn.setOnClickListener()
        {
            val tform = Intent(this, TaskForm::class.java)
            startActivity(tform)
            finish()

        }
        logout.setOnClickListener()
        {
            FirebaseAuth.getInstance().signOut()
            val Logout = Intent(this, Login::class.java)
            startActivity(Logout)
            finish();
            var username: EditText = findViewById(R.id.userName_text)
            var message = " ${username.text.toString()} HAS LOGGED OUT!"
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("isFirstLogin", true).apply()
            preloads.usersname = null



        }

    }
    object preloads
    { var names:String = ""
        var surname:String = ""
        var emails:String = ""
            var usersname: String? = null
        var conpass:String =""
        var pass:String = ""


    }


    private var isDialogOpen = false

    fun accountverify() {
        if (!isDialogOpen) {
            isDialogOpen = true
            val builder = AlertDialog.Builder(this)
            val titleView = layoutInflater.inflate(R.layout.authenticate_title, null)
            builder.setCustomTitle(titleView)

            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL

            // Set up the username input
            val usernameInput = EditText(this)
            usernameInput.hint = "Username"
            layout.addView(usernameInput)

            // Set up the password input
            val passwordInput = EditText(this)
            passwordInput.hint = "Password"
            passwordInput.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            layout.addView(passwordInput)

            builder.setView(layout)



                // Set up the buttons
                builder.setPositiveButton("OK") { dialog, which ->
                    val username = usernameInput.text.toString().trim()
                    val password = passwordInput.text.toString().trim()
                    val verify = username + password

                    val database = Firebase.database
                    val userid = FirebaseAuth.getInstance().currentUser?.uid
                    val myRef = database.getReference("users")


                    val oldRef =
                        FirebaseDatabase.getInstance().getReference("users/${verify.toString()}")
                    val newRef =
                        FirebaseDatabase.getInstance().getReference("users/${userid.toString()}")
                    if (usernameInput.text.isNullOrEmpty()) {

                    } else if (passwordInput.text.isNullOrEmpty()) {

                    } else if (usernameInput.text.isNullOrEmpty() && passwordInput.text.isNullOrEmpty()) {

                    } else {
                        oldRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val data = dataSnapshot.value as? Map<*, *>
                                if (dataSnapshot.exists()) {
                                    if (data != null) {
                                        newRef.setValue(data) { error, _ ->
                                            if (error == null) {
                                                oldRef.removeValue()
                                                myRef.child(userid.toString()).child("userid")
                                                    .setValue(userid)
                                            }
                                        }
                                    } else {
                                        // The document does not exist

                                        validerror(Errors())
                                    }
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Handle error
                            }
                        }
                        )
                    }




                    isDialogOpen = false
                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                    dialog.cancel()
                    isDialogOpen = false
                }

                builder.setOnCancelListener {
                    isDialogOpen = false
                }

                builder.show()
            }

    }




    fun accountverify2() {

        val buttonLayout = layoutInflater.inflate(R.layout.authenticate_buttons, null)
            val builder = AlertDialog.Builder(this)

        val dialog = builder.create()

            // Set up the custom title view
            val titleView = layoutInflater.inflate(R.layout.authenticate_title, null)
            builder.setCustomTitle(titleView)

            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL

            // Set up the username input
            val usernameInput = EditText(this)
            usernameInput.hint = "Username"
            layout.addView(usernameInput)

            // Set up the password input
            val passwordInput = EditText(this)
            passwordInput.hint = "Password"
            passwordInput.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            layout.addView(passwordInput)


        val okButton = buttonLayout.findViewById<Button>(R.id.positive_button)
        val cancelButton = buttonLayout.findViewById<Button>(R.id.negative_button)
        layout.addView(buttonLayout)

        builder.setView(layout)


        builder.show()



        cancelButton.setOnClickListener {

            dialog.dismiss()


        }
            okButton.setOnClickListener {
                val username = usernameInput.text.toString().trim()
                val password = passwordInput.text.toString().trim()
                val verify =username+password

                val database = Firebase.database
                val userid = FirebaseAuth.getInstance().currentUser?.uid
                val myRef = database.getReference("users")


                val oldRef = FirebaseDatabase.getInstance().getReference("users/${verify.toString()}")
                val newRef = FirebaseDatabase.getInstance().getReference("users/${userid.toString()}")

                oldRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val data = dataSnapshot.value as? Map<*, *>
                        if (dataSnapshot.exists()) {
                            if (data != null) {
                                newRef.setValue(data) { error, _ ->
                                    if (error == null) {
                                        oldRef.removeValue()
                                        myRef.child(userid.toString()).child("userid").setValue(userid)
                                    }
                                }
                            } else {
                                // The document does not exist

                                validerror( Errors())
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle error
                    }
                })





        }
    }

    fun validerror(errors: Errors) {
        val crouton = Crouton.makeText(this, errors.ValidationError, Style.ALERT)
        crouton.show()
    }

    fun populatefields()
    {


        val userid = FirebaseAuth.getInstance().currentUser?.uid
        val database = Firebase.database
        val myRef = database.getReference("users")

        var username2: EditText = findViewById(R.id.userName_text)

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val userId = data.child("userid").getValue(String::class.java)

                    if (userId.toString().trim() == userid.toString().trim()) {

                        var name2: EditText = findViewById(R.id.nameEditText)
                        var surname2: EditText = findViewById(R.id.surnameEditText)

                        var email2 : EditText = findViewById(R.id.emailEditText)
                        if(preloads.names.isNullOrEmpty()) {
                            preloads.names =
                                data.child("name").getValue(String::class.java).toString()
                            preloads.emails =
                                data.child("email").getValue(String::class.java).toString()
                            preloads.surname =
                                data.child("surname").getValue(String::class.java).toString()
                            preloads.usersname =
                                data.child("usersname").getValue(String::class.java).toString()
                            preloads.conpass =
                                data.child("confirm").getValue(String::class.java).toString()
                            preloads.pass =
                                data.child("password").getValue(String::class.java).toString()


                        }

                        name2.setText( preloads.names)
                        surname2.setText(preloads.surname)
                        username2.setText(preloads.usersname)
                        email2.setText(preloads.emails)
                    }
                    else if (preloads.usersname == null)
                    {
                        accountverify()
                    }






                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })

    }
}