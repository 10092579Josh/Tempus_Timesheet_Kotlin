package com.example.tempus

import android.content.Intent
import android.graphics.Color
import android.graphics.ColorMatrix
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.blue
import androidx.core.graphics.red
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
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
import java.text.SimpleDateFormat
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

        setupLineChart()


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
        val db = FirebaseFirestore.getInstance()
        val tasksRef = db.collection("TaskStorage")
        val durationList = mutableListOf<Float>()
        val compList = mutableListOf<Float>()
        val mings = mutableListOf<Float>()
        val mags = mutableListOf<Float>()
        val datedatabase = mutableListOf<String>()
        val userid = FirebaseAuth.getInstance().currentUser?.uid
        val dparse = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val startDate = dparse.parse("01/01/2023")
        val endDate = dparse.parse("31/12/2023")
        val accreditedhours = mutableListOf<BarEntry>()
        val finisedhours = mutableListOf<Entry>()
        val mgoals = mutableListOf<Entry>()
        val axgoals = mutableListOf<Entry>()
        val Tempustats = findViewById<CombinedChart>(R.id.statsChart)
        val usertks = mutableListOf<String>()
        tasksRef.whereEqualTo("userIdTask", userid).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = document.data
                val task = data["taskName"] as String
                val TempusMingoal = data["minGoal"] as String
                val TempusMaxgoal = data["maxGoal"] as String
                val whenadded = data["dateAdded"] as String
                val intialhours = data["duration"] as String
                val breaKup = intialhours.split(":")
                val intialtime = breaKup[0].toInt() * 60
                val intialmin = breaKup[1].toInt()
                val result = intialtime + intialmin
                val graphpoints = result / 60.0f

                mings.add(TempusMingoal.toFloat())
                mags.add(TempusMaxgoal.toFloat())
                usertks.add(task)
                datedatabase.add(whenadded)
                compList.add(3.0f)
                durationList.add(graphpoints)
                Log.d("dateslist", "$datedatabase")
                Log.d("datesadded", "$whenadded")
            }
            val test = 3.0f

            for (h in durationList.indices) {
                val dateadded = dparse.parse(datedatabase[h])
                val task = usertks[h]
                val taskIndex = usertks.indexOf(task)
                val dayrange = (dateadded.time - startDate.time) / (24 * 60 * 60 * 1000)
                val barEntry = BarEntry(taskIndex.toFloat(), floatArrayOf( compList[h],durationList[h]))
                accreditedhours.add(barEntry)
                finisedhours.add(Entry(taskIndex.toFloat(), durationList[h]))
                mgoals.add(Entry(taskIndex.toFloat(), mings[h]))
                axgoals.add(Entry(taskIndex.toFloat(), mags[h]))

            }
            val mx = LineDataSet(axgoals,"Max goals")
            mx.setColors(Color.GREEN)
            mx.valueTextSize = 16f
            mx.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            mx.lineWidth = 3f
            mx.circleRadius = 5f
            mx.circleHoleRadius = 5f
            mx.setCircleColor(Color.rgb(2,69,30))
            val m = LineDataSet(mgoals,"Min goals")
            m.setColors(Color.rgb(248,30,30))
            m.valueTextSize = 16f
            m.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            m.lineWidth = 3f
            m.circleHoleRadius = 5f
            m.circleRadius = 5f
            m.setCircleColor((Color.rgb(189,4,4)))

            val hoursfinihed = LineDataSet(finisedhours, "Completed Hours")
            hoursfinihed.color = Color.rgb(98,0,234)
            hoursfinihed.valueTextSize =16f
            hoursfinihed.setCircleColor(Color.rgb(25,1,58))
            hoursfinihed.circleRadius = 5f
            hoursfinihed.circleHoleRadius = 5f

            val accumolatedhours = BarDataSet(accreditedhours, "")

accumolatedhours.setColors(Color.LTGRAY,Color.DKGRAY,)
accumolatedhours.valueTextColor = Color.CYAN
            accumolatedhours.valueTextSize = 16f
            val barData = BarData(accumolatedhours)
            barData.barWidth = 0.6f // Set the width of the bars

            val combinedData = CombinedData()

            val lineData = LineData()
            lineData.addDataSet(hoursfinihed)
           lineData.addDataSet(m)
            lineData.addDataSet(mx)
            hoursfinihed.mode = LineDataSet.Mode.CUBIC_BEZIER
            hoursfinihed.lineWidth = 3f

            combinedData.setData(lineData)
            combinedData.setData(barData)
            Tempustats.legend.resetCustom()
            val legendEntries = Tempustats.legend.entries.toMutableList()
            legendEntries.add(LegendEntry("min goals", Legend.LegendForm.DEFAULT, Float.NaN, Float.NaN, null, Color.rgb(248,30,30)))
            legendEntries.add(LegendEntry("max goals", Legend.LegendForm.DEFAULT, Float.NaN, Float.NaN, null, Color.GREEN))
            legendEntries.add(LegendEntry("allocated hours", Legend.LegendForm.DEFAULT, Float.NaN, Float.NaN, null, Color.DKGRAY))
            legendEntries.add(LegendEntry("completed hours", Legend.LegendForm.DEFAULT, Float.NaN, Float.NaN, null, Color.rgb(98,0,234)))
            legendEntries.add(LegendEntry("breaks", Legend.LegendForm.DEFAULT, Float.NaN, Float.NaN, null, Color.MAGENTA))
            Tempustats.legend.setCustom(legendEntries)
            Tempustats.legend.textSize = 10.5f
            Tempustats.data = combinedData
            Tempustats.isScaleXEnabled = true
            Tempustats.isScaleYEnabled = false
            Tempustats.isDragEnabled = true

            Tempustats.setVisibleXRangeMaximum(5f)
            Tempustats.xAxis.textSize = 9.5f
            Tempustats.xAxis.xOffset = 3f
            Tempustats.xAxis.yOffset = 0f
            Tempustats.xAxis.granularity = 1f
            val dates = datedatabase.toTypedArray()


            Tempustats.invalidate()

            Tempustats.description.isEnabled = false // Hide the description text
            Tempustats.legend.isEnabled = true // Show the legend
            Tempustats.setDrawGridBackground(false) // Hide the grid background
            Tempustats.setDrawBorders(false) // Hide the borders
            Tempustats.axisRight.isEnabled = false // Hide the right axis
            val topXAxis = Tempustats.xAxis
            topXAxis.position = XAxis.XAxisPosition.TOP // Set the position to top
            topXAxis.setDrawGridLines(false) // Hide the grid lines
            topXAxis.valueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    val index = value.toInt()
                    return if (index >= 0 && index < datedatabase.size) datedatabase[index] else ""

                }
            }
            val yAxis = Tempustats.axisLeft
            yAxis.setDrawGridLines(false) // Hide the grid lines
            yAxis.setDrawZeroLine(true) // Draw a zero line
            yAxis.axisMinimum = 0f // Set the minimum value to zero
            val bottomXAxis = Tempustats.xAxis
            bottomXAxis.position = XAxis.XAxisPosition.BOTTOM // Set the position to bottom
            bottomXAxis.setDrawGridLines(true) // Hide the grid lines
            bottomXAxis.valueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    val index = value.toInt()
                    return if (index >= 0 && index < usertks.size) usertks[index] else ""
                }
            }


        }

    }


}

