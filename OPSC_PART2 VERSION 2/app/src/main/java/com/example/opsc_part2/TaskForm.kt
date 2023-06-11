package com.example.opsc_part2

import android.app.TimePickerDialog
import android.app.DatePickerDialog
import java.util.Calendar
import android.view.View
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.*
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import com.google.firebase.firestore.FirebaseFirestore

// THIS IS FOR THE CREATION OF THE TASK
// THIS HAS THE VARIABLE ASSIGNMENT
class TaskForm:AppCompatActivity() {
    private lateinit var selectedDateText: TextView
    private lateinit var selectedStartTimeText: TextView
    private lateinit var selectedEndTimeText: TextView
    private lateinit var uploadPictureBtn: Button
    private lateinit var imgGallery : ImageView
    private val myDataList = mutableListOf<MainTaskActivity.ItemsViewModel>()
    private val customAdapter = MainTaskActivity.CustomAdapter(myDataList)


    companion object {
        val IMAGE_REQUEST_CODE = 100
    }



    override fun onCreate(savedInstanceState: Bundle?) {
 try {


     super.onCreate(savedInstanceState)
     setContentView(R.layout.task_form)
     selectedDateText = findViewById(R.id.selectedDateText)
     selectedStartTimeText = findViewById(R.id.selectedStartTimeText)
     selectedEndTimeText = findViewById(R.id.selectedEndTimeText)
     uploadPictureBtn = findViewById(R.id.uploadPicturetbtn)
     imgGallery = findViewById(R.id.imgGallery)

     tasks()
     val homebtn = findViewById<ImageButton>(R.id.hometbtn)
     val breaksbtn = findViewById<ImageButton>(R.id.breakstbtn)
     val statsbtn = findViewById<ImageButton>(R.id.statstbtn)
     val settingsbtn = findViewById<ImageButton>(R.id.settingstbtn)

     val addbtn = findViewById<ImageButton>(R.id.addbtn)

     uploadPictureBtn.setOnClickListener {
         pickImageGallery()
     }

     // this creates a vertical layout Manager
     addbtn.setOnClickListener()
     {
         val homepage = Intent(this, TaskForm::class.java)
         startActivity(homepage)
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
 }
 catch (e:Exception)
 {  Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();}
    }
    private fun  pickImageGallery(){
        try {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_REQUEST_CODE)
        }
        catch (e:Exception)
        {  Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();}




    }
    // MODEL FOR THE IMAGE
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK ){

                val Uri = data?.data
                val item = MainTaskActivity.ItemsViewModel(
                    "Text",
                    "Hours",
                    "Sub",
                    "Date",

                )
                if(MainActivity.TaskClass.tasks[0][0].isNullOrEmpty()) {
                    MainActivity.TaskClass.tasks[0][11] = Uri.toString()



                }
                else if(MainActivity.TaskClass.tasks[1][0].isNullOrEmpty()) {
                    MainActivity.TaskClass.tasks[1][11] = Uri.toString()



                }
                else if(MainActivity.TaskClass.tasks[2][0].isNullOrEmpty()) {
                    MainActivity.TaskClass.tasks[2][11] = Uri.toString()
                }
                else if(MainActivity.TaskClass.tasks[3][0].isNullOrEmpty()) {
                    MainActivity.TaskClass.tasks[3][11] = Uri.toString()

                }
                else if(MainActivity.TaskClass.tasks[4][0].isNullOrEmpty()) {
                    MainActivity.TaskClass.tasks[4][11] = Uri.toString()

                }
                else if(MainActivity.TaskClass.tasks[5][0].isNullOrEmpty()) {
                    MainActivity.TaskClass.tasks[5][11] = Uri.toString()

                }
                else if(MainActivity.TaskClass.tasks[6][0].isNullOrEmpty()) {
                    MainActivity.TaskClass.tasks[6][11] = Uri.toString()
                }
                else if(MainActivity.TaskClass.tasks[7][0].isNullOrEmpty()) {
                    MainActivity.TaskClass.tasks[7][11] = Uri.toString()
                }
                else if(MainActivity.TaskClass.tasks[8][0].isNullOrEmpty()) {
                    MainActivity.TaskClass.tasks[8][11] = Uri.toString()

                }

                myDataList.add(item)
                customAdapter.notifyDataSetChanged()
            }



        }            catch (e:Exception)
        {  Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();}


    }
    // MODEL FOR THE DATES
    fun selectDate(view: View) {
        try {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            // Create date picker dialog
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    // Set selected date to the TextView
                    selectedDateText.text = "$dayOfMonth/${month + 1}/$year"
                },
                year,
                month,
                dayOfMonth
            )
            datePickerDialog.show()
        }
        catch (e:Exception)
        {  Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();}



    }
    // MODEL FOR THE TIMES
    fun selectTime(view: View) {
        try {
            val calendar = Calendar.getInstance()
            val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val timePickerDialog = object : TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    val selectedStartTime = String.format("%02d:%02d", hourOfDay, minute - minute % 15)
                    selectedStartTimeText.text = selectedStartTime
                },
                hourOfDay,
                minute,
                true
            ) {
                override fun onTimeChanged(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    val roundedMinute = (minute / 15) * 15
                    if (minute != roundedMinute) {
                        view?.minute = roundedMinute
                    }
                }
            }

            timePickerDialog.show()
        }
        catch (e:Exception)
        {  Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();}




    }
