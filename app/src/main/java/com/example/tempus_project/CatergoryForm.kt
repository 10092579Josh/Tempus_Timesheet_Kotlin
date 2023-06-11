package com.example.tempus_project

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// THIS ALLOWS THE USER TO CREATE A CATEGORY
// THIS HOLDS THE DATA ASSIGNMENT
//ASSIGNS TO THE ARRAY
class CatergoryForm: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.catergory_form)
        try {


            val create = findViewById<Button>(R.id.createCategory)
            val addbtn = findViewById<ImageButton>(R.id.addbtn)
            val homebtn = findViewById<ImageButton>(R.id.hometbtn)
            val breaksbtn = findViewById<ImageButton>(R.id.breakstbtn)
            val statsbtn = findViewById<ImageButton>(R.id.statstbtn)
            val settingsbtn = findViewById<ImageButton>(R.id.settingstbtn)


            homebtn.setOnClickListener {
                val homepage = Intent(this, MainActivity::class.java)
                startActivity(homepage)
                finish()


            }

            breaksbtn.setOnClickListener {
                val breakspage = Intent(this, BreaksActivity::class.java)
                startActivity(breakspage)
                finish()

            }

            statsbtn.setOnClickListener {
                val statspage = Intent(this, StatsActivity::class.java)
                startActivity(statspage)
                finish()

            }

            settingsbtn.setOnClickListener {
                val settingspage = Intent(this, SettingsActivity::class.java)
                startActivity(settingspage)
                finish()

            }

            addbtn.setOnClickListener()
            {
                val tform = Intent(this, TaskForm::class.java)
                startActivity(tform)
                finish()

            }
            try {
                create.setOnClickListener()
                {
                    val catnames: EditText = findViewById(R.id.categoryNameInput)
                    if (catnames.text.toString().isNullOrEmpty()) {
                        var message = "ERROR: TASK NAME CAN NOT BE EMPTY "
                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

                    } else {
                        // Get the current user's unique ID
                        val firestore = Firebase.firestore
                        val userid = Firebase.auth.currentUser?.uid
                        val itemsadd = firestore.collection("Categories")


                        val catname = catnames.text.toString().trim()
                        val cathours = "0"


// Add as many items
                        val cat = catergories(catname,cathours,userid.toString().trim())

                        val docRef = itemsadd.document(catname)
                        docRef.set(cat)

                    }

// Add all the items in

                }



            } catch (e: Exception) {
                Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();
            }

        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}

