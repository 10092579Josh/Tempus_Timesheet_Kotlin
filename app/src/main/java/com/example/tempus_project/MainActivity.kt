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
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database

// THIS PAGE DEALS WITH THE CATEGORY
// THIS POPULATES THE RECYCLER VIEW
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        populatefields()
        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("isFirstLogin", true)) {
         loggedonnotification()
            sharedPreferences.edit().putBoolean("isFirstLogin", false).apply()
        }


        try {
            FirebaseApp.initializeApp(this)



            print()
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
    companion object {
        private const val TAG = "MainActivity"
    }
    object TaskClass {
        //ASSIGNING TASKS
        val rows = 10
        val columns = 12

        //ARRAYS FOR THE TASK DATA
        val tasks = Array(rows) { arrayOfNulls<String>(columns) }
        val hours = Array(rows) { arrayOfNulls<String>(columns) }

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
    fun loggedonnotification()
    {
        val intent = Intent(this, SettingsActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val settingsIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)

        val user = FirebaseAuth.getInstance().currentUser?.email
        val channelId = "login"
        val channelName = "Loginuser"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(channelId, channelName, importance)

        val TempusManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        TempusManager.createNotificationChannel(notificationChannel)

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.imageuser)
            .setContentTitle("$user logged in")
            .setContentText("welcome to tempus $user")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_EVENT)
            .setContentIntent(settingsIntent)
            .setAutoCancel(true)



        val unverifiedbuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.imageuser)
            .setContentTitle("$user logged in")
            .setContentText("welcome to tempus new $user please verify your account")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_EVENT)
            .setContentIntent(settingsIntent)
            .setAutoCancel(true)



        val userid = FirebaseAuth.getInstance().currentUser?.uid
        val database = Firebase.database
        val myRef = database.getReference("users")
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    val userId = ds.child("userid").getValue(String::class.java)

                    if (userId.toString().trim() == userid.toString().trim()) {

                        TempusManager.notify(0, builder.build())
                        break
                    }
                    else if (userId.toString().trim() != userid.toString().trim())
                    {
                        TempusManager.notify(1, unverifiedbuilder.build())


                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })

    }
    fun populatefields()
    {


        val userid = FirebaseAuth.getInstance().currentUser?.uid
        val database = Firebase.database
        val myRef = database.getReference("users")



        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val userId = data.child("userid").getValue(String::class.java)

                    if (userId.toString().trim() == userid.toString().trim()) {

                        SettingsActivity.preloads.names = data.child("name").getValue(String::class.java).toString()
                        SettingsActivity.preloads.emails = data.child("email").getValue(String::class.java).toString()
                        SettingsActivity.preloads.surname = data.child("surname").getValue(String::class.java).toString()
                        SettingsActivity.preloads.usersname = data.child("usersname").getValue(String::class.java).toString()
                        SettingsActivity.preloads.conpass = data.child("confirm").getValue(String::class.java).toString()
                        SettingsActivity.preloads.pass = data.child("password").getValue(String::class.java).toString()

                    }


                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })

    }

}



