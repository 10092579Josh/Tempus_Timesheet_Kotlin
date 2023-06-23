package com.example.tempus

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


// THIS PAGE DEALS WITH THE CATEGORY
// THIS POPULATES THE RECYCLER VIEW
class Home : AppCompatActivity() {
    private var selectedTabIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val homeID = intent.getIntExtra("home", 0)
        val homelayout = layoutInflater.inflate(homeID, null)
        setContentView(homelayout)
        SecurGuard()
        populatefields()
// Set up the ViewPager and the TabLayout

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
            task.setOnClickListener()
            {
                val intent = Intent(this, Home::class.java)
                intent.putExtra("home", getIntent().getIntExtra("home", R.layout.home))
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

            addbtn.setOnClickListener()
            {


                val Shortcut = BottomSheetDialog(this)
                val ShortcutView = layoutInflater.inflate(R.layout.shortcut, null)

                Shortcut.setContentView(ShortcutView)

                Shortcut.show()

                val CreateNewCat = ShortcutView.findViewById<Button>(R.id.add_category)

                CreateNewCat.setOnClickListener {
                    val Newform = Intent(this, CatergoryForm::class.java)
                    startActivity(Newform)
                    overridePendingTransition(0, 0)
                    finish()

                    Shortcut.dismiss()
                }

                val CreateNewTask = ShortcutView.findViewById<Button>(R.id.add_task)
                CreateNewTask.setOnClickListener {

                    val NewTask = Intent(this, TaskForm::class.java)
                    startActivity(NewTask)
                    overridePendingTransition(0, 0)
                    finish()

                    Shortcut.dismiss()
                }

                val AddNewGoals = ShortcutView.findViewById<Button>(R.id.add_goals)
                AddNewGoals.setOnClickListener {
                    // to be implemented

                    Shortcut.dismiss()
                }


            }
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the selected tab index
        outState.putInt("selectedTabIndex", selectedTabIndex)
    }

    private fun SecurGuard() {

        val TempusSecurity = FirebaseAuth.getInstance()
        TempusSecurity.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {

                val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
                sharedPreferences.edit().putBoolean("isFirstLogin", true).apply()
                AppSettings.preloads.usersname = null
                val intent = Intent(this@Home, Login::class.java)
                intent.putExtra("login", R.layout.login)
                overridePendingTransition(0, 0)
                startActivity(intent)

            }
        }

