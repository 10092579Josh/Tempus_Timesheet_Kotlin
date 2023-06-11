package com.example.tempus_project

import android.app.TimePickerDialog
import android.app.DatePickerDialog
import java.util.Calendar
import android.view.View
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.tempus_project.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

// THIS IS FOR THE CREATION OF THE TASK
// THIS HAS THE VARIABLE ASSIGNMENT
class TaskForm:AppCompatActivity() {
    private lateinit var selectedDateText: TextView
    private lateinit var selectedStartTimeText: TextView
    private lateinit var selectedEndTimeText: TextView
    private lateinit var uploadPictureBtn: Button
    private lateinit var imgGallery: ImageView
    private val myDataList = mutableListOf<MainTaskActivity.ItemsViewModel>()
    private val customAdapter = MainTaskActivity.CustomAdapter(myDataList)


    companion object {
        val IMAGE_REQUEST_CODE = 100
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            FirebaseApp.initializeApp(this)

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
                getContent.launch("image/*")
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
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // handle the result here
        val task = findViewById<EditText>(R.id.taskNameInput)
        if (uri != null) {
            // display the image in an ImageView
            val imageView = findViewById<ImageView>(R.id.imgGallery)
            imageView.setImageURI(uri)
            val storageRef = Firebase.storage.reference.child("Images/${task.text.toString().trim()}")

            // upload the image to Firebase storage
            val uploadTask = storageRef.putFile(uri)
            uploadTask.addOnSuccessListener {
                // handle successful upload here
            }.addOnFailureListener {
                // handle failed upload here
            }
        }
    }




    // MODEL FOR THE IMAGE


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
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();
        }


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
                    val selectedStartTime =
                        String.format("%02d:%02d", hourOfDay, minute - minute % 15)
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
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();
        }


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
                    val selectedEndTime =
                        String.format("%02d:%02d", hourOfDay, minute - minute % 15)
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
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();
        }


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
                        val value =
                            document.getString("catname") // Replace with the name of the field you want to add to the spinner
                        if (value != null) {
                            spinnerList.add(value)
                        }
                    }


                    val spinnerAdapter =
                        ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerList)
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = spinnerAdapter

                }


            val create = findViewById<Button>(R.id.createTask)
            val task = findViewById<EditText>(R.id.taskNameInput)
            val description = findViewById<EditText>(R.id.taskDescriptionInput)
            val dates = findViewById<TextView>(R.id.selectedDateText)
            val start = findViewById<TextView>(R.id.selectedStartTimeText)
            val end = findViewById<TextView>(R.id.selectedEndTimeText)
            val minimum = findViewById<Spinner>(R.id.minimumGoalSpinner)

            val storageRef = Firebase.storage.reference.child("Images/${task.text.toString().trim()}")

// get the download URL of the uploaded image

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

                    } else if (dates.text.toString().isNullOrEmpty()) {
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
                    } else {
                        var picture: String

                        // Get the current user's unique ID
                        val firestore = Firebase.firestore
                        storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                            picture = downloadUrl.toString()

                            val itemsadd = firestore.collection("Tasks")

                            val taskname = task.text.toString().trim()
                            val catergorytask = selectedItem.trim()
                            val description = description.text.toString().trim()
                            val startime = start.text.toString().trim()
                            val endtime = end.text.toString().trim()

                            val maxgoal = max.trim()
                            val mingoal = minimum.selectedItem.toString().trim()
                            val date = dates.text.toString().trim()

                            val userid = Firebase.auth.currentUser?.uid

                            val start = start.text.toString().replace(Regex("[^\\w\\s]"), "")
                            val end = end.text.toString().replace(Regex("[^\\w\\s]"), "")

                            val result = Integer.parseInt(end) - Integer.parseInt(start)

                            val symbol = "-"
                            val outputs = result.toString().removePrefix(symbol)

                            val middleIndex = outputs.length / 2
                            val parsed =
                                outputs.substring(0, middleIndex) + ":" + outputs.substring(
                                    middleIndex
                                )
                            result.toString().substring(middleIndex)


                            val hours = parsed


                            val message = "TASK ${task.text} ADDED "
                            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()


                            val tasksadd = taskstore(
                                taskname,
                                catergorytask,
                                description,
                                startime,
                                endtime,
                                hours,
                                mingoal,
                                maxgoal,
                                date,
                                picture,
                                userid.toString().trim()
                            )

                            val docRef = itemsadd.document(taskname)
                            docRef.set(tasksadd)


                        }

                    }
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();
                }

            }

        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
