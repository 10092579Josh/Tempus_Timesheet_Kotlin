package com.example.tempus_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseAuth

// THIS PAGE DEALS WITH THE CATEGORY
// THIS POPULATES THE RECYCLER VIEW
class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity" // Replace "MyActivity" with the name of your class
    }


    // OBJECT CLASS TO ADD DATA TO AND CONTAINS THE ARRRAYS
    object TaskClass {
        //ASSIGNING TASKS
        val rows = 10
        val columns = 12

        //ARRAYS FOR THE TASK DATA
        val tasks = Array(rows) { arrayOfNulls<String>(columns) }
        val hours = Array(rows) { arrayOfNulls<String>(columns) }

    }
    fun loggedonnotification()
    {
        val user = FirebaseAuth.getInstance().currentUser?.email
        val channelId = "login"
        val channelName = "Loginuser"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(channelId, channelName, importance)

        val TempusManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        TempusManager.createNotificationChannel(notificationChannel)

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("$user logged in")
            .setContentText("welcome to tempus $user")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_EVENT)

        TempusManager.notify(0, builder.build())

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
     loggedonnotification()
        try {
            FirebaseApp.initializeApp(this)
            val firestore = Firebase.firestore


            print()
            // SETTING THE TOTAL DATA

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
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    fun print() {
        val recyclerview = findViewById<RecyclerView>(R.id.mRecycler_category)
        recyclerview.layoutManager = LinearLayoutManager(this)
        val db = FirebaseFirestore.getInstance()
        val itemsRef = db.collection("Categories")

// Get the user ID (this will depend on how you are authenticating users)
        val userid = FirebaseAuth.getInstance().currentUser?.uid

// Create a query that only returns documents where the userId field matches the current user's ID
        val query = itemsRef.whereEqualTo("userid",userid.toString().trim())

// Execute the query and get the results as a Task
        val task = query.get()

// Add an OnSuccessListener to the Task to get the results
        task.addOnSuccessListener { documents ->
            // Create an empty list to hold the items
            val items = mutableListOf<ItemsViewModel>()

            for (document in documents) {
                // Get the data for each document
                val name = document.getString("catname") ?: ""
                val description = document.getString("cathours") ?: ""
                val item = ItemsViewModel(name, description)
                items.add(item)
            }

            // Update the RecyclerView with the new data
            // ...
            val sortedItems = items.sortedBy { it.text }
            try {


                val adapter = CustomAdapter(sortedItems as MutableList<ItemsViewModel>)
                recyclerview.adapter = adapter
            }catch (e:Exception)
            {


            }
        }






    }

    // PROMPT THE USER FOR PERMISSIONS


    data class ItemsViewModel(val text: String, val hours: String) {


    }

    class CustomAdapter(val catlist: MutableList<ItemsViewModel> = mutableListOf()) :
        RecyclerView.Adapter<CustomAdapter.ViewHolder>() {




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

            val ItemsViewModel = catlist[position]

            holder.textView.text = ItemsViewModel.text
            holder.textView2.text = ItemsViewModel.hours

            holder.itemView.setOnClickListener {
                // Handle item click here
                // You can use the position parameter to determine which item was clicked
                // For example, you can start a new activity and pass the position or data as an extra



                // You can use the position parameter to access data for the clicked item


            }
        }

        // return the number of the items in the list
        override fun getItemCount(): Int {
            return catlist.size
        }

        // Holds the views for adding it to image and text
        class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

            val textView: TextView = itemView.findViewById(R.id.mTitle)
            val textView2: TextView = itemView.findViewById(R.id.mHours_category)

        }
    }
}



