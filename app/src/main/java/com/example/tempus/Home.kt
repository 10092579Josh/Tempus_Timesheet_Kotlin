package com.example.tempus

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


// THIS PAGE DEALS WITH THE CATEGORY
// THIS POPULATES THE RECYCLER VIEW
class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val homeID = intent.getIntExtra("home", 0)
        val homelayout = layoutInflater.inflate(homeID, null)
        setContentView(homelayout)

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
                val intent = Intent(this, Home::class.java)
                intent.putExtra("home", getIntent().getIntExtra("home",R.layout.home))
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()

            }
            cat.setOnClickListener()
            {
                val tasks = Intent(this, Tasks::class.java)
                startActivity(tasks)
                overridePendingTransition(0, 0)
                finish()

            }

            homebtn.setOnClickListener {

                val intent = Intent(this, Home::class.java)
                intent.putExtra("home", getIntent().getIntExtra("home",R.layout.home))
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

            addbtn.setOnClickListener()
            {
                val cform = Intent(this, CatergoryForm::class.java)
                startActivity(cform)
                overridePendingTransition(0, 0)
                finish()



            }
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    companion object {
        private const val TAG = "Home"
    }
    object TaskClass {
        //ASSIGNING TASKS
        val rows = 10
        val columns = 12

        //ARRAYS FOR THE TASK DATA
        val tasks = Array(rows) { arrayOfNulls<String>(columns) }
        val hours = Array(rows) { arrayOfNulls<String>(columns) }
        var check:Int? = null

    }

    fun print() {
        val recyclerview = findViewById<RecyclerView>(R.id.mRecycler_category)
        recyclerview.layoutManager = LinearLayoutManager(this)
        val db = FirebaseFirestore.getInstance()
        val itemsRef = db.collection("Categories")

// Get the user ID (this will depend on how you are authenticating users)
        val userid = FirebaseAuth.getInstance().currentUser?.uid

// Create a query that only returns documents where the userId field matches the current user's ID
        val catergoryquery = itemsRef.whereEqualTo("userid",userid.toString().trim())

// Execute the query and get the results as a Task
        val task = catergoryquery.get()

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
                adapter.onTaskClickListener = { task ->


                }
                recyclerview.adapter = adapter
            } catch (e: Exception) {
            }
        }




    }

    // PROMPT THE USER FOR PERMISSIONS
    data class Task(val name: String,val hours2: String)
    fun fix()
    {  val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val layoutParams = tabLayout.layoutParams
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        tabLayout.layoutParams = layoutParams
    }

    data class ItemsViewModel(val text: String, val hours: String)

    class CustomAdapter(val catlist: MutableList<ItemsViewModel> = mutableListOf()) :
        RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

        var onTaskClickListener: ((Task) -> Unit)? = null



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.category, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val itemsViewModel = catlist[position]
            holder.textView.text = itemsViewModel.text
            holder.textView2.text = itemsViewModel.hours

            holder.itemView.setOnClickListener {

                if (holder.tasksLayout.visibility == View.GONE) {
                    holder.tasksLayout.visibility = View.VISIBLE
                    populateTasks(holder, itemsViewModel.text)
                } else {
                    holder.tasksLayout.visibility = View.GONE
                }
            }


        }

        override fun getItemCount(): Int {
            return catlist.size
        }

        class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
            val textView: TextView = itemView.findViewById(R.id.mTitle)
            val textView2: TextView = itemView.findViewById(R.id.mHours_category)
            val tasksLayout: LinearLayout = itemView.findViewById(R.id.tasksLayout)
        }
        private fun populateTasks(holder: ViewHolder, CategoryTask: String) {

            val db = FirebaseFirestore.getInstance()
            val tasksRef = db.collection("Tasks")
            val userid = FirebaseAuth.getInstance().currentUser?.uid


            val CatQuery = tasksRef.whereEqualTo("userid", userid).whereEqualTo("catergorytask", CategoryTask)

            val task = CatQuery.get()
            Log.d("catname", "$CategoryTask")
            Log.d("userid", "$userid")

            task.addOnSuccessListener { documents ->
                val tasksByTab = mutableMapOf<String, MutableList<Task>>()
                for (document in documents) {
                    // Get the data for each document
                    val name = document.getString("taskname") ?: ""
                    val hours = document.getString("hours") ?: ""
                    Log.d("Taskname", "$name")
                    val tabName = document.getString("tabname") ?: ""
                    Log.d("tabname", "$tabName")
                    val task = Task(name,hours)
                    if (tasksByTab.containsKey(tabName)) {
                        tasksByTab[tabName]?.add(task)
                        Log.d("tabname2", "$tabName")
                    } else {
                        tasksByTab[tabName] = mutableListOf(task)

                        Log.d("tabname3", "$tabName")
                    }
                    Log.d("tabname4", "$tabName")
                }

                holder.tasksLayout.removeAllViews()

                for ((tabName, tasks) in tasksByTab) {

                    val DisplayTab = TextView(holder.itemView.context)
                    DisplayTab.setTypeface(null, Typeface.BOLD)
                    holder.tasksLayout.addView(DisplayTab)
                    val NewView = RecyclerView(holder.itemView.context)
                    NewView.layoutManager = LinearLayoutManager(holder.itemView.context)
                    val SubAdapter = SubTasksAdapter(tasks)
                    NewView.adapter = SubAdapter
                    holder.tasksLayout.addView(NewView)
                }

            }

        }



    }
    class SubTasksAdapter(private val tasks: List<Task>) : RecyclerView.Adapter<SubTasksAdapter.ViewHolder>() {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(R.id.mTitle3)
            val textView2: TextView = itemView.findViewById(R.id.mHpurs2)


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.rr, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val task = tasks[position]
            holder.textView.text = task.name
            holder.textView2.text = task.hours2

            holder.itemView.setOnClickListener()
            {
                val clickedData = tasks[position]
                val context = holder.itemView.context
                val HomeIntent = Intent(context, TaskPage::class.java)
                HomeIntent.putExtra("task", clickedData.name)
                HomeIntent.putExtra("hours", clickedData.hours2)
                val bundle = Bundle()
                val bundle2 = Bundle()
                bundle.putSerializable("myDataList", Home.TaskClass.tasks)
                HomeIntent.putExtra("position1", position) // use "position" as the key
                HomeIntent.putExtras(bundle)
                bundle2.putSerializable("Hours", Home.TaskClass.hours)
                HomeIntent.putExtra("position2", position) // use "position" as the key
                HomeIntent.putExtras(bundle2)
                HomeIntent.putExtra("itemId", clickedData.name)
                context.startActivity(HomeIntent)
            }

        }

        override fun getItemCount(): Int {
            return tasks.size
        }
    }


    fun loggedonnotification()
    {
        val LogIntent = Intent(this, AppSettings::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val settingsIntent = PendingIntent.getActivity(this, 0, LogIntent, PendingIntent.FLAG_MUTABLE)

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
                var matchFound = false
                for (ds in dataSnapshot.children) {
                    val documentName = ds.key
                    Log.d("MyTag", "${AppSettings.preloads.usersname}")
                    Log.d("MyTag", "documentName: $documentName")
                    if (documentName == userid.toString().trim() ) {
                        TempusManager.notify(0, builder.build())

                        Log.d("MyTag", "Condition 2 met")
                        break
                        matchFound = true

                    }

                    else if ( AppSettings.preloads.usersname.isNullOrEmpty()) {
                        Log.d("MyTag", "${AppSettings.preloads.usersname}")
                            Log.d("MyTag", "Condition 3 met")
                            TempusManager.notify(1, unverifiedbuilder.build()) }

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })

    }
 

}



