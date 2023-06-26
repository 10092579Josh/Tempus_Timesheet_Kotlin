package com.example.tempus

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import de.keyboardsurfer.android.widget.crouton.Crouton
import de.keyboardsurfer.android.widget.crouton.Style
import java.io.ByteArrayOutputStream
import java.util.Calendar
import kotlin.math.absoluteValue

// THIS IS FOR THE CREATION OF THE TASK
// THIS HAS THE VARIABLE ASSIGNMENT
class TaskForm : AppCompatActivity() {
    private val e = Errors()
    private lateinit var selectedDateText: TextView
    private lateinit var selectedStartTimeText: TextView
    private lateinit var selectedEndTimeText: TextView

    private val catEmpty = Crouton.makeText(this, e.emptyCat, Style.ALERT)
    private val taskEmpty = Crouton.makeText(this, e.emptyTaskName, Style.ALERT)
    private val noMing = Crouton.makeText(this, e.noMinGoal, Style.ALERT)
    private val noMx = Crouton.makeText(this, e.noMaxGoal, Style.ALERT)
    private val emptyBody = Crouton.makeText(this, e.emptyDesc, Style.ALERT)
    private val sDate = Crouton.makeText(this, e.noStartDate, Style.ALERT)
    private val sTime = Crouton.makeText(this, e.startTimeNotChosen, Style.ALERT)
    private val eTime = Crouton.makeText(this, e.endTimeNotChosen, Style.ALERT)

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            FirebaseApp.initializeApp(this)
            security()
            super.onCreate(savedInstanceState)
            setContentView(R.layout.task_form)
            val tasksBack = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val intent = Intent(this@TaskForm, Tasks::class.java)
                    intent.putExtra("home", getIntent().getIntExtra("home", R.layout.home))
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()


                }
            }
            onBackPressedDispatcher.addCallback(this, tasksBack)

            selectedDateText = findViewById(R.id.selectedDateText)
            selectedStartTimeText = findViewById(R.id.selectedStartTimeText)
            selectedEndTimeText = findViewById(R.id.selectedEndTimeText)


            tasks()
            val uploadImage = findViewById<ImageView>(R.id.imgGallery)
            val homebtn = findViewById<ImageButton>(R.id.hometbtn)
            val breaksbtn = findViewById<ImageButton>(R.id.breakstbtn)
            val statsbtn = findViewById<ImageButton>(R.id.statstbtn)
            val settingsbtn = findViewById<ImageButton>(R.id.settingstbtn)

            val addbtn = findViewById<ImageButton>(R.id.addbtn)

            uploadImage.setOnClickListener {
                val task = findViewById<EditText>(R.id.taskNameInput)
                if (task.text.toString().isEmpty()) {


                    val message = "TASK MUST BE ENTERED FIRST! "
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()


                } else {
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
                val intent = Intent(this@TaskForm, Login::class.java)
                intent.putExtra("login", R.layout.login)
                overridePendingTransition(0, 0)
                startActivity(intent)

            }
        }

        val user = FirebaseAuth.getInstance().currentUser
        user?.reload()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // stuff to do

            } else {
                val exception = task.exception
                if (exception is FirebaseAuthInvalidUserException) {
                    val errorCode = exception.errorCode
                    if (errorCode == "ERROR_USER_NOT_FOUND") {
                        val sharedPreferences =
                            getSharedPreferences("preferences", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putBoolean("isFirstLogin", true).apply()
                        AppSettings.Preloads.userSName = null
                        val intent = Intent(this@TaskForm, Login::class.java)
                        intent.putExtra("login", R.layout.login)
                        overridePendingTransition(0, 0)
                        startActivity(intent)
                    }
                }
            }
        }

    }

    private val galleryContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { url: Uri? ->


            when {
                url != null -> {
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
        }
    private val camera =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { photo: Bitmap? ->

            val task = findViewById<EditText>(R.id.taskNameInput)

            val imageView = findViewById<ImageView>(R.id.imgGallery)
            imageView.setImageBitmap(photo)


            val imageRef = Firebase.storage.reference.child(task.text.toString().trim())


            val imageStream = ByteArrayOutputStream()
            photo?.compress(Bitmap.CompressFormat.JPEG, 100, imageStream)
            val data = imageStream.toByteArray()

            val uploadDP = imageRef.putBytes(data)
            uploadDP.addOnSuccessListener {
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
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
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
                    when {
                        minute != roundedMinute -> {
                            view?.minute = roundedMinute
                        }
                    }
                }
            }

            timePickerDialog.show()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
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
                    when {
                        minute != roundedMinute -> {
                            view?.minute = roundedMinute
                        }
                    }
                }
            }

            timePickerDialog.show()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
        }


    }

    // POPULATION OF THE SPINNERS
    private fun tasks() {
        try {

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
            val breaks = findViewById<Spinner>(R.id.breaks_spinner)


            val breaksArray = (1..6).toList()
            val breaksAdapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, breaksArray)
            breaksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            breaks.adapter = breaksAdapter

            val maximumGoalSpinner = findViewById<Spinner>(R.id.maximumGoalSpinner)
            val maxArray = (1..24).toList()
            val maxAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, maxArray)
            maxAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            maximumGoalSpinner.adapter = maxAdapter


            val minAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, maxArray)
            minAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            minimum.adapter = minAdapter




            create.setOnClickListener {

                try {
                    val max = maximumGoalSpinner.selectedItem.toString()
                    val min = minimum.selectedItem.toString()
                    val selectedItem = spinner.selectedItem.toString()
                    when {
                        task.text.toString().isEmpty() -> {
                            taskEmpty.show()
                        }

                        description.text.toString().isEmpty() -> {
                            emptyBody.show()
                        }

                        start.text.toString().isEmpty() -> {

                            sTime.show()
                        }

                        end.text.toString().isEmpty() -> {
                            eTime.show()

                        }

                        dates.text.toString().isEmpty() -> {
                            sDate.show()

                        }

                        selectedItem.isEmpty() -> {
                            catEmpty.show()

                        }

                        max.isEmpty() -> {
                            noMx.show()
                        }

                        min.isEmpty() -> {
                            noMing.show()
                        }

                        else -> {
                            var picture: String


                            val firestore = Firebase.firestore
                            val storageRef =
                                Firebase.storage.reference.child(task.text.toString().trim())
                            storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                picture = downloadUrl.toString()

                                val itemsAdd = firestore.collection("TaskStorage")

                                val taskName = task.text.toString().trim()
                                val categoryTask = selectedItem.trim()
                                val tabName = "$categoryTask$taskName"
                                val description = description.text.toString().trim()
                                val startTime = start.text.toString().trim()
                                val endTime = end.text.toString().trim()

                                val maxGoal = max.trim()
                                val mingoal = minimum.selectedItem.toString().trim()
                                val date = dates.text.toString().trim()

                                val completedHours = 0
                                val breaksHours = 0
                                val userid = Firebase.auth.currentUser?.uid
                                val start = start.text.toString().replace(Regex("[^\\w\\s:]"), "")
                                val end = end.text.toString().replace(Regex("[^\\w\\s:]"), "")

                                val startSplit = start.split(":")
                                val sHours = startSplit[0].toInt()
                                val sMinutes = startSplit[1].toInt()

                                val endTimeParts = end.split(":")
                                val endHours = endTimeParts[0].toInt()
                                val endMinutes = endTimeParts[1].toInt()

                                val startTotalMinutes = sHours * 60 + sMinutes
                                val endTotalMinutes = endHours * 60 + endMinutes


                                val diffMinutes =
                                    (endTotalMinutes - startTotalMinutes).absoluteValue

                                val diffHours = diffMinutes / 60
                                val diffRemainingMinutes = diffMinutes % 60

                                val categoryName = selectedItem.trim()

                                val db = Firebase.firestore
                                val categoryRef =
                                    db.collection("CategoryStorage").document(categoryName)
                                categoryRef.get()
                                    .addOnSuccessListener { document ->
                                        val categoryHours = document.get("totalHours")
                                        val currentSplit = categoryHours.toString().split(":")
                                        val hoursValue = currentSplit[0].toInt()
                                        val minutesValue = currentSplit[1].toInt()

                                        val newTotalMinutes =
                                            hoursValue * 60 + minutesValue + diffMinutes
                                        val newHoursValue = newTotalMinutes / 60
                                        val newRemainingMinutesValue = newTotalMinutes % 60
                                        categoryRef.update(
                                            "totalHours",
                                            "%02d:%02d".format(
                                                newHoursValue,
                                                newRemainingMinutesValue
                                            )
                                        )
                                    }


                                val hours = "%02d:%02d".format(diffHours, diffRemainingMinutes)


                                when {
                                    picture.isEmpty() -> {
                                        val message = "ERROR NO IMAGE CHOSEN"
                                        Toast.makeText(
                                            applicationContext,
                                            message,
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }

                                    else -> {
                                        val tasksAdd = TaskStorage(
                                            taskName,
                                            categoryTask,
                                            description,
                                            startTime,
                                            endTime,
                                            hours,
                                            mingoal,
                                            maxGoal,
                                            date,
                                            picture,
                                            tabName,
                                            breaksHours.toString(),
                                            completedHours.toString(),
                                            userid.toString().trim()
                                        )

                                        val docRef = itemsAdd.document(taskName)
                                        docRef.set(tasksAdd)


                                        val message = "TASK ${task.text} ADDED "
                                        Toast.makeText(
                                            applicationContext,
                                            message,
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()


                                    }
                                }

                            }.addOnFailureListener {
                                val message = "ERROR NO IMAGE CHOSEN"
                                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
                                    .show()
                            }

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
