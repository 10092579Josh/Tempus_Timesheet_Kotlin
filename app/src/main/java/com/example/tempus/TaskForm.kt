package com.example.tempus

import android.app.TimePickerDialog
import android.app.DatePickerDialog
import java.util.Calendar
import android.view.View
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.*
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import kotlin.math.absoluteValue

// THIS IS FOR THE CREATION OF THE TASK
// THIS HAS THE VARIABLE ASSIGNMENT
class TaskForm:AppCompatActivity() {
    private lateinit var selectedDateText: TextView
    private lateinit var selectedStartTimeText: TextView
    private lateinit var selectedEndTimeText: TextView
    private lateinit var uploadPictureBtn: Button
    private lateinit var imgGallery: ImageView
    private val myDataList = mutableListOf<Tasks.ItemsViewModel>()
    private val customAdapter = Tasks.CustomAdapter(myDataList)



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
                val task = findViewById<EditText>(R.id.taskNameInput)
                if(task.text.toString().isNullOrEmpty())
                {


                    val message = "TASK MUST BE ENTERED FIRST! "
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()


                }
                else if (task.text.toString() != null) {
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

            }

            // this creates a vertical layout Manager
            addbtn.setOnClickListener()
            {
                val homepage = Intent(this, TaskForm::class.java)
                startActivity(homepage)
                overridePendingTransition(0, 0)
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
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    private val GalleryContent = registerForActivityResult(ActivityResultContracts.GetContent()) { url: Uri? ->


        if (url != null) {
            val task = findViewById<EditText>(R.id.taskNameInput)

            val imageView = findViewById<ImageView>(R.id.imgGallery)
            imageView.setImageURI(url)

            val store =
                Firebase.storage.reference.child(task.text.toString().trim())

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


        val Imagestream = ByteArrayOutputStream()
        photo?.compress(Bitmap.CompressFormat.JPEG, 100, Imagestream)
        val data = Imagestream.toByteArray()

        val UploadDP = ImageRef.putBytes(data)
        UploadDP.addOnSuccessListener {
            val message = "IMAGE UPLOADED "
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            val message = "INVALID IMAGE!"
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }


    fun selectDate(view: View) {
        try {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->

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


            val myArray = arrayOf(Home.TaskClass.tasks)
            val spinner = findViewById<Spinner>(R.id.category_spinner)
            val userid = Firebase.auth.currentUser?.uid
            val db = FirebaseFirestore.getInstance()
            val spinnerList = mutableListOf<String>()


            db.collection("CategoryStorage")
                .whereEqualTo("userIdCat", userid)
                .get()

                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val value =
                            document.getString("categoryID")
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



// get the download URL of the uploaded imageURL

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
                    if (task.text.toString().isEmpty()) {
                        val message = "ERROR: TASK NAME CAN NOT BE EMPTY "
                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

                    } else if (description.text.toString().isEmpty()) {
                        val message = "ERROR: DESCRIPTION CAN NOT BE EMPTY "
                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

                    } else if (dates.text.toString().isEmpty()) {
                        val message = "ERROR: START DATE CAN NOT BE EMPTY "
                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

                    } else if (selectedItem.isEmpty()) {
                        val message = "ERROR: START DATE CAN NOT BE EMPTY "
                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

                    } else if (max.isEmpty()) {
                        val message = "ERROR: START DATE CAN NOT BE EMPTY "
                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

                    } else if (min.isEmpty()) {
                        val message = "ERROR: START DATE CAN NOT BE EMPTY "
                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                    } else {
                        var picture: String

                        // Get the current user's unique ID
                        val firestore = Firebase.firestore
                        val storageRef = Firebase.storage.reference.child(task.text.toString().trim())
                        storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                            picture = downloadUrl.toString()

                            val itemsadd = firestore.collection("TaskStorage")

                            val taskname = task.text.toString().trim()
                            val catergorytask = selectedItem.trim()
                            val tabname = "$catergorytask$taskname"
                            val description = description.text.toString().trim()
                            val startime = start.text.toString().trim()
                            val endtime = end.text.toString().trim()

                            val maxgoal = max.trim()
                            val mingoal = minimum.selectedItem.toString().trim()
                            val date = dates.text.toString().trim()

                            val userid = Firebase.auth.currentUser?.uid
                            val start = start.text.toString().replace(Regex("[^\\w\\s:]"), "")
                            val end = end.text.toString().replace(Regex("[^\\w\\s:]"), "")

                            val startsplit = start.split(":")
                            val sHours = startsplit[0].toInt()
                            val sMinutes = startsplit[1].toInt()

                            val endTimeParts = end.split(":")
                            val endHours = endTimeParts[0].toInt()
                            val endMinutes = endTimeParts[1].toInt()

// convert start and end times to minutes
                            val startTotalMinutes = sHours * 60 + sMinutes
                            val endTotalMinutes = endHours * 60 + endMinutes

// calculate difference in minutes
                            val diffMinutes = (endTotalMinutes - startTotalMinutes).absoluteValue

// convert difference back to duration and minutes
                            val diffHours = diffMinutes / 60
                            val diffRemainingMinutes = diffMinutes % 60

                            val categoryName = selectedItem.trim()

                            val db = Firebase.firestore
                            val categoryRef = db.collection("CategoryStorage").document(categoryName)
                            categoryRef.get()
                                .addOnSuccessListener { document ->
                                    val CategoryHours = document.get("totalHours")
                                    val currentsplit = CategoryHours.toString().split(":")
                                    val HoursValue = currentsplit[0].toInt()
                                    val MinutesValue = currentsplit[1].toInt()

                                    val newTotalMinutes = HoursValue * 60 + MinutesValue + diffMinutes
                                    val newHoursValue = newTotalMinutes / 60
                                    val newRemainingMinutesValue = newTotalMinutes % 60
                                    categoryRef.update(
                                        "totalHours",
                                        "%02d:%02d".format(newHoursValue, newRemainingMinutesValue)
                                    )
                                }

                            val symbol = "-"
                           // val outputs = result.toString().removePrefix(symbol)

                          //  val middleIndex = outputs.length / 2
                            //val parsed = outputs.substring(0, middleIndex) + ":" + outputs.substring( middleIndex )
                          //  result.toString().substring(middleIndex)


                            val hours = "%02d:%02d".format(diffHours, diffRemainingMinutes)


if(picture.isNullOrEmpty()){
    val message = "ERROR NO IMAGE CHOSEN"
    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()}
                            else {
    val tasksadd = TaskStorage(
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
        tabname,
        userid.toString().trim()
    )

    val docRef = itemsadd.document(taskname)
    docRef.set(tasksadd)


    val message = "TASK ${task.text} ADDED "
    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()


}

                        }.addOnFailureListener(){
                            val message = "ERROR NO IMAGE CHOSEN"
                            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                        }

                    }
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
                }

            }

        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}
