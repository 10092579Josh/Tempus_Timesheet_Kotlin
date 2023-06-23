package com.example.tempus

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import java.util.Locale

class Breaks : AppCompatActivity() {
    private var mEditTextInput: EditText? = null
    private var mTextViewCountDown: TextView? = null
    private var mButtonSet: Button? = null
    private var mButtonStartPause: Button? = null
    private var mButtonReset: Button? = null
    private var mCountDownTimer: CountDownTimer? = null
    private var mTimerRunning = false
    private var mStartTimeInMillis: Long = 0
    private var mTimeLeftInMillis: Long = 0
    private var mEndTime: Long = 0

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "your_channel_id"
        const val NOTIFICATION_ID = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.breaks)
        security()
        val home = findViewById<ImageButton>(R.id.hometbtn)
        val breaks = findViewById<ImageButton>(R.id.breakstbtn)
        val stats = findViewById<ImageButton>(R.id.statstbtn)
        val settings = findViewById<ImageButton>(R.id.settingstbtn)
        val add = findViewById<ImageButton>(R.id.addbtn)
        // Linking the buttons
        mEditTextInput = findViewById(R.id.edit_text_input)
        mTextViewCountDown = findViewById(R.id.text_view_countdown)
        mButtonSet = findViewById(R.id.button_set)
        mButtonStartPause = findViewById(R.id.button_start_pause)
        mButtonReset = findViewById(R.id.button_reset)
        mButtonSet!!.setOnClickListener {
            val input = mEditTextInput!!.text.toString()
            if (input.isEmpty()) {
                Toast.makeText(this@Breaks, "Field can't be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val millisInput = input.toLong() * 60000
            if (millisInput == 0L) {
                Toast.makeText(this@Breaks, "Please enter a positive number", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            setTime(millisInput)
            mEditTextInput!!.setText("")
        }
        mButtonStartPause!!.setOnClickListener {
            if (mTimerRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }
        mButtonReset!!.setOnClickListener {
            resetTimer()
        }




        home.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.putExtra("home", getIntent().getIntExtra("home", R.layout.home))
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()

        }

        breaks.setOnClickListener {
            val breakspage = Intent(this, Breaks::class.java)
            startActivity(breakspage)
            overridePendingTransition(0, 0)
            finish()
        }

        stats.setOnClickListener {
            val statspage = Intent(this, Statistics::class.java)
            startActivity(statspage)
            overridePendingTransition(0, 0)
            finish()
        }

        settings.setOnClickListener {
            val settingspage = Intent(this, AppSettings::class.java)
            startActivity(settingspage)
            overridePendingTransition(0, 0)
            finish()
        }
        add.setOnClickListener()
        {
            val taskform = Intent(this, TaskForm::class.java)
            startActivity(taskform)
            overridePendingTransition(0, 0)
            finish()

        }

    }

    private fun setTime(milliseconds: Long) {
        mStartTimeInMillis = milliseconds
        resetTimer()
        closeKeyboard()
    }

    private fun startTimer() {
        createNotificationChannel()

        // Build the initial notification without the timer countdown
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Timer Running")
            .setContentText("The timer is currently running.")
            .setSmallIcon(R.drawable.imageuser)
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

                // Update the notification with the current countdown time
                val updatedNotificationBuilder =
                    notificationBuilder.setContentText(getFormattedTimeLeft())
                notificationManager.notify(NOTIFICATION_ID, updatedNotificationBuilder.build())
            }

            override fun onFinish() {
                mTimerRunning = false
                updateWatchInterface()

                // Update the notification when the timer finishes
                val updatedNotificationBuilder =
                    notificationBuilder.setContentText("Timer finished")
                notificationManager.notify(NOTIFICATION_ID, updatedNotificationBuilder.build())
            }
        }.start()

        mTimerRunning = true
        updateWatchInterface()
    }

    private fun updateNotification(contentText: String) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Timer Running")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.imageuser)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVibrate(longArrayOf(0L)) // Disable vibration

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }


    private fun getFormattedTimeLeft(): String {
        val minutes = (mTimeLeftInMillis / 1000) / 60
        val seconds = (mTimeLeftInMillis / 1000) % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
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

    private fun pauseTimer() {
        mCountDownTimer!!.cancel()
        mTimerRunning = false
        updateWatchInterface()
    }

    private fun resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis
        updateCountDownText()
        updateWatchInterface()

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

    private fun updateWatchInterface() {
        when {
            mTimerRunning -> {
                mEditTextInput!!.visibility = View.INVISIBLE
                mButtonSet!!.visibility = View.INVISIBLE
                mButtonReset!!.visibility = View.INVISIBLE
                mButtonStartPause!!.text = "Pause"
            }

            else -> {
                mEditTextInput!!.visibility = View.VISIBLE
                mButtonSet!!.visibility = View.VISIBLE
                mButtonStartPause!!.text = "Start"
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

    private fun closeKeyboard() {
        val view = currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
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


    private fun security() {

        val auth = FirebaseAuth.getInstance()
        auth.addAuthStateListener { firebaseAuth ->
            when (firebaseAuth.currentUser) {
                null -> {

                    val sharedPreferences =
                        getSharedPreferences("preferences", Context.MODE_PRIVATE)
                    sharedPreferences.edit().putBoolean("isFirstLogin", true).apply()
                    AppSettings.Preloads.userSName = null
                    val intent = Intent(this@Breaks, Login::class.java)
                    intent.putExtra("login", R.layout.login)
                    overridePendingTransition(0, 0)
                    startActivity(intent)

                }
            }
        }

        val user = FirebaseAuth.getInstance().currentUser
        user?.reload()?.addOnCompleteListener { task ->
            when {
                task.isSuccessful -> {
                    // stuff to do

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
                                    val intent = Intent(this@Breaks, Login::class.java)
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
}