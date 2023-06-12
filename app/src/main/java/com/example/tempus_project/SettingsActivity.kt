package com.example.tempus_project

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import de.keyboardsurfer.android.widget.crouton.Crouton
import de.keyboardsurfer.android.widget.crouton.Style

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        populatefields()


        val homebtn = findViewById<ImageButton>(R.id.hometbtn)
        val breaksbtn = findViewById<ImageButton>(R.id.breakstbtn)
        val statsbtn = findViewById<ImageButton>(R.id.statstbtn)
        val settingsbtn = findViewById<ImageButton>(R.id.settingstbtn)
       val confirm  = findViewById<Button>(R.id.editButton)
        val addbtn = findViewById<ImageButton>(R.id.addbtn)
        val logout = findViewById<Button>(R.id.logoutButton)
        homebtn.setOnClickListener {
            val homepage = Intent(this, MainActivity::class.java)
            startActivity(homepage)
            finish();
        }
//
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
        confirm.setOnClickListener()
         {


                 val details = Intent(this, EditDetails::class.java)
                 startActivity(details)
                 finish();



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
            builder.setTitle("USER AUTHENTICATION")

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
    fun validerror(errors: Errors) {
        val crouton = Crouton.makeText(this, errors.ValidationError, Style.ALERT)
        crouton.show()
    }

    fun populatefields()
    {


        val userid = FirebaseAuth.getInstance().currentUser?.uid
        val database = Firebase.database
        val myRef = database.getReference("users")



        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val userId = data.child("userid").getValue(String::class.java)

                    if (userId.toString().trim() == userid.toString().trim()) {

                        var name2: EditText = findViewById(R.id.nameEditText)
                        var surname2: EditText = findViewById(R.id.surnameEditText)
                        var username2: EditText = findViewById(R.id.userName_text)
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
                        // Update UI with retrieved data
                        name2.setText( preloads.names)
                        surname2.setText(preloads.surname)
                        username2.setText(preloads.usersname)
                        email2.setText(preloads.emails)


                    }

                    else if (userId != userid && preloads.usersname.isNullOrEmpty()) {

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