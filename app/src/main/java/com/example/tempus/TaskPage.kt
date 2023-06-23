package com.example.tempus


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File


// THIS PAGE HANDLES THE DISPLAY OF THE TASKS WHEN A SPECIFIC TASK IS CLICKED
class TaskPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.task_page)
            security()
            FirebaseApp.initializeApp(this)
            taskPopulation()

            val taskImage = findViewById<ImageView>(R.id.task_image)
            val homebtn = findViewById<ImageButton>(R.id.hometbtn)
            val breaksbtn = findViewById<ImageButton>(R.id.breakstbtn)
            val statsbtn = findViewById<ImageButton>(R.id.statstbtn)
            val settingsbtn = findViewById<ImageButton>(R.id.settingstbtn)
            val addbtn = findViewById<ImageButton>(R.id.addbtn)

            taskImage.isEnabled = true
            taskImage.isClickable = true
            taskImage.setOnClickListener {
                Log.d("MyApp", "ImageView clicked")
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Choose an option")
                builder.setItems(arrayOf("Take a photo", "Pick from gallery")) { _, which ->
                    when (which) {

                        0 -> camera.launch(null)
                        1 -> galleryContent.launch("imageURL/*")
                    }

                }

                val dialog = builder.create()
                dialog.show()
            }

            addbtn.setOnClickListener()
            {
                val tform = Intent(this, TaskForm::class.java)
                overridePendingTransition(0, 0)
                startActivity(tform)
                finish()

            }

            homebtn.setOnClickListener {
                val intent = Intent(this, Home::class.java)
                intent.putExtra("home", getIntent().getIntExtra("home", R.layout.home))
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()


            }

            breaksbtn.setOnClickListener {
                val breakspage = Intent(this, Breaks::class.java)
                overridePendingTransition(0, 0)
                startActivity(breakspage)
                finish()

            }

            statsbtn.setOnClickListener {
                val statspage = Intent(this, Statistics::class.java)
                overridePendingTransition(0, 0)
                startActivity(statspage)
                finish()

            }

            settingsbtn.setOnClickListener {
                val settingspage = Intent(this, AppSettings::class.java)
                overridePendingTransition(0, 0)
                startActivity(settingspage)
                finish()

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
                val intent = Intent(this@TaskPage, Login::class.java)
                intent.putExtra("login", R.layout.login)
                overridePendingTransition(0, 0)
                startActivity(intent)

            }
        }

        val user = FirebaseAuth.getInstance().currentUser
        user?.reload()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //stuff to do

            } else {
                val exception = task.exception
                if (exception is FirebaseAuthInvalidUserException) {
                    val errorCode = exception.errorCode
                    if (errorCode == "ERROR_USER_NOT_FOUND") {
                        val sharedPreferences =
                            getSharedPreferences("preferences", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putBoolean("isFirstLogin", true).apply()
                        AppSettings.Preloads.userSName = null
                        val intent = Intent(this@TaskPage, Login::class.java)
                        intent.putExtra("login", R.layout.login)
                        overridePendingTransition(0, 0)
                        startActivity(intent)
                    }
                }
            }
        }

    }

    private fun taskPopulation() {
//THIS INSTANTIATES THE FIELDS AND CREATES VARIABLES
        try {


            val tName = findViewById<TextView>(R.id.task_name)
            val catname = findViewById<TextView>(R.id.category_name)
            val desc = findViewById<TextView>(R.id.description_text)
            val sDate = findViewById<TextView>(R.id.start_date_display)
            val eDate = findViewById<TextView>(R.id.end_date_display)
            val hours2 = findViewById<TextView>(R.id.hour_text)
            val min = findViewById<TextView>(R.id.min_text)
            val max = findViewById<TextView>(R.id.max_text)
            val date = findViewById<TextView>(R.id.date_display)
            val taskImage = findViewById<ImageView>(R.id.task_image)


            val itemId = intent.getStringExtra("itemId")
            //THIS INDEX LETS THE FOR LOOP SORT THE SPECIFIC INDEX WHICH WONT CHANGE AS PER THE POSITION WHICH WILL CHANGE


            val db = FirebaseFirestore.getInstance()


            val userid = FirebaseAuth.getInstance().currentUser?.uid
// Query Firestore to get the data for the clicked item
            db.collection("TaskStorage")
                .whereEqualTo("userIdTask", userid.toString().trim())
                .whereEqualTo("taskName", itemId)
                .get()
                .addOnSuccessListener { documents ->
                    // Get the last document in the result, which corresponds to the clicked item
                    val document = documents.documents.lastOrNull()
                    if (document != null) {
                        // Get the data for the clicked item from the document


                        // Use the data from Firestore to populate the fields in your form
                        tName.text = document.getString("taskName")
                        catname.text = document.getString("categoryName")
                        desc.text = document.getString("description")
                        sDate.text = document.getString("starTime")
                        eDate.text = document.getString("endTime")
                        hours2.text = document.getString("duration")
                        min.text = document.getString("minGoal")
                        max.text = document.getString("maxGoal")
                        date.text = document.getString("dateAdded")
                        val url = document.getString("imageURL")


                        Glide.with(this)
                            .load(url)
                            .into(taskImage)


                    }
                }


        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    private val galleryContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { url: Uri? ->


            if (url != null) {
                val task = findViewById<TextView>(R.id.task_name)

                val imageView = findViewById<ImageView>(R.id.task_image)
                imageView.setImageURI(url)

                val store = Firebase.storage.reference.child(task.text.toString().trim())


                val choice = store.putFile(url)
                choice.addOnSuccessListener {

                    val message = "IMAGE UPLOADED ,PLEASE RESTART THE APP"
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                    cacheClosure(this)
                }.addOnFailureListener {

                }
            }
        }
    private val camera =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { photo: Bitmap? ->

            val task = findViewById<TextView>(R.id.task_name)

            val imageView = findViewById<ImageView>(R.id.task_image)
            imageView.setImageBitmap(photo)


            val imageRef = Firebase.storage.reference.child(task.text.toString().trim())

            val stream = ByteArrayOutputStream()
            photo?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val data = stream.toByteArray()

            val uploadDP = imageRef.putBytes(data)
            uploadDP.addOnSuccessListener {

                val message = "IMAGE UPLOADED ,PLEASE RESTART THE APP"
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                cacheClosure(this)

            }.addOnFailureListener {
                val message = "INVALID IMAGE!"
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            }
        }


    private fun cacheClosure(context: Context) {
        try {
            val location: File = context.cacheDir
            appFiles(location)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun appFiles(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val sub: Array<String> = dir.list() as Array<String>
            for (i in sub.indices) {
                val deleted = appFiles(File(dir, sub[i]))
                if (!deleted) {
                    return false
                }
            }
        }
        return dir?.delete() ?: false
    }


}