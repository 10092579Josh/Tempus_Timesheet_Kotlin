package com.example.tempus

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.EditText
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

// THIS PAGE HANDLES THE DISPLAY OF THE TASKS WHEN A SPECIFIC TASK IS CLICKED
class TaskPage: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.task_page)
// CALLING THE METHOD
            FirebaseApp.initializeApp(this)
            taskpopulation()

            val taskimage = findViewById<ImageView>(R.id.task_image)
            val homebtn = findViewById<ImageButton>(R.id.hometbtn)
            val breaksbtn = findViewById<ImageButton>(R.id.breakstbtn)
            val statsbtn = findViewById<ImageButton>(R.id.statstbtn)
            val settingsbtn = findViewById<ImageButton>(R.id.settingstbtn)
            val addbtn = findViewById<ImageButton>(R.id.addbtn)

            taskimage.isEnabled = true
            taskimage.isClickable = true
            taskimage.setOnClickListener(){
                Log.d("MyApp", "ImageView clicked")
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Choose an option")
                builder.setItems(arrayOf("Take a photo", "Pick from gallery")) { _, which ->
                    when (which) {

                        0 -> camera.launch(null)
                        1 -> GalleryContent.launch("imageURL/*")
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
                intent.putExtra("home", getIntent().getIntExtra("home",R.layout.home))
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
        }catch (e:Exception)
        {  Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();}

    }

// THIS METHOD CALLS WHERE THE DATA IS STORED AND LETS THE USER VIEW INPUTTED DATA FOR EACH TASK

    fun taskpopulation() {
//THIS INSTANTIATES THE FIELDS AND CREATES VARIABLES
        try {


            val tname = findViewById<TextView>(R.id.task_name)
            val catname = findViewById<TextView>(R.id.category_name)
            val desc = findViewById<TextView>(R.id.description_text)
            val sdate = findViewById<TextView>(R.id.start_date_display)
            val edate = findViewById<TextView>(R.id.end_date_display)
            val hours2 = findViewById<TextView>(R.id.hour_text)
            val min = findViewById<TextView>(R.id.min_text)
            val max = findViewById<TextView>(R.id.max_text)
            val date = findViewById<TextView>(R.id.date_display)
            val taskimage = findViewById<ImageView>(R.id.task_image)
            val position = intent.getIntExtra("position1", 0)
            val hours = intent.getStringExtra("duration")
            val task = intent.getStringExtra("task")
            val rowIndex = position
            val itemId = intent.getStringExtra("itemId")
            //THIS INDEX LETS THE FOR LOOP SORT THE SPECIFIC INDEX WHICH WONT CHANGE AS PER THE POSITION WHICH WILL CHANGE


            val db = FirebaseFirestore.getInstance()


            val userid = FirebaseAuth.getInstance().currentUser?.uid
// Query Firestore to get the data for the clicked item
            db.collection("TaskStorage")
                .whereEqualTo("userIdTask",userid.toString().trim())
                .whereEqualTo("taskName", itemId)
                .get()
                .addOnSuccessListener { documents ->
                    // Get the last document in the result, which corresponds to the clicked item
                    val document = documents.documents.lastOrNull()
                    if (document != null) {
                        // Get the data for the clicked item from the document


                            // Use the data from Firestore to populate the fields in your form
                            tname.text = document.getString("taskName")
                            catname.text = document.getString("categoryName")
                            desc.text = document.getString("description")
                            sdate.text = document.getString("starTime")
                            edate.text = document.getString("endTime")
                            hours2.text = document.getString("duration")
                            min.text = document.getString("minGoal")
                            max.text = document.getString("maxGoal")
                            date.text = document.getString("dateAdded")
                        val url = document.getString("imageURL")


                        Glide.with(this)
                            .load(url)
                            .into(taskimage)






                    }
                }


        }
        catch (e:Exception)
        {  Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();}

    }
    private val GalleryContent = registerForActivityResult(ActivityResultContracts.GetContent()) { url: Uri? ->


        if (url != null) {
            val task = findViewById<EditText>(R.id.taskNameInput)

            val imageView = findViewById<ImageView>(R.id.imgGallery)
            imageView.setImageURI(url)

            val store = Firebase.storage.reference.child(task.text.toString().trim())


            val choice = store.putFile(url)
            choice.addOnSuccessListener {

            }.addOnFailureListener {

            }
        }
    }
    private val camera = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { photo: Bitmap? ->

        val task = findViewById<EditText>(R.id.taskNameInput)

        val imageView = findViewById<ImageView>(R.id.imgGallery)
        imageView.setImageBitmap(photo)



        val ImageRef = Firebase.storage.reference.child(task.text.toString().trim())

        val stream = ByteArrayOutputStream()
        photo?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val data = stream.toByteArray()

        val uploadDP = ImageRef.putBytes(data)
        uploadDP.addOnSuccessListener {
            val message = "IMAGE UPLOADED "
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            val message = "INVALID IMAGE!"
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }



}