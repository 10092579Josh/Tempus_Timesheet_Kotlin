package com.example.tempus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditCategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_category)
        updateStuff()
        val upfate = findViewById<Button>(R.id.EditCategory)
        upfate.setOnClickListener()
        {

        }
    }
    fun updateStuff()
    {
        val tName = findViewById<TextView>(R.id.EditcatNameInput)

        val itemId = intent.getStringExtra("item_id")

        val db = FirebaseFirestore.getInstance()


        val userid = FirebaseAuth.getInstance().currentUser?.uid
// Query Firestore to get the data for the clicked item
        db.collection("CategoryStorage")
            .whereEqualTo("userIdCat", userid.toString().trim())
            .whereEqualTo("categoryID", itemId)
            .get()
            .addOnSuccessListener { documents ->
                // Get the last document in the result, which corresponds to the clicked item
                val document = documents.documents.lastOrNull()
                if (document != null) {

                    tName.text = document.getString("categoryID")



                }
            }
    }
}