        val TempusUsers = FirebaseAuth.getInstance().currentUser
        TempusUsers?.reload()?.addOnCompleteListener { task ->
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
                        val intent = Intent(this@Home, Login::class.java)
                        intent.putExtra("login", R.layout.login)
                        overridePendingTransition(0, 0)
                        startActivity(intent)
                    }
                }
            }
        }

    }

    companion object {
        private const val TAG = "Home"
    }

    object TaskClass {
        val rows = 10
        val columns = 12

        val tasks = Array(rows) { arrayOfNulls<String>(columns) }
        val hours = Array(rows) { arrayOfNulls<String>(columns) }
        var check: Int? = null

    }

    fun print() {
        val recyclerview = findViewById<RecyclerView>(R.id.mRecycler_category)
        recyclerview.layoutManager = LinearLayoutManager(this)
        val db = FirebaseFirestore.getInstance()
        val itemsRef = db.collection("CategoryStorage")

        val userid = FirebaseAuth.getInstance().currentUser?.uid
        val catergoryquery = itemsRef.whereEqualTo("userIdCat", userid.toString().trim())
        val task = catergoryquery.get()

        task.addOnSuccessListener { documents ->

            val items = mutableListOf<ItemsViewModel>()

            for (document in documents) {
                val name = document.getString("categoryID") ?: ""
                val description = document.getString("totalHours") ?: ""
                val item = ItemsViewModel(name, description)
                items.add(item)
            }

            val sortedItems = items.sortedBy { it.text }
            try {
                val adapter = CustomAdapter(sortedItems as MutableList<ItemsViewModel>)
                adapter.onTaskClickListener = { _ ->

                }
                recyclerview.adapter = adapter

                val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
                    0,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                ) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val position = viewHolder.adapterPosition
                        val item = sortedItems[position]

                        // Remove the item from the RecyclerView
                        sortedItems.removeAt(position)
                        adapter.notifyItemRemoved(position)

                        // Delete the item from the database
                        val databaseRef = itemsRef.document(item.text)
                        databaseRef.delete()
                    }

                    // Add swipe action buttons
                    override fun onChildDraw(
                        c: Canvas,
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        dX: Float,
                        dY: Float,
                        actionState: Int,
                        isCurrentlyActive: Boolean
                    ) {
                        super.onChildDraw(
                            c,
                            recyclerView,
                            viewHolder,
                            dX,
                            dY,
                            actionState,
                            isCurrentlyActive
                        )

                        val itemView = viewHolder.itemView

                        if (dX > 0) {
                            // Swiping to the right (edit action)
                            val editIcon =
                                ContextCompat.getDrawable(this@Home, R.drawable.edit_icon)
                            val editIconMargin = (itemView.height - editIcon?.intrinsicHeight!!) / 2
                            val editIconTop = itemView.top + editIconMargin
                            val editIconBottom = editIconTop + editIcon.intrinsicHeight
                            val editIconLeft = itemView.left + editIconMargin
                            val editIconRight =
                                itemView.left + editIconMargin + editIcon.intrinsicWidth

                            editIcon?.setBounds(
                                editIconLeft,
                                editIconTop,
                                editIconRight,
                                editIconBottom
                            )

                            val editBackground = ContextCompat.getDrawable(
                                this@Home,
                                R.drawable.edit_button_background
                            )
                            editBackground?.setBounds(
                                itemView.left,
                                itemView.top,
                                itemView.left + dX.toInt(),
                                itemView.bottom
                            )
                            editBackground?.draw(c)
                            editIcon?.draw(c)
                        } else {
                            // Swiping to the left (delete action)
                            val deleteIcon =
                                ContextCompat.getDrawable(this@Home, R.drawable.delete_icon)
                            val deleteIconMargin =
                                (itemView.height - deleteIcon?.intrinsicHeight!!) / 2
                            val deleteIconTop = itemView.top + deleteIconMargin
                            val deleteIconBottom = deleteIconTop + deleteIcon.intrinsicHeight
                            val deleteIconLeft =
                                itemView.right - deleteIconMargin - deleteIcon.intrinsicWidth
                            val deleteIconRight = itemView.right - deleteIconMargin

                            deleteIcon?.setBounds(
                                deleteIconLeft,
                                deleteIconTop,
                                deleteIconRight,
                                deleteIconBottom
                            )

                            val deleteBackground = ContextCompat.getDrawable(
                                this@Home,
                                R.drawable.delete_button_background
                            )
                            deleteBackground?.setBounds(
                                itemView.right + dX.toInt(),
                                itemView.top,
                                itemView.right,
                                itemView.bottom
                            )
                            deleteBackground?.draw(c)
                            deleteIcon?.draw(c)
                        }
                    }
                }

                val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
                itemTouchHelper.attachToRecyclerView(recyclerview)
            } catch (e: Exception) {
            }
        }
    }


    data class Task(val name: String, val hours2: String)

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
            val tasksRef = db.collection("TaskStorage")
            val userid = FirebaseAuth.getInstance().currentUser?.uid


            val CatQuery = tasksRef.whereEqualTo("userIdTask", userid)
                .whereEqualTo("categoryName", CategoryTask)

            val task = CatQuery.get()
            Log.d("categoryID", "$CategoryTask")
            Log.d("userIdTask", "$userid")

            task.addOnSuccessListener { documents ->
                val tasksByTab = mutableMapOf<String, MutableList<Task>>()
                for (document in documents) {
                    // Get the data for each document
                    val name = document.getString("taskName") ?: ""
                    val hours = document.getString("duration") ?: ""
                    Log.d("Taskname", "$name")
                    val tabName = document.getString("tabID") ?: ""
                    Log.d("tabID", "$tabName")
                    val task = Task(name, hours)
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

    class SubTasksAdapter(private val tasks: List<Task>) :
        RecyclerView.Adapter<SubTasksAdapter.ViewHolder>() {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(R.id.mTitle3)
            val textView2: TextView = itemView.findViewById(R.id.mHpurs2)


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.subtask, parent, false)
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
                HomeIntent.putExtra("duration", clickedData.hours2)
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


    fun loggedonnotification() {
        val LogIntent = Intent(this, AppSettings::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val settingsIntent =
            PendingIntent.getActivity(this, 0, LogIntent, PendingIntent.FLAG_MUTABLE)

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
        val myRef = database.getReference("UserDetails")
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var matchFound = false
                for (ds in dataSnapshot.children) {
                    val documentName = ds.key
                    Log.d("MyTag", "${AppSettings.preloads.usersname}")
                    Log.d("MyTag", "documentName: $documentName")
                    if (documentName == userid.toString().trim()) {
                        TempusManager.notify(0, builder.build())

                        Log.d("MyTag", "Condition 2 met")

                        matchFound = true

                    } else if (AppSettings.preloads.usersname.isNullOrEmpty()) {
                        Log.d("MyTag", "${AppSettings.preloads.usersname}")
                        Log.d("MyTag", "Condition 3 met")
                        TempusManager.notify(1, unverifiedbuilder.build())
                    }

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })

    }

    fun populatefields() {


        val userid = FirebaseAuth.getInstance().currentUser?.uid
        val database = Firebase.database
        val myRef = database.getReference("UserDetails")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val userId = data.child("userid").getValue(String::class.java)
                    Log.d("check2", "${userId.toString()}")
                    if (userId.toString().trim() == userid.toString().trim()) {
                        AppSettings.preloads.names =
                            data.child("firstname").getValue(String::class.java).toString()
                        AppSettings.preloads.emails =
                            data.child("emailaddress").getValue(String::class.java).toString()
                        AppSettings.preloads.surname =
                            data.child("surname").getValue(String::class.java).toString()
                        AppSettings.preloads.usersname =
                            data.child("displayname").getValue(String::class.java).toString()
                        AppSettings.preloads.conpass =
                            data.child("confirmkey").getValue(String::class.java).toString()
                        AppSettings.preloads.pass =
                            data.child("password").getValue(String::class.java).toString()
                        Log.d("check1", "${AppSettings.preloads.names}")
                        Log.d("check3", "${userid.toString()}")

                    }


                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }

        })


    }
}