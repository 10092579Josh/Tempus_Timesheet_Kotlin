package com.example.opsc_part2

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
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
            try{
            create.setOnClickListener()
            {
                val catname: EditText = findViewById(R.id.categoryNameInput)
                if (catname.text.toString().isNullOrEmpty()) {
                    var message = "ERROR: TASK NAME CAN NOT BE EMPTY "
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

                } else if (MainActivity.TaskClass.tasks[0][8].isNullOrEmpty()) {


                    MainActivity.TaskClass.tasks[0][8] = catname.text.toString()
                    MainActivity.TaskClass.hours[0][1] = MainActivity.TaskClass.hours[1][0].toString()
                    var message = "CATEGORY ${catname.text} ADDED "
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                } else if (MainActivity.TaskClass.tasks[1][8].isNullOrEmpty()) {


                    MainActivity.TaskClass.tasks[1][8] = catname.text.toString()
                    MainActivity.TaskClass.hours[1][1] = MainActivity.TaskClass.hours[2][0].toString()
                    var message = "CATEGORY ${catname.text} ADDED "
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                } else if (MainActivity.TaskClass.tasks[2][8].isNullOrEmpty()) {


                    MainActivity.TaskClass.tasks[2][8] = catname.text.toString()
                    MainActivity.TaskClass.hours[2][1] = MainActivity.TaskClass.hours[3][0].toString()
                    var message = "CATEGORY ${catname.text} ADDED "
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

                } else if (MainActivity.TaskClass.tasks[3][8].isNullOrEmpty()) {


                    MainActivity.TaskClass.tasks[3][8] = catname.text.toString()
                    MainActivity.TaskClass.hours[3][1] = MainActivity.TaskClass.hours[4][0].toString()

                    var message = "CATEGORY ${catname.text} ADDED "
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

                } else if (MainActivity.TaskClass.tasks[4][8].isNullOrEmpty()) {


                    MainActivity.TaskClass.tasks[4][8] = catname.text.toString()
                    MainActivity.TaskClass.hours[4][1] = MainActivity.TaskClass.hours[5][0].toString()

                    var message = "CATEGORY ${catname.text} ADDED "
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

                } else if (MainActivity.TaskClass.tasks[5][8].isNullOrEmpty()) {


                    MainActivity.TaskClass.tasks[5][8] = catname.text.toString()
                    MainActivity.TaskClass.hours[5][1] = MainActivity.TaskClass.hours[5][0].toString()
                    var message = "CATEGORY ${catname.text} ADDED "
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                } else if (MainActivity.TaskClass.tasks[6][8].isNullOrEmpty()) {


                    MainActivity.TaskClass.tasks[6][8] = catname.text.toString()
                    MainActivity.TaskClass.hours[6][1] = MainActivity.TaskClass.hours[6][0].toString()

                    var message = "CATEGORY ${catname.text} ADDED "
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                } else if (MainActivity.TaskClass.tasks[7][8].isNullOrEmpty()) {



                    MainActivity.TaskClass.tasks[7][8] = catname.text.toString()
                    MainActivity.TaskClass.hours[7][1] = MainActivity.TaskClass.hours[7][0].toString()
                    var message = "CATEGORY ${catname.text} ADDED "
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()


                } else if (MainActivity.TaskClass.tasks[8][8].isNullOrEmpty()) {
                    val one = MainActivity.TaskClass.hours[0][2]

                    MainActivity.TaskClass.tasks[8][8] = catname.text.toString()
                    MainActivity.TaskClass.hours[8][1] = MainActivity.TaskClass.hours[8][0].toString()
                    var message = "CATEGORY ${catname.text} ADDED "
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                } else {
                    val message = "ERROR: MAX TASKS REACHED "
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()


                }
                }

            } catch (e:Exception){}
        }    catch (e:Exception)
        {  Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();}
    }
}