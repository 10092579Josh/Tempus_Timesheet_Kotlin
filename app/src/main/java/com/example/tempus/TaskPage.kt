package com.example.tempus


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Locale


// THIS PAGE HANDLES THE DISPLAY OF THE TASKS WHEN A SPECIFIC TASK IS CLICKED
class TaskPage : AppCompatActivity() {

    private var mTextViewCountDown: TextView? = null
    private var mTextViewCountDownBreak: TextView? = null

    private var mButtonSet: ImageView? = null
    private var mButtonSetBreak: ImageView? = null

    private var mButtonStartPause: ImageView? = null
    private var mButtonStartPauseBreak: ImageView? = null

    private var mButtonReset: ImageView? = null
    private var mButtonResetBreak: ImageView? = null

    private var mCountDownTimer: CountDownTimer? = null
    private var mCountDownTimerBreaks: CountDownTimer? = null

    private var mTimerRunning = false
    private var mTimerRunningBreak = false

    private var mStartTimeInMillis: Long = 0
    private var mStartTimeInMillisBreaks: Long = 0

    private var mTimeLeftInMillis: Long = 0
    private var mTimeLeftInMillisBreaks: Long = 0

    private var mEndTime: Long = 0
    private var mEndTimeBreaks: Long = 0

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "your_channel_id"
        const val NOTIFICATION_ID = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.task_page)
            val tasksBack = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val intent = Intent(this@TaskPage, Tasks::class.java)
                    intent.putExtra("home", getIntent().getIntExtra("home", R.layout.home))
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()

                }
            }
            onBackPressedDispatcher.addCallback(this, tasksBack)
            security()
            FirebaseApp.initializeApp(this)
            taskPopulation()
            GetBreaks()
            val taskImage = findViewById<ImageView>(R.id.task_image)
            val homebtn = findViewById<ImageButton>(R.id.hometbtn)
            val breaksbtn = findViewById<ImageButton>(R.id.breakstbtn)
            val statsbtn = findViewById<ImageButton>(R.id.statstbtn)
            val settingsbtn = findViewById<ImageButton>(R.id.settingstbtn)
            val addbtn = findViewById<ImageButton>(R.id.addbtn)

            mTextViewCountDown = findViewById(R.id.text_view_countdown)
            mTextViewCountDownBreak = findViewById(R.id.break_view_countdown)

            mButtonSet = findViewById(R.id.task_set)
            mButtonSetBreak = findViewById(R.id.break_set)

            mButtonStartPause = findViewById(R.id.task_start_pause)
            mButtonStartPauseBreak = findViewById(R.id.break_start_pause)

            mButtonReset = findViewById(R.id.task_reset)
            mButtonResetBreak = findViewById(R.id.break_reset)

            mButtonSet!!.setOnClickListener {
                mButtonStartPause!!.visibility = View.GONE
                mButtonReset!!.visibility = View.GONE

            }

            mButtonSetBreak!!.setOnClickListener {
                mButtonStartPauseBreak!!.visibility = View.GONE
                mButtonResetBreak!!.visibility = View.GONE
            }

            mButtonStartPause!!.setOnClickListener {
                if (mTimerRunning) {
                    pauseTimer()
                } else {
                    startTimer()
                }
            }

            mButtonStartPauseBreak!!.setOnClickListener {
                if (mTimerRunningBreak) {
                    pauseTimerBreak()
                } else {
                    startTimerBreak()
                }
            }

            mButtonReset!!.setOnClickListener {
                resetTimer()
            }


            mButtonResetBreak!!.setOnClickListener {
                resetTimerBreak()
            }

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

    private fun setTime(milliseconds: Long) {
        mStartTimeInMillis = milliseconds
        resetTimer()

    }

    private fun setTimeBreak(milliseconds: Long) {
        mStartTimeInMillisBreaks = milliseconds
        resetTimerBreak()

    }

    private fun startTimer() {
        createNotificationChannel()

        // Build the initial notification without the timer countdown
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Timer Running")
            .setContentText("The timer is currently running.")
            .setSmallIcon(R.drawable.clock)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        // Show the initial notification
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

        // Set the end time for the timer
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis

        // Start the countdown timer
        mCountDownTimer = object : CountDownTimer(mTimeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMillis = millisUntilFinished
                updateCountDownText()
                updateNotification(getFormattedTimeLeft())

                if (mTimeLeftInMillis % (30 * 60 * 1000) == 0L) {
                    val updatedNotificationBuilder =
                        notificationBuilder.setContentText(getFormattedTimeLeft())

                    notificationManager.notify(
                        NOTIFICATION_ID,
                        updatedNotificationBuilder.build()
                    )
                }

                // Update the notification with the current countdown time

            }

            override fun onFinish() {
                mTimerRunning = false
                updateWatchInterface()

                // Update the notification when the timer finishes
                val updatedNotificationBuilder =
                    notificationBuilder.setContentText("Timer finished")
                notificationManager.notify(
                    NOTIFICATION_ID,
                    updatedNotificationBuilder.build()
                )
            }
        }.start()

        mTimerRunning = true
        updateWatchInterface()
    }

    private fun startTimerBreak() {
        createNotificationChannel()
        // Build the initial notification without the timer countdown
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Timer Running")
            .setContentText("The timer is currently running.")
            .setSmallIcon(R.drawable.clock)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Show the initial notification
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        // Set the end time for the timer
        mEndTimeBreaks = System.currentTimeMillis() + mTimeLeftInMillisBreaks
        // Start the countdown timer
        mCountDownTimerBreaks = object : CountDownTimer(mTimeLeftInMillisBreaks, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMillisBreaks = millisUntilFinished
                updateCountDownTextBreaks()
                updateNotification(getFormattedTimeLeftBreak())
                if (mTimeLeftInMillisBreaks % (30 * 60 * 1000) == 0L) {
                    val updatedNotificationBuilder =
                        notificationBuilder.setContentText(getFormattedTimeLeftBreak())
                    notificationManager.notify(
                        NOTIFICATION_ID,
                        updatedNotificationBuilder.build()
                    )
                }
            }

            override fun onFinish() {
                mTimerRunningBreak = false
                updateWatchInterfaceBreak()
                // Update the notification when the timer finishes
                val updatedNotificationBuilder =
                    notificationBuilder.setContentText("Timer finished")
                notificationManager.notify(
                    NOTIFICATION_ID,
                    updatedNotificationBuilder.build()
                )
            }
        }.start()
        mTimerRunningBreak = true
        updateWatchInterfaceBreak()
    }

    private fun updateNotification(contentText: String) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Timer Running")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.clock)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }


    private fun getFormattedTimeLeft(): String {
        val hours = (mTimeLeftInMillis / 1000) / 3600
        val minutes = ((mTimeLeftInMillis / 1000) / 60) % 60
        val seconds = (mTimeLeftInMillis / 1000) % 60
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun getFormattedTimeLeftBreak(): String {
        val hours = (mTimeLeftInMillisBreaks / 1000) / 3600
        val minutes = ((mTimeLeftInMillisBreaks / 1000) / 60) % 60
        val seconds = (mTimeLeftInMillisBreaks / 1000) % 60
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }


    private fun createNotificationChannel() {
        val channelName = "Timer Channel"
        val channelDescription = "Channel for displaying timer notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance)
        channel.description = channelDescription


        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun pauseTimerBreak()
    {
        mCountDownTimerBreaks!!.cancel()
        mTimerRunningBreak = false
        updateWatchInterfaceBreak()
        val currentTime = mTimeLeftInMillisBreaks
        Log.d("YourTag", "PauseDurationBreaks Retrieved: $currentTime")
        val currentTimeFormatted = getFormattedTimeLeft()
        Log.d("YourTag", "PauseDurationBreaks Retrieved: $currentTimeFormatted")
        val itemId = intent.getStringExtra("itemId")
        val db = FirebaseFirestore.getInstance()
        val userid = FirebaseAuth.getInstance().currentUser?.uid
        db.collection("TaskStorage")
            .whereEqualTo("userIdTask", userid.toString().trim())
            .whereEqualTo("taskName", itemId)
            .get()
            .addOnSuccessListener { queryResult ->
                // Get the document object
                val document = queryResult.documents.lastOrNull()
                if (document != null) { val breakdurationVar = document.get("breakDurations")
                    Log.d("YourTag", "PauseDuration Retrieved: $breakdurationVar")
                    val minutes = currentTime / 60000
                    val seconds = (currentTime % 60000) / 1000
                    val timeString = String.format("%02d:%02d", minutes, seconds)

                    Log.d("YourTag", "Timer converted to mins $timeString")

                    // Update the value of completedHours to result using update()
                    val roundedMinutes = if (seconds >= 30) minutes + 1 else minutes
                    document.reference.update("breakDurations", roundedMinutes.toInt())
                    Log.d("YourTag", "Timer converted to mins $roundedMinutes")
                    recreate()

                }
            }
    }

    // Stark Code
    private fun pauseTimer() {
        mCountDownTimer!!.cancel()
        mTimerRunning = false
        updateWatchInterface()
        val currentTime = mTimeLeftInMillis
        val currentTimeFormatted = getFormattedTimeLeft()
        Log.d("YourTag", "PauseDuration Retrieved: $currentTimeFormatted")
        val currentTimeWithoutSeconds = currentTimeFormatted.substringBeforeLast(":")
        Log.d("YourTag", "currentTimeWithoutSeconds: $currentTimeWithoutSeconds")

        val itemId = intent.getStringExtra("itemId")

        val db = FirebaseFirestore.getInstance()

        val userid = FirebaseAuth.getInstance().currentUser?.uid
        db.collection("TaskStorage")
            .whereEqualTo("userIdTask", userid.toString().trim())
            .whereEqualTo("taskName", itemId)
            .get()
            .addOnSuccessListener { queryResult ->
                // Get the document object
                val document = queryResult.documents.lastOrNull()
                if (document != null) {
                    val durationVar = document.getString("timeRemaining")
                    Log.d("YourTag", "PauseDuration Retrieved: $durationVar")

                    // Split the duration string into hours and minutes
                    val parts = durationVar?.split(":")
                    val hours = parts?.getOrNull(0)?.toIntOrNull() ?: 0
                    val minutes = parts?.getOrNull(1)?.toIntOrNull() ?: 0

                    // Calculate the total duration in milliseconds
                    val durationMillis = (hours * 60 * 60 * 1000) + (minutes * 60 * 1000)

                    val currentTimeMillis = currentTimeWithoutSeconds.toMillis()
                    Log.d("YourTag", "Hours done: $currentTimeMillis")
                    val differenceMillis = durationMillis - currentTimeMillis
                    val differenceFormatted = formatTime(differenceMillis)

                    // Store the result in a variable
                    val result = differenceFormatted
                    Log.d("YourTag", "Completed Time: $differenceFormatted")

                    // Update the value of completedHours to result using update()
                    document.reference.update("timeRemaining",currentTimeFormatted)
                    document.reference.update("completedHours", result)

                    val catname = findViewById<TextView>(R.id.category_name)

                    val db = Firebase.firestore
                    val categoryRef =
                        db.collection("CategoryStorage").document(catname.text.toString())
                    categoryRef.get()
                        .addOnSuccessListener { document ->
                            val timeComponents = result.split(":")
                            val hours = timeComponents[0].toInt()
                            val minutes = timeComponents[1].toInt()
                            val totalMinutes = hours * 60 + minutes
                            categoryRef.update(
                                "totalTimeCompleted",totalMinutes

                            )
                        }
                }


            }
    }

    // Stark Code
    private fun String.toMillis(): Long {
        val parts = this.split(":")
        val hours = parts.getOrNull(0)?.toLongOrNull() ?: 0
        val minutes = parts.getOrNull(1)?.toLongOrNull() ?: 0
        return ((hours * 3600) + (minutes * 60)) * 1000
    }



    // Extension function to format milliseconds as a time string
    //Stark Code
    private fun formatTime(millis: Long): String {
        val minutes = (millis / (1000 * 60)) % 60
        val hours = (millis / (1000 * 60 * 60)) % 24
        return String.format("%02d:%02d", hours, minutes)
    }



    private fun resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis
        updateCountDownText()
        updateWatchInterface()
    }
    private fun resetTimerBreak() {
        mTimeLeftInMillisBreaks = mStartTimeInMillisBreaks
        updateCountDownTextBreaks()
        updateWatchInterfaceBreak()
    }


    private fun updateCountDownText() {
        val hours = (mTimeLeftInMillis / 1000).toInt() / 3600
        val minutes = ((mTimeLeftInMillis / 1000) % 3600).toInt() / 60
        val seconds = (mTimeLeftInMillis / 1000).toInt() % 60
        val timeLeftFormatted: String = if (hours > 0) {
            String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        }
        mTextViewCountDown!!.text = timeLeftFormatted
    }
    private fun updateCountDownTextBreaks() {
        val hours = (mTimeLeftInMillisBreaks / 1000).toInt() / 3600
        val minutes = ((mTimeLeftInMillisBreaks / 1000) % 3600).toInt() / 60
        val seconds = (mTimeLeftInMillisBreaks / 1000).toInt() % 60
        val timeLeftFormatted: String = if (hours > 0) {
            String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        }
        mTextViewCountDownBreak!!.text = timeLeftFormatted
    }

    private fun updateWatchInterface() {
        when {
            mTimerRunning -> {
                mButtonSet!!.visibility = View.INVISIBLE
                mButtonReset!!.visibility = View.INVISIBLE
                mButtonStartPause?.setImageResource(R.drawable.pause_icon)

            }

            else -> {

                mButtonSet!!.visibility = View.VISIBLE
                mButtonStartPause?.setImageResource(R.drawable.play_icon)

                when {
                    mTimeLeftInMillis < 1000 -> {
                        mButtonStartPause!!.visibility = View.INVISIBLE
                    }

                    else -> {
                        mButtonStartPause!!.visibility = View.VISIBLE
                    }
                }
                when {
                    mTimeLeftInMillis < mStartTimeInMillis -> {
                        mButtonReset!!.visibility = View.VISIBLE
                    }

                    else -> {
                        mButtonReset!!.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }
    private fun updateWatchInterfaceBreak() {
        when {
            mTimerRunningBreak -> {
                mButtonSetBreak!!.visibility = View.INVISIBLE
                mButtonResetBreak!!.visibility = View.INVISIBLE
                mButtonStartPauseBreak?.setImageResource(R.drawable.pause_icon)
            }
            else -> {
                mButtonSetBreak!!.visibility = View.VISIBLE
                mButtonStartPauseBreak?.setImageResource(R.drawable.play_icon)
                when {
                    mTimeLeftInMillisBreaks < 1000 -> {
                        mButtonStartPauseBreak!!.visibility = View.INVISIBLE
                    }

                    else -> {
                        mButtonStartPauseBreak!!.visibility = View.VISIBLE
                    }
                }
                when {
                    mTimeLeftInMillisBreaks < mStartTimeInMillisBreaks -> {
                        mButtonResetBreak!!.visibility = View.VISIBLE
                    }

                    else -> {
                        mButtonResetBreak!!.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }



    override fun onStop() {
        super.onStop()
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putLong("startTimeInMillis", mStartTimeInMillis)
        editor.putLong("millisLeft", mTimeLeftInMillis)
        editor.putBoolean("timerRunning", mTimerRunning)
        editor.putLong("endTime", mEndTime)
        editor.apply()
        when {
            mCountDownTimer != null -> {
                mCountDownTimer!!.cancel()
            }
        }
    }

    fun onStopBreak() {

        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putLong("startTimeInMillisBreak", mStartTimeInMillisBreaks)
        editor.putLong("millisLeftBreak", mTimeLeftInMillisBreaks)
        editor.putBoolean("timerRunningBreak", mTimerRunningBreak)
        editor.putLong("endTimeBreak", mEndTimeBreaks)
        editor.apply()
        when {
            mCountDownTimerBreaks != null -> {
                mCountDownTimerBreaks!!.cancel()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 600000)
        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis)
        mTimerRunning = prefs.getBoolean("timerRunning", false)
        updateCountDownText()
        updateWatchInterface()
        when {
            mTimerRunning -> {
                mEndTime = prefs.getLong("endTime", 0)
                mTimeLeftInMillis = mEndTime - System.currentTimeMillis()
                when {
                    mTimeLeftInMillis < 0 -> {
                        mTimeLeftInMillis = 0
                        mTimerRunning = false
                        updateCountDownText()
                        updateWatchInterface()
                    }

                    else -> {
                        startTimer()
                    }
                }
            }
        }
    }
    fun onStartBreak() {

        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        mStartTimeInMillisBreaks = prefs.getLong("startTimeInMillisBreak", 600000)
        mTimeLeftInMillisBreaks = prefs.getLong("millisLeftBreak", mStartTimeInMillisBreaks)
        mTimerRunningBreak = prefs.getBoolean("timerRunningBreak", false)
        updateCountDownTextBreaks()
        updateWatchInterfaceBreak()
        when {
            mTimerRunningBreak -> {
                mEndTimeBreaks = prefs.getLong("endTimeBreak", 0)
                mTimeLeftInMillisBreaks = mEndTimeBreaks - System.currentTimeMillis()
                when {
                    mTimeLeftInMillisBreaks < 0 -> {
                        mTimeLeftInMillisBreaks = 0
                        mTimerRunningBreak = false
                        updateCountDownTextBreaks()
                        updateWatchInterfaceBreak()
                    }

                    else -> {
                        startTimerBreak()
                    }
                }
            }
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
            when {
                task.isSuccessful -> {
                    //stuff to do

                }

                else -> {
                    when (val exception = task.exception) {
                        is FirebaseAuthInvalidUserException -> {
                            when (exception.errorCode) {
                                "ERROR_USER_NOT_FOUND" -> {
                                    val sharedPreferences =
                                        getSharedPreferences("preferences", Context.MODE_PRIVATE)
                                    sharedPreferences.edit().putBoolean("isFirstLogin", true)
                                        .apply()
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
            }
        }

    }
    private fun GetBreaks()
    {
        val itemId = intent.getStringExtra("itemId")
        Log.d("YourTag", "ItemID: " + itemId)
        //THIS INDEX LETS THE FOR LOOP SORT THE SPECIFIC INDEX WHICH WONT CHANGE AS PER THE POSITION WHICH WILL CHANGE
        val db = FirebaseFirestore.getInstance()
        val userid = FirebaseAuth.getInstance().currentUser?.uid
        Log.d("YourTag", "userid: " + userid)
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
                    val  breakVal = document.getLong("breakDurations")
                    Log.d("YourTag", "BreakVal: " + breakVal)
                    val breakValInMilliseconds = breakVal?.times(60000)
                    Log.d("YourTag", "BreakVal: " + breakValInMilliseconds)
                    if (breakValInMilliseconds != null) {
                        setTimeBreak(breakValInMilliseconds)
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
                        val timerem = document.getString("timeRemaining")
                        Log.d("YourTag", "Duration: " + hours2.text)

                        min.text = document.getString("minGoal")
                        max.text = document.getString("maxGoal")
                        date.text = document.getString("dateAdded")
                        val url = document.getString("imageURL")
                        val durationParts = timerem?.split(":")
                        val hours = durationParts!![0].toLong()
                        val minutes = durationParts!![1].toLong()
                        val durationInMillis = hours * 60 * 60 * 1000 + minutes * 60 * 1000
                        setTime(durationInMillis)

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


            when {
                url != null -> {
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
