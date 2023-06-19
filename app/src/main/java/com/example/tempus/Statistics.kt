package com.example.tempus

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton

import androidx.appcompat.app.AppCompatActivity
import com.example.tempus.Tasks.DateClass.endDate
import com.example.tempus.Tasks.DateClass.startDate
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class Statistics : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.statistics)


        FirebaseApp.initializeApp(this)

        //Initialzation of bottom navigation buttons

        val homebtn = findViewById<ImageButton>(R.id.hometbtn)
        val breaksbtn = findViewById<ImageButton>(R.id.breakstbtn)
        val statsbtn = findViewById<ImageButton>(R.id.statstbtn)
        val settingsbtn = findViewById<ImageButton>(R.id.settingstbtn)
        val addbtn = findViewById<ImageButton>(R.id.addbtn)

        setupDateRangePicker()
        setupLineChart()
        GetGraphData()


        // Onclick listnerner
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



    private fun setupLineChart() {
        /*
        lineChart.description.isEnabled = false
        lineChart.setTouchEnabled(true)
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(true)
        lineChart.setPinchZoom(true)

        lineChart.xAxis.setDrawGridLines(false)
        lineChart.xAxis.setDrawAxisLine(true)
        lineChart.xAxis.granularity = 1f

        lineChart.axisRight.isEnabled = false
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.axisLeft.setDrawAxisLine(true)
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false

        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
       // Replace with your end date string
       // generateDateLabels()
// Refresh the chart
        lineChart.invalidate()
        */
        val lineChart = findViewById<LineChart>(R.id.statsChart)
        val lineEntries = listOf(
            Entry(0f, 4f),
            Entry(1f, 3f),
            Entry(2f, 6f),
            Entry(3f, 8f),
            Entry(4f, 2f),
            Entry(5f, 3f),
            Entry(6f, 1f)
        )

        // Create a line data set with a label and a color
        val lineDataSet = LineDataSet(lineEntries, "Work")
        lineDataSet.color = Color.RED
        lineDataSet.lineWidth = 2f

        // Create a line data object with the data set
        val lineData = LineData(lineDataSet)

        // Set the data for the line chart and refresh it
        lineChart.data = lineData
        lineChart.invalidate()

        // Customize the appearance of the line chart
        lineChart.description.isEnabled = false // Hide the description text
        lineChart.legend.isEnabled = false // Hide the legend
        lineChart.setDrawGridBackground(false) // Hide the grid background
        lineChart.setDrawBorders(false) // Hide the borders
        lineChart.axisRight.isEnabled = false // Hide the right axis

        // Customize the x-axis labels
        val xAxisLabels = listOf("Rest", "Work", "2-up")
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM // Set the position to bottom
        xAxis.setDrawGridLines(false) // Hide the grid lines
        xAxis.labelCount = xAxisLabels.size // Set the number of labels
        xAxis.valueFormatter = XAxisValueFormatter(xAxisLabels) // Set the custom formatter

        // Customize the y-axis labels
        val yAxis = lineChart.axisLeft
        yAxis.setDrawGridLines(false) // Hide the grid lines
        yAxis.setDrawZeroLine(true) // Draw a zero line
        yAxis.axisMinimum = 0f // Set the minimum value to zero
    }

    fun setupDateRangePicker()
    {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        builder.setTitleText("Select dates")

        val picker = builder.build()
        picker.show(supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener { dateRange ->
            // dateRange is a pair of Long values
            val startDate = dateRange.first
            val endDate = dateRange.second
            // You can use SimpleDateFormat or other classes to format these values
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val startDateString = dateFormat.format(startDate)
            val endDateString = dateFormat.format(endDate)
            // You can display these values in a TextView or other views
            Log.d("TAG", "userid: $startDateString - $endDateString ")
        }
    }
    fun GetGraphData() {

        // val itemId = intent.getStringExtra("itemId") //??
        val db = FirebaseFirestore.getInstance()
        Log.d("TAG", "Firestore Instance: $db")
        val tasksRef = db.collection("TaskStorage")
        Log.d("TAG", "Task Data: $tasksRef")
        val durationList = mutableListOf<String>()
        val DateList = mutableListOf<String>()
        val userid = FirebaseAuth.getInstance().currentUser?.uid
        Log.d("TAG", "userid: $userid")
        tasksRef.whereEqualTo("userIdTask", userid).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = document.data

                val hours = data["duration"] as String
                val date = data["dateAdded"] as String
                val cleanedHours = hours.replace(":", " ") // Remove colon from hours
                DateList.add(date)
                durationList.add(cleanedHours)
            }
            Log.d("Tag", "HoursFromTasks: $durationList")
            Log.d("Tag", "DatesFromTasks: $DateList")


        }

    }

}














//NEED TO UPDATE SINCE DB FIELD NAMES HAVE CHANGED
//The Duration of all the tasks on a date needs to be calculated
//The total duration for each day needs to be calucalted and shown on the graph inrealtion to the y axis and the X axis.




class XAxisValueFormatter(private val labels: List<String>) : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return if (value.toInt() in labels.indices) {
            labels[value.toInt()]
        } else {
            ""
        }
    }
}






// Create a list of entries for the line data set

// A custom formatter for the x-axis labels




