package com.example.tempus

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
// THIS DEALS WITH THE TASK AND CONTAINS THE RECYCLER VIEW
// THIS HAS ALL NEEDED MODELS FOR THE VIEW
class Tasks : AppCompatActivity() {
    object DateClass {
        //ASSIGNING TASKS
        var startDate = "dd/MM/yyyy"
        var endDate = "dd/MM/yyyy"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tasks_display)
        print()
        val cat = findViewById<Button>(R.id.category_selected)
        val addbtn = findViewById<ImageButton>(R.id.addbtn)
        val cats = findViewById<Button>(R.id.task_selected)
        val date = findViewById<Button>(R.id.filterDates_btn)

        date.setOnClickListener {
                val recyclerview = findViewById<RecyclerView>(R.id.mRecycler_task)
                recyclerview.layoutManager = LinearLayoutManager(this)
                val db = FirebaseFirestore.getInstance()
                val itemsRef = db.collection("Tasks")

// Get the user ID (this will depend on how you are authenticating users)
                val userid = FirebaseAuth.getInstance().currentUser?.uid

// Create a query that only returns documents where the userId field matches the current user's ID
                val datequery = itemsRef.whereEqualTo("userid",userid.toString().trim())
                    .whereGreaterThanOrEqualTo("date", DateClass.startDate)
                    .whereLessThanOrEqualTo("date", DateClass.endDate)

// Execute the query and get the results as a Task
                val task = datequery.get()

// Add an OnSuccessListener to the Task to get the results
                task.addOnSuccessListener { documents ->
                    // Create an empty list to hold the items
                    val items = mutableListOf<ItemsViewModel>()
                    if (documents.size() > 0) {

                        for (document in documents) {
                            // Get the data for each document
                            val name = document.getString("taskname") ?: ""
                            val description = document.getString("hours") ?: ""
                            val sub = document.getString("catergorytask") ?: ""
                            val date = document.getString("date") ?: ""
                            val imageUrl = document.getString("image") ?: ""
                            val item = ItemsViewModel(name, description,sub,date,imageUrl)
                            items.add(item)
                        }

                        // Update the RecyclerView with the new data
                        // ...
                        val sortedItems = items.sortedBy { it.date }
                        try {

                            val adapter = CustomAdapter(sortedItems.toMutableList())
                            recyclerview.adapter = adapter
                            adapter.notifyDataSetChanged()

                        } catch (e: Exception) {


                        }
                    } else {
                    }

                }
            }


        cat.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.putExtra("home", getIntent().getIntExtra("home",R.layout.home))
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()

        }

        cats.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.putExtra("home", getIntent().getIntExtra("home",R.layout.home))
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()

        }

        addbtn.setOnClickListener {
            val tform = Intent(this, TaskForm::class.java)
            overridePendingTransition(0, 0)
            startActivity(tform)
            finish()
        }
    }

    fun selectstartDate(view: View) {
        // Get current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Create date picker dialog
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                // Set selected date to the TextView
                DateClass.startDate = "$dayOfMonth/${month + 1}/$year"
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }

    fun selectendDate(view: View) {
        // Get current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Create date picker dialog
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                // Set selected date to the TextView
                DateClass.endDate = "$dayOfMonth/${month + 1}/$year"
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }


    fun print() {
        val recyclerview = findViewById<RecyclerView>(R.id.mRecycler_task)
        recyclerview.layoutManager = LinearLayoutManager(this)
        val db = FirebaseFirestore.getInstance()
        val itemsRef = db.collection("Tasks")

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
                val name = document.getString("taskname") ?: ""
                val description = document.getString("hours") ?: ""
                val sub = document.getString("catergorytask") ?: ""
                val date = document.getString("date") ?: ""
                val imageUrl = document.getString("image") ?: ""
                val item = ItemsViewModel(name, description,sub,date,imageUrl)
                items.add(item)
            }

            // Update the RecyclerView with the new data
            // ...
            val sortedItems = items.sortedBy { it.text }
            try {

                val adapter = CustomAdapter(sortedItems.toMutableList())
                recyclerview.adapter = adapter

            } catch (e: Exception) {


            }
        }
    }


    data class ItemsViewModel(val text: String, val hours: String, val sub: String, var date: String, val imageUrl: String)

    class CustomAdapter(var myDataList: MutableList<ItemsViewModel> = mutableListOf()) :
        RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.tastk_tab, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val itemsViewModel = myDataList[position]
            Glide.with(holder.itemView)
                .load(itemsViewModel.imageUrl)
                .circleCrop()
                .into(holder.imageView)

            holder.textView.text = itemsViewModel.text
            holder.textView2.text = itemsViewModel.hours
            holder.textView3.text = itemsViewModel.sub

            holder.itemView.setOnClickListener {
                val clickedData = myDataList[position]
                val context = holder.itemView.context
                val intent = Intent(context, TaskPage::class.java)
                intent.putExtra("task", clickedData.text)
                intent.putExtra("hours", clickedData.hours)
                val bundle = Bundle()
                val bundle2 = Bundle()

                bundle.putSerializable("myDataList", Home.TaskClass.tasks)
                intent.putExtra("position1", position) // use "position" as the key
                intent.putExtras(bundle)
                bundle2.putSerializable("Hours", Home.TaskClass.hours)
                intent.putExtra("position2", position) // use "position" as the key
                intent.putExtras(bundle2)
                intent.putExtra("itemId", clickedData.text)
                context.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return myDataList.size
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(R.id.mTitle2)
            val textView2: TextView = itemView.findViewById(R.id.mHpurs)
            val textView3: TextView = itemView.findViewById(R.id.mSubtitle)
            val imageView :ImageView = itemView.findViewById(R.id.task_item_image)
          }
      }
}