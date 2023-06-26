package com.example.tempus

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class Statistics : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.statistics)
        val statsBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@Statistics, Home::class.java)
                intent.putExtra("home", getIntent().getIntExtra("home", R.layout.home))
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()


            }
        }
        onBackPressedDispatcher.addCallback(this, statsBack)

        FirebaseApp.initializeApp(this)

        //Initialization of bottom navigation buttons

        val homebtn = findViewById<ImageButton>(R.id.hometbtn)
        val breaksbtn = findViewById<ImageButton>(R.id.breakstbtn)
        val statsbtn = findViewById<ImageButton>(R.id.statstbtn)
        val settingsbtn = findViewById<ImageButton>(R.id.settingstbtn)
        val addbtn = findViewById<ImageButton>(R.id.addbtn)

        setupLineChart()


        // Onclick listener
        addbtn.setOnClickListener {
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


    }


    private fun setupLineChart() {
        val db = FirebaseFirestore.getInstance()
        val tasksRef = db.collection("TaskStorage")
        val durationList = mutableListOf<Float>()
        val compList = mutableListOf<Float>()
        val min = mutableListOf<Float>()
        val mags = mutableListOf<Float>()
        val breakHour = mutableListOf<Float>()
        val dateDatabase = mutableListOf<String>()
        val userid = FirebaseAuth.getInstance().currentUser?.uid
        val accreditedHours = mutableListOf<BarEntry>()
        val finishedHours = mutableListOf<Entry>()
        val lowGoals = mutableListOf<Entry>()
        val maxGoals = mutableListOf<Entry>()
        val tempuStats = findViewById<CombinedChart>(R.id.statsChart)
        val userTasks = mutableListOf<String>()

        tasksRef.whereEqualTo("userIdTask", userid).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = document.data
                val task = data["taskName"] as String
                val tempusMingoal = data["minGoal"] as String
                val tempusMaxGoal = data["maxGoal"] as String
                val whenAdded = data["dateAdded"] as String
                val initialHours = data["duration"] as String
                val notedHours:String = data["completedHours"] as String
                val breakHours:String = data["breakHours"] as String
                val breakup = initialHours.split(":")
                val initialTime = breakup[0].toInt() * 60
                val initialMin = breakup[1].toInt()
                val result = initialTime + initialMin
                val graphPoints = result / 60.0f

                val breakup2 = notedHours.split(":")
                val initialNoteHours = breakup2[0].toInt() * 60
                val initialNoteMin = breakup[1].toInt()
                val resultNoted = initialNoteHours + initialNoteMin
                val graphPointsNoted = resultNoted / 60.0f

val point = breakHours.toFloat() * 0.01

                min.add(tempusMingoal.toFloat())
                mags.add(tempusMaxGoal.toFloat())
                userTasks.add(task)
                dateDatabase.add(whenAdded)
                compList.add(graphPointsNoted)
                durationList.add(graphPoints)
                breakHour.add(point.toFloat())
            }


            for (h in durationList.indices) {

                val task = userTasks[h]
                val taskIndex = userTasks.indexOf(task)
                val barEntry =
                    BarEntry(taskIndex.toFloat(), floatArrayOf(breakHour[h], durationList[h]))
                accreditedHours.add(barEntry)
                finishedHours.add(Entry(taskIndex.toFloat(), compList[h]))
                lowGoals.add(Entry(taskIndex.toFloat(), min[h]))
                maxGoals.add(Entry(taskIndex.toFloat(), mags[h]))

            }
            val mx = LineDataSet(maxGoals, "Max goals")
            mx.setColors(Color.GREEN)
            mx.valueTextSize = 16f
            mx.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            mx.lineWidth = 3f
            mx.circleRadius = 5f
            mx.circleHoleRadius = 5f
            mx.setCircleColor(Color.rgb(2, 69, 30))
            val m = LineDataSet(lowGoals, "Min goals")
            m.setColors(Color.rgb(248, 30, 30))
            m.valueTextSize = 16f
            m.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            m.lineWidth = 3f
            m.circleHoleRadius = 5f
            m.circleRadius = 5f
            m.setCircleColor((Color.rgb(189, 4, 4)))

            val hoursFinished = LineDataSet(finishedHours, "Completed Hours")
            hoursFinished.color = Color.rgb(98, 0, 234)
            hoursFinished.valueTextSize = 16f
            hoursFinished.setCircleColor(Color.rgb(25, 1, 58))
            hoursFinished.circleRadius = 5f
            hoursFinished.circleHoleRadius = 5f

            val accumulateHours = BarDataSet(accreditedHours, "")

            accumulateHours.setColors(Color.LTGRAY, Color.DKGRAY)
            accumulateHours.valueTextColor = Color.CYAN
            accumulateHours.valueTextSize = 16f
            val barData = BarData(accumulateHours)
            barData.barWidth = 0.6f // Set the width of the bars

            val combinedData = CombinedData()

            val lineData = LineData()
            lineData.addDataSet(hoursFinished)
            lineData.addDataSet(m)
            lineData.addDataSet(mx)
            hoursFinished.mode = LineDataSet.Mode.CUBIC_BEZIER
            hoursFinished.lineWidth = 3f

            combinedData.setData(lineData)
            combinedData.setData(barData)
            tempuStats.legend.resetCustom()
            val legendEntries = tempuStats.legend.entries.toMutableList()
            legendEntries.add(
                LegendEntry(
                    "min goals",
                    Legend.LegendForm.DEFAULT,
                    Float.NaN,
                    Float.NaN,
                    null,
                    Color.rgb(248, 30, 30)
                )
            )
            legendEntries.add(
                LegendEntry(
                    "max goals",
                    Legend.LegendForm.DEFAULT,
                    Float.NaN,
                    Float.NaN,
                    null,
                    Color.GREEN
                )
            )
            legendEntries.add(
                LegendEntry(
                    "allocated hours",
                    Legend.LegendForm.DEFAULT,
                    Float.NaN,
                    Float.NaN,
                    null,
                    Color.DKGRAY
                )
            )
            legendEntries.add(
                LegendEntry(
                    "completed hours",
                    Legend.LegendForm.DEFAULT,
                    Float.NaN,
                    Float.NaN,
                    null,
                    Color.rgb(98, 0, 234)
                )
            )
            legendEntries.add(
                LegendEntry(
                    "breaks",
                    Legend.LegendForm.DEFAULT,
                    Float.NaN,
                    Float.NaN,
                    null,
                    Color.LTGRAY
                )
            )
            tempuStats.legend.setCustom(legendEntries)
            tempuStats.legend.textSize = 10.5f
            tempuStats.data = combinedData
            tempuStats.isScaleXEnabled = true
            tempuStats.isScaleYEnabled = false
            tempuStats.isDragEnabled = true

            tempuStats.setVisibleXRangeMaximum(5f)
            tempuStats.xAxis.textSize = 9.5f
            tempuStats.xAxis.xOffset = 3f
            tempuStats.xAxis.yOffset = 0f
            tempuStats.xAxis.granularity = 1f


            tempuStats.invalidate()

            tempuStats.description.isEnabled = false // Hide the description text
            tempuStats.legend.isEnabled = true // Show the legend
            tempuStats.setDrawGridBackground(false) // Hide the grid background
            tempuStats.setDrawBorders(false) // Hide the borders
            tempuStats.axisRight.isEnabled = false // Hide the right axis
            val topXAxis = tempuStats.xAxis
            topXAxis.position = XAxis.XAxisPosition.TOP // Set the position to top
            topXAxis.setDrawGridLines(false) // Hide the grid lines
            topXAxis.valueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    val index = value.toInt()
                    return if (index >= 0 && index < dateDatabase.size) dateDatabase[index] else ""

                }
            }


            val yAxis = tempuStats.axisLeft

            yAxis.yOffset = 10f
            yAxis.setDrawGridLines(false) // Hide the grid lines
            yAxis.setDrawZeroLine(true) // Draw a zero line
            yAxis.axisMinimum = 0f // Set the minimum value to zero
            val bottomXAxis = tempuStats.xAxis
            bottomXAxis.position = XAxis.XAxisPosition.BOTTOM // Set the position to bottom
            bottomXAxis.setDrawGridLines(true) // Hide the grid lines
            bottomXAxis.valueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    val index = value.toInt()
                    return if (index >= 0 && index < userTasks.size) userTasks[index] else ""
                }
            }


        }

    }


}

