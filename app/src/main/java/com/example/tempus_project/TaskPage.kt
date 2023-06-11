package com.example.tempus_project

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// THIS PAGE HANDLES THE DISPLAY OF THE TASKS WHEN A SPECIFIC TASK IS CLICKED
class TaskPage: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.task_page)
// CALLING THE METHOD
            FirebaseApp.initializeApp(this)
            taskpopulation()


            val homebtn = findViewById<ImageButton>(R.id.hometbtn)
            val breaksbtn = findViewById<ImageButton>(R.id.breakstbtn)
            val statsbtn = findViewById<ImageButton>(R.id.statstbtn)
            val settingsbtn = findViewById<ImageButton>(R.id.settingstbtn)
            val addbtn = findViewById<ImageButton>(R.id.addbtn)


            addbtn.setOnClickListener()
            {
                val tform = Intent(this, TaskForm::class.java)
                startActivity(tform)
                finish()

            }

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
            val position = intent.getIntExtra("position", 0)
            val hours = intent.getStringExtra("hours")
            val task = intent.getStringExtra("task")
            val rowIndex = position

            //THIS INDEX LETS THE FOR LOOP SORT THE SPECIFIC INDEX WHICH WONT CHANGE AS PER THE POSITION WHICH WILL CHANGE


            val db = FirebaseFirestore.getInstance()



            val userid = FirebaseAuth.getInstance().currentUser?.uid
// Query Firestore to get the data for the clicked item
            db.collection("Tasks")
                .whereEqualTo("userid",userid.toString().trim())
                .limit(position + 1.toLong()) // Limit the results to the first (position + 1) items
                .get()
                .addOnSuccessListener { documents ->
                    // Get the last document in the result, which corresponds to the clicked item
                    val document = documents.documents.lastOrNull()
                    if (document != null) {
                        // Get the data for the clicked item from the document


                            // Use the data from Firestore to populate the fields in your form
                            tname.text = document.getString("taskname")
                            catname.text = document.getString("categorytask")
                            desc.text = document.getString("description")
                            sdate.text = document.getString("startime")
                            edate.text = document.getString("endtime")
                            hours2.text = document.getString("hours")
                            min.text = document.getString("mingoal")
                            max.text = document.getString("maxgoal")
                            date.text = document.getString("date")
                        val url = document.getString("image")


                        Glide.with(this)
                            .load(url)
                            .into(taskimage)




                    }
                }



        }
        catch (e:Exception)
        {  Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();}
    }


}