// MODEL FOR THE END TIMES
    fun selectEndTime(view: View) {
        try {


            val calendar = Calendar.getInstance()
            val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)




            val timePickerDialog = object : TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    val selectedEndTime = String.format("%02d:%02d", hourOfDay, minute - minute % 15)
                    selectedEndTimeText.text = selectedEndTime
                },
                hourOfDay,
                minute,
                true
            ) {
                override fun onTimeChanged(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    val roundedMinute = (minute / 15) * 15
                    if (minute != roundedMinute) {
                        view?.minute = roundedMinute
                    }
                }
            }

            timePickerDialog.show()
        }
        catch (e:Exception)
        {  Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();}


    }

// POPULATION OF THE SPINNERS
    private fun tasks() {
        try {


            val myArray = arrayOf(MainActivity.TaskClass.tasks)
            val spinner = findViewById<Spinner>(R.id.category_spinner)

            val db = FirebaseFirestore.getInstance()
            val spinnerList = mutableListOf<String>()

            db.collection("Categories") // Replace with the name of your collection
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val value = document.getString("catname") // Replace with the name of the field you want to add to the spinner
                        if (value != null) {
                            spinnerList.add(value)
                        }
                    }


                    val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerList)
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = spinnerAdapter

                }



            val create = findViewById<Button>(R.id.createTask)
            val task = findViewById<EditText>(R.id.taskNameInput)
            val description = findViewById<EditText>(R.id.taskDescriptionInput)
            val start = findViewById<TextView>(R.id.selectedDateText)
            val time = findViewById<TextView>(R.id.selectedStartTimeText)
            val end = findViewById<TextView>(R.id.selectedEndTimeText)
            val minimum = findViewById<Spinner>(R.id.minimumGoalSpinner)


            val maximumGoalSpinner = findViewById<Spinner>(R.id.maximumGoalSpinner)
            val spinnerArray = (1..24).toList()
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerArray)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            maximumGoalSpinner.adapter = adapter



            val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerArray)
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            minimum.adapter = adapter2




            create.setOnClickListener {

try {
    val max = maximumGoalSpinner.selectedItem.toString()
    val min = minimum.selectedItem.toString()
    val selectedItem = spinner.selectedItem.toString()
    if (task.text.toString().isNullOrEmpty()) {
        val message = "ERROR: TASK NAME CAN NOT BE EMPTY "
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    } else if (description.text.toString().isNullOrEmpty()) {
        val message = "ERROR: DESCRIPTION CAN NOT BE EMPTY "
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    } else if (start.text.toString().isNullOrEmpty()) {
        val message = "ERROR: START DATE CAN NOT BE EMPTY "
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    } else if (selectedItem.toString().isNullOrEmpty()) {
        val message = "ERROR: START DATE CAN NOT BE EMPTY "
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    } else if (max.toString().isNullOrEmpty()) {
        val message = "ERROR: START DATE CAN NOT BE EMPTY "
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    } else if (min.toString().isNullOrEmpty()) {
        val message = "ERROR: START DATE CAN NOT BE EMPTY "
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    } else if (MainActivity.TaskClass.tasks[0][0].isNullOrEmpty()) {
        MainActivity.TaskClass.tasks[0][0] = task.text.toString().trim()
        MainActivity.TaskClass.tasks[0][1] = selectedItem.trim()
        MainActivity.TaskClass.tasks[0][2] = description.text.toString().trim()
        MainActivity.TaskClass.tasks[0][3] = start.text.toString().trim()
        MainActivity.TaskClass.tasks[0][4] = time.text.toString().trim()
        MainActivity.TaskClass.tasks[0][5] = end.text.toString().trim()
        MainActivity.TaskClass.tasks[0][6] = minimum.selectedItem.toString().trim()
        MainActivity.TaskClass.tasks[0][7] = max.trim()


        val start = time.text.toString().replace(Regex("[^\\w\\s]"), "")
        val end = end.text.toString().replace(Regex("[^\\w\\s]"), "")
        val result = Integer.parseInt(end) - Integer.parseInt(start)
        val symbol = "-"
        val outputs = result.toString().removePrefix(symbol)
        MainActivity.TaskClass.hours[1][4] = outputs
        val middleIndex = outputs.length / 2
        val parsed =
            outputs.substring(0, middleIndex) + ":" + outputs.substring(middleIndex)
        result.toString().substring(middleIndex)




        MainActivity.TaskClass.hours[0][0] = parsed

        val message = "TASK ${task.text} ADDED "
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    } else if (MainActivity.TaskClass.tasks[1][0].isNullOrEmpty()) {
        MainActivity.TaskClass.tasks[1][0] = task.text.toString().trim()
        MainActivity.TaskClass.tasks[1][1] = selectedItem.trim()
        MainActivity.TaskClass.tasks[1][2] = description.text.toString().trim()
        MainActivity.TaskClass.tasks[1][3] = start.text.toString().trim()
        MainActivity.TaskClass.tasks[1][4] = time.text.toString().trim()
        MainActivity.TaskClass.tasks[1][5] = end.text.toString().trim()
        MainActivity.TaskClass.tasks[1][6] = min.trim()
        MainActivity.TaskClass.tasks[1][7] = max.trim()


        val time = time.text.toString().replace(Regex("[^\\w\\s]"), "")
        val times = end.text.toString().replace(Regex("[^\\w\\s]"), "")
        val result = Integer.parseInt(times) - Integer.parseInt(time)
        val symbol = "-"
        val outputs = result.toString().removePrefix(symbol)
        MainActivity.TaskClass.hours[0][2] = outputs
        val middleIndex = outputs.length / 2
        val parsed =
            outputs.substring(0, middleIndex) + ":" + outputs.substring(middleIndex)
        result.toString().substring(middleIndex)

        MainActivity.TaskClass.hours[1][0] = parsed.trim()

        val message = "TASK ${task.text} ADDED "
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()


    } else if (MainActivity.TaskClass.tasks[2][0].isNullOrEmpty()) {
        MainActivity.TaskClass.tasks[2][0] = task.text.toString().trim()
        MainActivity.TaskClass.tasks[2][1] = selectedItem.trim()
        MainActivity.TaskClass.tasks[2][2] = description.text.toString().trim()
        MainActivity.TaskClass.tasks[2][3] = start.text.toString().trim()
        MainActivity.TaskClass.tasks[2][4] = time.text.toString().trim()
        MainActivity.TaskClass.tasks[2][5] = end.text.toString().trim()
        MainActivity.TaskClass.tasks[2][6] = min.trim()
        MainActivity.TaskClass.tasks[2][7] = max.trim()


        val timeline = time.text.toString().replace(Regex("[^\\w\\s]"), "")
        val timeline2 = end.text.toString().replace(Regex("[^\\w\\s]"), "")
        val result = Integer.parseInt(timeline2) - Integer.parseInt(timeline)
        val symbol = "-"
        val outputs = result.toString().removePrefix(symbol)
        MainActivity.TaskClass.hours[0][3] = outputs
        val middleIndex = outputs.length / 2
        val parsed =
            outputs.substring(0, middleIndex) + ":" + outputs.substring(middleIndex)
        result.toString().substring(middleIndex)

        MainActivity.TaskClass.hours[2][0] = parsed.trim()

        val message = "TASK ${task.text} ADDED "
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    } else if (MainActivity.TaskClass.tasks[3][0].isNullOrEmpty()) {
        MainActivity.TaskClass.tasks[3][0] = task.text.toString().trim()
        MainActivity.TaskClass.tasks[3][1] = selectedItem.trim()
        MainActivity.TaskClass.tasks[3][2] = description.text.toString().trim()
        MainActivity.TaskClass.tasks[3][3] = start.text.toString().trim()
        MainActivity.TaskClass.tasks[3][4] = time.text.toString().trim()
        MainActivity.TaskClass.tasks[3][5] = end.text.toString().trim()
        MainActivity.TaskClass.tasks[3][6] = min.trim()
        MainActivity.TaskClass.tasks[3][7] = max.trim()

        val timeline = time.text.toString().replace(Regex("[^\\w\\s]"), "")
        val timeline2 = end.text.toString().replace(Regex("[^\\w\\s]"), "")
        val result = Integer.parseInt(timeline2) - Integer.parseInt(timeline)
        val symbol = "-"
        val outputs = result.toString().removePrefix(symbol)
        MainActivity.TaskClass.hours[0][4] = outputs
        val middleIndex = outputs.length / 2
        val parsed =
            outputs.substring(0, middleIndex) + ":" + outputs.substring(middleIndex)
        result.toString().substring(middleIndex)

        MainActivity.TaskClass.hours[3][0] = parsed.trim()

        val message = "TASK ${task.text} ADDED "
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    } else if (MainActivity.TaskClass.tasks[4][0].isNullOrEmpty()) {
        MainActivity.TaskClass.tasks[4][0] = task.text.toString().trim()
        MainActivity.TaskClass.tasks[4][1] = selectedItem.trim()
        MainActivity.TaskClass.tasks[4][2] = description.text.toString().trim()
        MainActivity.TaskClass.tasks[4][3] = start.text.toString().trim()
        MainActivity.TaskClass.tasks[4][4] = time.text.toString().trim()
        MainActivity.TaskClass.tasks[4][5] = end.text.toString().trim()
        MainActivity.TaskClass.tasks[4][6] = min.trim()
        MainActivity.TaskClass.tasks[4][7] = max.trim()


        val timeline = time.text.toString().replace(Regex("[^\\w\\s]"), "")
        val timeline2 = end.text.toString().replace(Regex("[^\\w\\s]"), "")
        val result = Integer.parseInt(timeline2) - Integer.parseInt(timeline)
        val symbol = "-"
        val outputs = result.toString().removePrefix(symbol)
        MainActivity.TaskClass.hours[0][5] = outputs
        val middleIndex = outputs.length / 2
        val parsed =
            outputs.substring(0, middleIndex) + ":" + outputs.substring(middleIndex)
        result.toString().substring(middleIndex)

        MainActivity.TaskClass.hours[4][0] = parsed.trim()

        val message = "TASK ${task.text} ADDED "
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    } else if (MainActivity.TaskClass.tasks[5][0].isNullOrEmpty()) {
        MainActivity.TaskClass.tasks[5][0] = task.text.toString().trim()
        MainActivity.TaskClass.tasks[5][1] = selectedItem.trim()
        MainActivity.TaskClass.tasks[5][2] = description.text.toString().trim()
        MainActivity.TaskClass.tasks[5][3] = start.text.toString().trim()
        MainActivity.TaskClass.tasks[5][4] = time.text.toString().trim()
        MainActivity.TaskClass.tasks[5][5] = end.text.toString().trim()
        MainActivity.TaskClass.tasks[5][6] = min.trim()
        MainActivity.TaskClass.tasks[5][7] = max.trim()


        val timeline = time.text.toString().replace(Regex("[^\\w\\s]"), "")
        val timeline2 = end.text.toString().replace(Regex("[^\\w\\s]"), "")
        val result = Integer.parseInt(timeline2) - Integer.parseInt(timeline)
        val symbol = "-"
        val outputs = result.toString().removePrefix(symbol)
        MainActivity.TaskClass.hours[0][6] = outputs
        val middleIndex = outputs.length / 2
        val parsed =
            outputs.substring(0, middleIndex) + ":" + outputs.substring(middleIndex)
        result.toString().substring(middleIndex)

        MainActivity.TaskClass.hours[5][0] = parsed.trim()

        val message = "TASK ${task.text} ADDED "
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    } else if (MainActivity.TaskClass.tasks[6][0].isNullOrEmpty()) {
        MainActivity.TaskClass.tasks[6][0] = task.text.toString().trim()
        MainActivity.TaskClass.tasks[6][1] = selectedItem.trim()
        MainActivity.TaskClass.tasks[6][2] = description.text.toString().trim()
        MainActivity.TaskClass.tasks[6][3] = start.text.toString().trim()
        MainActivity.TaskClass.tasks[6][4] = time.text.toString().trim()
        MainActivity.TaskClass.tasks[6][5] = end.text.toString().trim()
        MainActivity.TaskClass.tasks[6][6] = min.trim()
        MainActivity.TaskClass.tasks[6][7] = max.trim()


        val timeline = time.text.toString().replace(Regex("[^\\w\\s]"), "")
        val timeline2 = end.text.toString().replace(Regex("[^\\w\\s]"), "")
        val result = Integer.parseInt(timeline2) - Integer.parseInt(timeline)
        val symbol = "-"
        val outputs = result.toString().removePrefix(symbol)
        MainActivity.TaskClass.hours[0][7] = outputs
        val middleIndex = outputs.length / 2
        val parsedstring =
            outputs.substring(0, middleIndex) + ":" + outputs.substring(middleIndex)
        result.toString().substring(middleIndex)

        MainActivity.TaskClass.hours[6][0] = parsedstring.trim()


        val message = "TASK ${task.text} ADDED "
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    } else if (MainActivity.TaskClass.tasks[7][0].isNullOrEmpty()) {
        MainActivity.TaskClass.tasks[7][0] = task.text.toString().trim()
        MainActivity.TaskClass.tasks[7][1] = selectedItem.trim()
        MainActivity.TaskClass.tasks[7][2] = description.text.toString().trim()
        MainActivity.TaskClass.tasks[7][3] = start.text.toString().trim()
        MainActivity.TaskClass.tasks[7][4] = time.text.toString().trim()
        MainActivity.TaskClass.tasks[7][5] = end.text.toString().trim()
        MainActivity.TaskClass.tasks[7][6] = min.trim()
        MainActivity.TaskClass.tasks[7][7] = max.trim()


        val timeline = time.text.toString().replace(Regex("[^\\w\\s]"), "")
        val timeline2 = end.text.toString().replace(Regex("[^\\w\\s]"), "")
        val result = Integer.parseInt(timeline2) - Integer.parseInt(timeline)
        val symbol = "-"
        val outputs = result.toString().removePrefix(symbol)
        MainActivity.TaskClass.hours[0][8] = outputs
        val middleIndex = outputs.length / 2
        val parsedstring =
            outputs.substring(0, middleIndex) + ":" + outputs.substring(middleIndex)
        result.toString().substring(middleIndex)

        MainActivity.TaskClass.hours[7][0] = parsedstring.trim()

        val message = "TASK ${task.text} ADDED "
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    } else if (MainActivity.TaskClass.tasks[8][0].isNullOrEmpty()) {
        MainActivity.TaskClass.tasks[8][0] = task.text.toString().trim()
        MainActivity.TaskClass.tasks[8][1] = selectedItem.trim()
        MainActivity.TaskClass.tasks[8][2] = description.text.toString().trim()
        MainActivity.TaskClass.tasks[8][3] = start.text.toString().trim()
        MainActivity.TaskClass.tasks[8][4] = time.text.toString().trim()
        MainActivity.TaskClass.tasks[8][5] = end.text.toString().trim()
        MainActivity.TaskClass.tasks[8][6] = min.trim()
        MainActivity.TaskClass.tasks[8][7] = max.trim()


        val timeline = time.text.toString().replace(Regex("[^\\w\\s]"), "")
        val timeline2 = end.text.toString().replace(Regex("[^\\w\\s]"), "")
        val result = Integer.parseInt(timeline2) - Integer.parseInt(timeline)
        val symbol = "-"
        val outputs = result.toString().removePrefix(symbol)
        MainActivity.TaskClass.hours[0][9] = outputs
        val middleIndex = outputs.length / 2
        val parsedstring =
            outputs.substring(0, middleIndex) + ":" + outputs.substring(middleIndex)
        result.toString().substring(middleIndex)

        MainActivity.TaskClass.hours[8][0] = parsedstring.trim()


        val message = "TASK ${task.text} ADDED "
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    } else {
        val message = "ERROR: MAX TASKS REACHED "
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()


    }
}
catch (e:Exception)

{
    val message = "EMPTY FIELDS, PLEASE ENTER DATA "
    Toast.makeText(applicationContext, message.toString(), Toast.LENGTH_SHORT).show();} }


        }
        catch (e:Exception)
        {  Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();}

    }

}