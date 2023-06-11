package com.example.opsc_part2

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
// THIS PAGE DEALS WITH THE CATEGORY
// THIS POPULATES THE RECYCLER VIEW
class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_PERMISSIONS = 1
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_MEDIA_LOCATION,
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.READ_MEDIA_VIDEO





    )
    // OBJECT CLASS TO ADD DATA TO AND CONTAINS THE ARRRAYS
    object TaskClass {
        //ASSIGNING TASKS
        val rows = 10
        val columns = 12
      //ARRAYS FOR THE TASK DATA
        val tasks = Array(rows) { arrayOfNulls<String>(columns) }
        val hours = Array(rows) { arrayOfNulls<String>(columns) }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
try {

    permissions()
    print()
    // SETTING THE TOTAL DATA
    MainActivity.TaskClass.hours[8][1] = MainActivity.TaskClass.hours[2][0].toString()
    MainActivity.TaskClass.hours[7][1] = MainActivity.TaskClass.hours[7][0].toString()
    MainActivity.TaskClass.hours[6][1] = MainActivity.TaskClass.hours[6][0].toString()
    MainActivity.TaskClass.hours[5][1] = MainActivity.TaskClass.hours[5][0].toString()
    MainActivity.TaskClass.hours[4][1] = MainActivity.TaskClass.hours[4][0].toString()
    MainActivity.TaskClass.hours[3][1] = MainActivity.TaskClass.hours[3][0].toString()
    MainActivity.TaskClass.hours[2][1] = MainActivity.TaskClass.hours[2][0].toString()
    MainActivity.TaskClass.hours[1][1] = MainActivity.TaskClass.hours[1][0].toString()
    MainActivity.TaskClass.hours[0][1] = MainActivity.TaskClass.hours[0][0].toString()
    // VARIABLES FOR BUTTONS
    val addbtn = findViewById<ImageButton>(R.id.addbtn)
    val homebtn = findViewById<ImageButton>(R.id.hometbtn)
    val breaksbtn = findViewById<ImageButton>(R.id.breakstbtn)
    val statsbtn = findViewById<ImageButton>(R.id.statstbtn)
    val settingsbtn = findViewById<ImageButton>(R.id.settingstbtn)

    val cat = findViewById<Button>(R.id.category_selected)
    val task = findViewById<Button>(R.id.task_selected)
// THIS ALLOWS FOR SWITCHING BETWEEN THE PAGES
    task.setOnClickListener()
    {
        val cat = Intent(this, MainActivity::class.java)
        startActivity(cat)
        finish()
    }
    cat.setOnClickListener()
    {
        val tasks = Intent(this, MainTaskActivity::class.java)
        startActivity(tasks)
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

    addbtn.setOnClickListener()
    {
        val cform = Intent(this, CatergoryForm::class.java)
        startActivity(cform)
        finish()

    }
}
catch (e:Exception)
{  Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();}
    }

    fun print()
    {
        val recyclerview = findViewById<RecyclerView>(R.id.mRecycler_category)
        recyclerview.layoutManager = LinearLayoutManager(this)



// THIS PRINTS THE CATEGORIES
        val myDataList = mutableListOf<ItemsViewModel>()
        for (i in TaskClass.tasks.indices) {

            val hours = TaskClass.hours[i][1]
            val task = TaskClass.tasks[i][8]

            if(hours != null && task != null) {

                myDataList.add(ItemsViewModel( task.toString(),hours.toString()))
            }

        }


        // In your adapter


        // This will pass the ArrayList to our Adapter
        val adapter = CustomAdapter(myDataList)


        recyclerview.adapter = adapter


    }
    // PROMPT THE USER FOR PERMISSIONS
    fun permissions()
    {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        val permission = Manifest.permission.MANAGE_EXTERNAL_STORAGE
        val requestCode = 2

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        }
        val permission2 = Manifest.permission.READ_MEDIA_IMAGES
        val requestCode2 = 3

        if (ContextCompat.checkSelfPermission(this, permission2) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission2), requestCode2)
        }

    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            val deniedPermissions = mutableListOf<String>()
            for (i in grantResults.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permissions[i])
                }
            }
            if (deniedPermissions.isEmpty()) {
                // All permissions were granted
            } else {
                // One or more permissions were denied
                Toast.makeText(this, ": ${deniedPermissions.joinToString()}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    data class ItemsViewModel( val text: String,val hours:String) {


    }

    class CustomAdapter( val myDataList: MutableList<ItemsViewModel> = mutableListOf()) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

        // create new views
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            // inflates the card_view_design view
            // that is used to hold list item
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.category, parent, false)

            return ViewHolder(view)

        }

        // binds the list items to a view
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val ItemsViewModel = myDataList[position]





            holder.textView.text = ItemsViewModel.text
            holder.textView2.text = ItemsViewModel.hours


            holder.itemView.setOnClickListener {
                // Handle item click here
                // You can use the position parameter to determine which item was clicked
                // For example, you can start a new activity and pass the position or data as an extra

                val clickedData = myDataList[position]
                val context = holder.itemView.context
                val intent = Intent(context, MainTaskActivity::class.java)
                intent.putExtra("task", clickedData.text)
                intent.putExtra("hours", clickedData.hours)
                val bundle = Bundle()
                bundle.putSerializable  ("myDataList", TaskClass.tasks)
                intent.putExtra("position", position) // use "position" as the key

                intent.putExtras(bundle)

                context.startActivity(intent)

                // You can use the position parameter to access data for the clicked item


            }
        }

        // return the number of the items in the list
        override fun getItemCount(): Int {
            return myDataList.size
        }

        // Holds the views for adding it to image and text
        class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

            val textView: TextView = itemView.findViewById(R.id.mTitle)
            val textView2:TextView = itemView.findViewById(R.id.mHours_category)

        }

    }



}



