package com.example.tempus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.keyboardsurfer.android.widget.crouton.Crouton
import de.keyboardsurfer.android.widget.crouton.Style

class EditCategoryActivity : AppCompatActivity() {
    private val e = Errors()
    private val emptycatname = Crouton.makeText(this, e.CatNewNameEmpty, Style.ALERT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_category)
        updateStuff()

        val upfate = findViewById<Button>(R.id.EditCategory)
        val itemId = intent.getStringExtra("item_id")
        val userid = FirebaseAuth.getInstance().currentUser?.uid
        val tName = findViewById<TextView>(R.id.EditcatNameInput)
        val ids = findViewById<TextView>(R.id.edit_id)
        val db = FirebaseFirestore.getInstance()
        upfate.setOnClickListener()
        {
            if (tName.text.isNullOrEmpty()) {

                emptycatname.show()

            } else {


                db.collection("CategoryStorage").whereEqualTo("categoryID", ids.text.toString())
                    .whereEqualTo("userIdCat", userid).get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            document.reference.update("categoryID", tName.text.toString())

                            val firestore = Firebase.firestore
// get the data from 'oldDocument'
                            firestore.collection("CategoryStorage").document(ids.text.toString())
                                .get()
                                .addOnSuccessListener { document ->
                                    if (document != null) {
                                        val data = document.data
                                        // save the data to 'newDocument'
                                        firestore.collection("CategoryStorage")
                                            .document(tName.text.toString())
                                            .set(data!!)
                                            .addOnSuccessListener {
                                                // delete the old document
                                                firestore.collection("CategoryStorage")
                                                    .document(ids.text.toString())
                                                    .delete()
                                            }
                                    }
                                    Toast.makeText(
                                        applicationContext,
                                        "updated category",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()


                                }
                        }


                    }
            }
        }

    }
    fun updateStuff()
    {
        val tName = findViewById<TextView>(R.id.EditcatNameInput)
        val ids = findViewById<TextView>(R.id.edit_id)

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
                    ids.text = document.getString("categoryID")



                }
            }
    }
}