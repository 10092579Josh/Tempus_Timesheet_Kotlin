package com.example.opsc_part2

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.media3.common.util.Log

// THIS PAGE HANDLES THE DISPLAY OF THE TASKS WHEN A SPECIFIC TASK IS CLICKED
class TaskPage: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.task_page)
// CALLING THE METHOD
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

            val columnIndex1 = 1
            val columnIndex2 = 2
            val columnIndex3 = 3
            val columnIndex4 = 4
            val columnIndex5 = 5
            val columnIndex6 = 6
            val columnIndex7 = 7
            val columnIndex11 = 11

            // CALLING THE ARRAY TO PASS OVER THE DATA
            val array = intent.getSerializableExtra("myDataList") as? Array<Array<String>>

            //ASSIGNING THE SPECIFIC INDEXES TO THE RELEVANT VARIABLES
            val category = array?.get(rowIndex)?.get(columnIndex1)
            val Description = array?.get(rowIndex)?.get(columnIndex2)
            val startdate = array?.get(rowIndex)?.get(columnIndex3)
            val Starttime = array?.get(rowIndex)?.get(columnIndex4)
            val Endtime = array?.get(rowIndex)?.get(columnIndex5)
            val mingoal = array?.get(rowIndex)?.get(columnIndex6)
            val maxgoal = array?.get(rowIndex)?.get(columnIndex7)

            try {
                val uri = array?.get(rowIndex)?.get(columnIndex11)
                taskimage.setImageURI(uri?.toUri())
            } catch (e: SecurityException) {
                Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();
            }
            //PRINTING THE DATA ACCORDING TO DATA ENTERED
            tname.text = task.toString().trim()
            catname.text = category.toString().trim()
            desc.text = Description.toString().trim()
            sdate.text = Starttime.toString().trim()
            edate.text = Endtime.toString().trim()
            hours2.text = hours.toString().trim()
            min.text = mingoal.toString().trim()
            max.text = maxgoal.toString().trim()
            date.text = startdate.toString().trim()


        }
        catch (e:Exception)
        {  Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();}
    }


}