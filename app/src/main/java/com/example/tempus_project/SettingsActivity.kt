package com.example.tempus_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.bumptech.glide.Glide
import com.example.tempus_project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)



        val userid = FirebaseAuth.getInstance().currentUser?.uid
        val database = Firebase.database
        val myRef = database.getReference("users")

        val user = Firebase.auth.currentUser


        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (ds in dataSnapshot.children) {
                        val userId = ds.child("userid").getValue(String::class.java)

                        if (userId.toString().trim() == userid.toString().trim()) {
                            var name2: EditText = findViewById(R.id.nameEditText)
                            var surname2: EditText = findViewById(R.id.surnameEditText)
                            var username2: EditText = findViewById(R.id.userName_text)
                            var email2 : EditText = findViewById(R.id.emailEditText)
                            val name = ds.child("name").getValue(String::class.java)
                            val email = ds.child("email").getValue(String::class.java)
                            val surname = ds.child("surname").getValue(String::class.java)
                            val username = ds.child("usersname").getValue(String::class.java)

                            // Update UI with retrieved data
                            name2.setText(name)
                            surname2.setText(surname)
                            username2.setText(username)
                            email2.setText(email)

                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })



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

             var name2: EditText = findViewById(R.id.nameEditText)
             var surname2: EditText = findViewById(R.id.surnameEditText)
             if (name2.text.isNullOrEmpty())
             {  var message = " ERROR NEED NAME TO UPDATE ID"
                 Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()}
             else  if (surname2.text.isNullOrEmpty())
             {
                 var message = " ERROR NEED SURNAME TO UPDATE ID"
                 Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
             }
             else if (name2.text.isNullOrEmpty()&& surname2.text.isNullOrEmpty())
             {

                 var message = " ERROR NEED NAME AND SURNAME TO UPDATE ID"
                 Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
             }
             else
             {
                 val details = Intent(this, EditDetails::class.java)
                 startActivity(details)
                 finish();
                 val userid = FirebaseAuth.getInstance().currentUser?.uid
                 val myRef = database.getReference("users")

                 val childKey = name2.text.toString().trim()+surname2.text.toString().trim()
                 myRef.child(childKey).child("userid").setValue(userid)
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


        }

    }
}