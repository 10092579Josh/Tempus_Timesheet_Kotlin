package com.example.tempus

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class Statistics : AppCompatActivity() {

    lateinit var lineChart: LineChart
    private val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
    private val startDate: Calendar = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.MONTH, Calendar.FEBRUARY)
    }
    private val endDate: Calendar = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 28)
        set(Calendar.MONTH, Calendar.FEBRUARY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.statistics)
        Security()
        FirebaseApp.initializeApp(this)


        //Initialzation of bottom navigation buttons
        val homebtn = findViewById<ImageButton>(R.id.hometbtn)
        val breaksbtn = findViewById<ImageButton>(R.id.breakstbtn)
        val statsbtn = findViewById<ImageButton>(R.id.statstbtn)
        val settingsbtn = findViewById<ImageButton>(R.id.settingstbtn)
        val addbtn = findViewById<ImageButton>(R.id.addbtn)
        lineChart = findViewById(R.id.statsChart)

        setupLineChart()

        addbtn.setOnClickListener() {
            val tform = Intent(this, TaskForm::class.java)
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
            startActivity(breakspage)
            overridePendingTransition(0, 0)
            finish();
        }

        statsbtn.setOnClickListener {
            val statspage = Intent(this, Statistics::class.java)
            startActivity(statspage)
            overridePendingTransition(0, 0)
            finish();
        }

        settingsbtn.setOnClickListener {
            val settingspage = Intent(this, AppSettings::class.java)
            startActivity(settingspage)
            overridePendingTransition(0, 0)
            finish();
        }


    }

    fun Security() {

        val auth = FirebaseAuth.getInstance()
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {

                val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
                sharedPreferences.edit().putBoolean("isFirstLogin", true).apply()
                AppSettings.preloads.usersname = null
                val intent = Intent(this@Statistics, Login::class.java)
                intent.putExtra("login", R.layout.login)
                overridePendingTransition(0, 0)
                startActivity(intent)

            }
        }

        val user = FirebaseAuth.getInstance().currentUser
        user?.reload()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {

            } else {
                val exception = task.exception
                if (exception is FirebaseAuthInvalidUserException) {
                    val errorCode = exception.errorCode
                    if (errorCode == "ERROR_USER_NOT_FOUND") {
                        val sharedPreferences =
                            getSharedPreferences("preferences", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putBoolean("isFirstLogin", true).apply()
                        AppSettings.preloads.usersname = null
                        val intent = Intent(this@Statistics, Login::class.java)
                        intent.putExtra("login", R.layout.login)
                        overridePendingTransition(0, 0)
                        startActivity(intent)
                    }
                }
            }
        }

    }

    private fun setupLineChart() {
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.xAxis.valueFormatter = object : IndexAxisValueFormatter() {
            override fun getFormattedValue(value: Float, axis: AxisBase?): String {
                val currentDate = startDate.clone() as Calendar
                currentDate.add(Calendar.DAY_OF_MONTH, value.toInt())
                return dateFormat.format(currentDate.time)
            }
        }

        lineChart.xAxis.setDrawGridLines(false)
        lineChart.xAxis.setDrawAxisLine(true)
        lineChart.xAxis.granularity = 1f

        lineChart.axisRight.isEnabled = false
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.axisLeft.setDrawAxisLine(true)
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false
    }

    private fun generateData() {
        val entries = mutableListOf<Entry>() // Entries represent the graph points

        val currentDate = startDate.clone() as Calendar
        while (currentDate <= endDate) {
            val daysSinceStart =
                currentDate.get(Calendar.DAY_OF_MONTH) - startDate.get(Calendar.DAY_OF_MONTH)
            val yValue = Math.random().toFloat() * 100f // Replace with your actual data
            //entries.add(Entry(daysSinceStart.toFloat(), yValue))
            currentDate.add(Calendar.DAY_OF_MONTH, 1)
        }

        val dataSet = LineDataSet(entries, "Data")
        dataSet.color = ColorTemplate.rgb("#009688")
        dataSet.setDrawCircles(false)
        dataSet.lineWidth = 2f
        dataSet.valueTextSize = 12f

        val dataSets: MutableList<ILineDataSet> = ArrayList()
        dataSets.add(dataSet)

        val lineData = LineData(dataSets)
        lineChart.data = lineData
        lineChart.invalidate()
    }

    //NEED TO UPDATE SINCE DB FIELD NAMES HAVE CHANGED
    //The Duration of all the tasks on a date needs to be calculated
    //The total duration for each day needs to be calucalted and shown on the graph inrealtion to the y axis and the X axis.
    fun GetGraphData() {

        val itemId = intent.getStringExtra("itemId") //??
        val db = FirebaseFirestore.getInstance()
        Log.d("TAG", "Firestore Instance: $db")
        val tasksRef = db.collection("Tasks")
        Log.d("TAG", "Task Data: $tasksRef")
        val hoursList = mutableListOf<String>()
        val userid = FirebaseAuth.getInstance().currentUser?.uid
        Log.d("TAG", "userid: $userid")
        tasksRef.whereEqualTo("userid", userid).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = document.data
                val hours = data["hours"] as String
                hoursList.add(hours)
            }
            Log.d("Tag", "HoursFromTasks: $hoursList")
        }


    }
}






