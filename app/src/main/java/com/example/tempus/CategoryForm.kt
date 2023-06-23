package com.example.tempus


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.keyboardsurfer.android.widget.crouton.Crouton
import de.keyboardsurfer.android.widget.crouton.Style

// THIS ALLOWS THE USER TO CREATE A CATEGORY
// THIS HOLDS THE DATA ASSIGNMENT
//ASSIGNS TO THE ARRAY
class CategoryForm : AppCompatActivity() {

    private val e = Errors()
    private val catEmpty = Crouton.makeText(this, e.emptyCat, Style.ALERT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.catergory_form)
        try {
            security()

            val create = findViewById<Button>(R.id.createCategory)
            val addbtn = findViewById<ImageButton>(R.id.addbtn)
            val homebtn = findViewById<ImageButton>(R.id.hometbtn)
            val breaksbtn = findViewById<ImageButton>(R.id.breakstbtn)
            val statsbtn = findViewById<ImageButton>(R.id.statstbtn)
            val settingsbtn = findViewById<ImageButton>(R.id.settingstbtn)


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
                val tform = Intent(this, TaskForm::class.java)
                startActivity(tform)
                overridePendingTransition(0, 0)
                finish()

            }
            try {
                create.setOnClickListener {
                    val catNames: EditText = findViewById(R.id.categoryNameInput)
                    if (catNames.text.toString().isEmpty()) {
                        catEmpty.show()


                    } else {
                        // Get the current user's unique ID
                        val firestore = Firebase.firestore
                        val userid = Firebase.auth.currentUser?.uid
                        val itemAdd = firestore.collection("CategoryStorage")


                        val catname = catNames.text.toString().trim()
                        val totalHours = "00:00"


                        val cat = CategoryStorage(catname, totalHours, userid.toString().trim())

                        val docRef = itemAdd.document(catname)
                        docRef.set(cat)
                        val message = "$catname added "
                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                    }


                }


            } catch (e: Exception) {
                Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun security() {

        val auth = FirebaseAuth.getInstance()
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {

                val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
                sharedPreferences.edit().putBoolean("isFirstLogin", true).apply()
                AppSettings.Preloads.userSName = null
                val intent = Intent(this@CategoryForm, Login::class.java)
                intent.putExtra("login", R.layout.login)
                overridePendingTransition(0, 0)
                startActivity(intent)

            }
        }

        val user = FirebaseAuth.getInstance().currentUser
        user?.reload()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // to do things here

            } else {
                val exception = task.exception
                if (exception is FirebaseAuthInvalidUserException) {
                    val errorCode = exception.errorCode
                    if (errorCode == "ERROR_USER_NOT_FOUND") {
                        val sharedPreferences =
                            getSharedPreferences("preferences", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putBoolean("isFirstLogin", true).apply()
                        AppSettings.Preloads.userSName = null
                        val intent = Intent(this@CategoryForm, Login::class.java)
                        intent.putExtra("login", R.layout.login)
                        overridePendingTransition(0, 0)
                        startActivity(intent)
                    }
                }
            }
        }

    }
}

