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
import de.keyboardsurfer.android.widget.crouton.Crouton
import de.keyboardsurfer.android.widget.crouton.Style
import java.util.*

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
            val itemsRef = db.collection("TaskStorage")

            val userid = FirebaseAuth.getInstance().currentUser?.uid

            val datequery = itemsRef.whereEqualTo("userIdTask", userid.toString().trim())
                .whereGreaterThanOrEqualTo("dateAdded", DateClass.startDate)
                .whereLessThanOrEqualTo("dateAdded", DateClass.endDate)

            val task = datequery.get()

            task.addOnSuccessListener { documents ->

                val items = mutableListOf<ItemsViewModel>()
                if (documents.size() > 0) {

                    for (document in documents) {
                        val name = document.getString("taskName") ?: ""
                        val description = document.getString("duration") ?: ""
                        val sub = document.getString("categoryName") ?: ""
                        val date = document.getString("dateAdded") ?: ""
                        val imageUrl = document.getString("imageURL") ?: ""
                        val item = ItemsViewModel(name, description, sub, date, imageUrl)
                        items.add(item)
                    }
                    Log.d("taskcheck",items.toString())

                    val sortedItems = items.sortedBy { it.date }
                    try {

                        val adapter = CustomAdapter(sortedItems.toMutableList())
                        recyclerview.adapter = adapter
                        adapter.notifyDataSetChanged()

                    } catch (e: Exception) {


                    }
                } else {
                    val crouton = Crouton.makeText(this, "NO TASKS WITHIN THIS RANGE FOUND", Style.INFO)
                    crouton.show()
                }

            }.addOnFailureListener()
            {
                val crouton = Crouton.makeText(this, "DATA ERROR:PLEASE CHECK CONNECTION OR CONTACT CUSTOMER SERVICES", Style.ALERT)
                crouton.show()
            }
        }


        cat.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.putExtra("home", getIntent().getIntExtra("home", R.layout.home))
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()

        }

        cats.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.putExtra("home", getIntent().getIntExtra("home", R.layout.home))
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

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this, { _, year, month, dayOfMonth ->

                DateClass.startDate = "$dayOfMonth/${month + 1}/$year"
            }, year, month, dayOfMonth
        )
        datePickerDialog.show()
    }

    fun selectendDate(view: View) {

        val taskcalendar = Calendar.getInstance()
        val taskyear = taskcalendar.get(Calendar.YEAR)
        val taskmonth = taskcalendar.get(Calendar.MONTH)
        val taskdayOfMonth = taskcalendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this, { _, year, month, dayOfMonth ->
                DateClass.endDate = "$dayOfMonth/${month + 1}/$year"
            }, taskyear, taskmonth, taskdayOfMonth
        )
        datePickerDialog.show()
    }


    fun print() {
        val recyclerview = findViewById<RecyclerView>(R.id.mRecycler_task)
        recyclerview.layoutManager = LinearLayoutManager(this)
        val db = FirebaseFirestore.getInstance()
        val itemsRef = db.collection("TaskStorage")


        val userid = FirebaseAuth.getInstance().currentUser?.uid


        val query = itemsRef.whereEqualTo("userIdTask", userid.toString().trim())


        val task = query.get()

        task.addOnSuccessListener { documents ->

            val items = mutableListOf<ItemsViewModel>()

            for (document in documents) {

                val name = document.getString("taskName") ?: ""
                val description = document.getString("duration") ?: ""
                val sub = document.getString("categoryName") ?: ""
                val date = document.getString("dateAdded") ?: ""
                val imageUrl = document.getString("imageURL") ?: ""
                val item = ItemsViewModel(name, description, sub, date, imageUrl)
                items.add(item)
            }


            val sortedItems = items.sortedBy { it.text }
            try {

                val adapter = CustomAdapter(sortedItems.toMutableList())
                recyclerview.adapter = adapter

            } catch (e: Exception) {


            }
        }
    }


    data class ItemsViewModel(
        val text: String, val hours: String, val sub: String, var date: String, val imageUrl: String
    )

    class CustomAdapter(var myDataList: MutableList<ItemsViewModel> = mutableListOf()) :
        RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.tastk_tab, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val itemsViewModel = myDataList[position]
            Glide.with(holder.itemView).load(itemsViewModel.imageUrl).circleCrop()
                .into(holder.imageView)

            holder.textView.text = itemsViewModel.text
            holder.textView2.text = itemsViewModel.hours
            holder.textView3.text = itemsViewModel.sub

            holder.itemView.setOnClickListener {
                val clickedData = myDataList[position]
                val context = holder.itemView.context
                val intent = Intent(context, TaskPage::class.java)
                intent.putExtra("taskName", clickedData.text)
                intent.putExtra("duration", clickedData.hours)
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
            val imageView: ImageView = itemView.findViewById(R.id.task_item_image)
        }
    }


}