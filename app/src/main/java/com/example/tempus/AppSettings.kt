package com.example.tempus

import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import de.keyboardsurfer.android.widget.crouton.Crouton
import de.keyboardsurfer.android.widget.crouton.Style
import java.io.File
import kotlin.system.exitProcess

class AppSettings : AppCompatActivity() {
    private val M = messages()
    private  val e = Errors()
    private val emailtype = Crouton.makeText(this, e.NotYourUsername, Style.ALERT)
    private val passwordEmpty = Crouton.makeText(this, e.PasswordCantBeEmpty, Style.ALERT)
    private val usernameEmpty = Crouton.makeText(this, e.EmptyUserName, Style.ALERT)
    private val nodetails = Crouton.makeText(this, e.NoDetailsEntered, Style.ALERT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_settings)
        Security()
        populatefields()
        FirebaseApp.initializeApp(this)
        val homebtn = findViewById<ImageButton>(R.id.hometbtn)
        val breaksbtn = findViewById<ImageButton>(R.id.breakstbtn)
        val statsbtn = findViewById<ImageButton>(R.id.statstbtn)
        val settingsbtn = findViewById<ImageButton>(R.id.settingstbtn)
        val accsetting = findViewById<CardView>(R.id.account_setting)
        val addbtn = findViewById<ImageButton>(R.id.addbtn)
        val logout = findViewById<CardView>(R.id.logout)
        val deleteappdata = findViewById<CardView>(R.id.delete_data)
        val deleteuser = findViewById<CardView>(R.id.delete_user)
        val appcache = findViewById<CardView>(R.id.delete_cache)

        appcache.setOnClickListener()
        {
            val clearcache = AlertDialog.Builder(this)
            clearcache.setTitle("Do you want to clear cache(closes app)?")
            clearcache.setItems(arrayOf("Yes", "Cancel")) { _, which ->
                when (which) {

                    0 -> Dialog.BUTTON_NEGATIVE
                    1 -> activate()
                }

            }

            val dialog = clearcache.create()
            dialog.show()

        }
        deleteuser.setOnClickListener()
        {
            val userdeletion = AlertDialog.Builder(this)
            userdeletion.setTitle("Are you sure?")
            userdeletion.setItems(arrayOf("Yes", "Cancel")) { _, which ->
                when (which) {

                    0 -> Dialog.BUTTON_NEGATIVE
                    1 -> DeleteUser()
                }

            }

            val dialog = userdeletion.create()
            dialog.show()


        }
        homebtn.setOnClickListener {
            val homepage = Intent(this, Home::class.java)
            homepage.putExtra("home", getIntent().getIntExtra("home", R.layout.home))
            startActivity(homepage)
            overridePendingTransition(0, 0)
            finish()
        }
//
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
        addbtn.setOnClickListener() {
            val taskform = Intent(this, TaskForm::class.java)
            startActivity(taskform)
            overridePendingTransition(0, 0)
            finish()

        }
        logout.setOnClickListener() {
            FirebaseAuth.getInstance().signOut()


            val message = " ${preloads.usersname} HAS LOGGED OUT!"
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("isFirstLogin", true).apply()
            preloads.usersname = null
            val loginpage = Intent(this@AppSettings, Login::class.java)
            loginpage.putExtra("login", R.layout.login)
            overridePendingTransition(0, 0)
            startActivity(loginpage)
        }
        accsetting.setOnClickListener() {
            if (preloads.usersname == null) {
                AccountVerify()
            } else {
                val accsettings = Intent(this, UserDetails::class.java)
                startActivity(accsettings)
                overridePendingTransition(0, 0)
                finish()


            }
        }
        deleteappdata.setOnClickListener() {

            val deleteoptions = AlertDialog.Builder(this)
            deleteoptions.setTitle("Choose an option")
            deleteoptions.setItems(
                arrayOf("Delete all Images", "Delete Tasks and Images", "Wipe Application DATA")
            ) { _, which ->
                when (which) {

                    0 -> ImageDelete()
                    1 -> TasksImageDelete()
                    2 -> AllDelete()

                }


            }
            val dialog = deleteoptions.create()
            dialog.show()
        }
    }
fun activate()
{
    deleteCache(this)
    restartApp()
}
    private fun Security() {

        val auth = FirebaseAuth.getInstance()
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {

                val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
                sharedPreferences.edit().putBoolean("isFirstLogin", true).apply()
                preloads.usersname = null
                val intent = Intent(this@AppSettings, Login::class.java)
                intent.putExtra("login", R.layout.login)
                overridePendingTransition(0, 0)
                startActivity(intent)

            }
        }

        val user = FirebaseAuth.getInstance().currentUser
        user?.reload()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {

            } else {
                val exception = task.exception
                if (exception is FirebaseAuthInvalidUserException) {
                    val errorCode = exception.errorCode
                    if (errorCode == "ERROR_USER_NOT_FOUND") {
                        val sharedPreferences =
                            getSharedPreferences("preferences", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putBoolean("isFirstLogin", true).apply()
                        preloads.usersname = null
                        val loginpage = Intent(this@AppSettings, Login::class.java)
                        loginpage.putExtra("login", R.layout.login)
                        overridePendingTransition(0, 0)
                        startActivity(loginpage)
                    }
                }
            }
        }

    }

    private var isDialogOpen = false
    private fun restartApp() {
        val intent = Intent(applicationContext, Home::class.java)
        val mPendingIntentId = 0
        val mPendingIntent = PendingIntent.getActivity(
            applicationContext,
            mPendingIntentId,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val mgr = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent)
        exitProcess(0)
    }
    fun EmailCheck(email: String): Boolean {
        val emailCheck = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$"
        return emailCheck.toRegex().matches(email)
    }
    private fun AccountVerify() {
        if (!isDialogOpen) {
            isDialogOpen = true
            val builder = AlertDialog.Builder(this)

            val titleView = layoutInflater.inflate(R.layout.authenticate_title, null)
            builder.setCustomTitle(titleView)

            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL

            // Set up the username input
            val usernameInput = EditText(this)
            usernameInput.hint = "Username"
            layout.addView(usernameInput)

            // Set up the password input
            val passwordInput = EditText(this)
            passwordInput.hint = "password"
            passwordInput.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            layout.addView(passwordInput)

            builder.setView(layout)

            with(builder) {
                setNegativeButtonIcon(
                    ContextCompat.getDrawable(
                        context, R.drawable.baseline_cancel_presentation_24
                    )
                ).setNegativeButton("CLOSE") { _, _ ->
                    Log.d("MyTag", "closing")
                    val alertDialog = builder.create()
                    alertDialog.dismiss()
                    Log.d("MyTag", "closed")
                    isDialogOpen = false
                    Log.d("MyTag", "$isDialogOpen")
                }
                setPositiveButtonIcon(
                    ContextCompat.getDrawable(
                        context, R.drawable.baseline_check_box_24
                    )
                ).setPositiveButton("SUBMIT") { _, _ ->
                    val username = usernameInput.text.toString().trim()
                    val password = passwordInput.text.toString().trim()
                    val verify = username + password
                    val database = Firebase.database
                    val userid = FirebaseAuth.getInstance().currentUser?.uid
                    val myRef = database.getReference("UserDetails")
                    val oldRef = FirebaseDatabase.getInstance().getReference("UserDetails/$verify")
                    val newRef = FirebaseDatabase.getInstance()
                        .getReference("UserDetails/${userid.toString()}")

                    if (usernameInput.text.isNullOrEmpty()) {
                        usernameEmpty.show()

                    }  else if (EmailCheck(username)) {
                        emailtype.show()

                    }
                    else if (passwordInput.text.isNullOrEmpty()) {
                        passwordEmpty.show()

                    } else if (usernameInput.text.isNullOrEmpty() && passwordInput.text.isNullOrEmpty()) {
                        nodetails.show()

                    } else {

                        oldRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val data = dataSnapshot.value as? Map<*, *>
                                if (dataSnapshot.exists()) {
                                    if (data != null) {
                                        newRef.setValue(data) { error, _ ->
                                            if (error == null) {
                                                oldRef.removeValue()
                                                myRef.child(userid.toString()).child("userid")
                                                    .setValue(userid)
                                            }
                                        }
                                    } else {
                                        validerror(Errors())
                                    }
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Handle error
                            }
                        })
                    }
                    isDialogOpen = false
                }
            }.create()

            val alertDialog = builder.create()
            alertDialog.show()
            val button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
            val layoutParams = button.layoutParams as LinearLayout.LayoutParams
            with(button) {

                setPadding(0, 0, 20, 0)
                setTextColor(Color.WHITE)
                setBackgroundColor(Color.WHITE)

                layoutParams.weight = 10f
                button.layoutParams = layoutParams
            }
            alertDialog.show()
            val buttons = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            val layoutParams2 = buttons.layoutParams as LinearLayout.LayoutParams
            with(buttons) {
                setPadding(250, 0, 20, 0)
                setTextColor(Color.WHITE)
                layoutParams2.weight = 10f
                buttons.layoutParams = layoutParams2
            }

            builder.setOnCancelListener { isDialogOpen = false }
        }

    }


    fun validerror(errors: Errors) {
        val crouton = Crouton.makeText(this, errors.ValidationError, Style.ALERT)
        crouton.show()
    }

    object preloads {
        var names: String = ""
        var surname: String = ""
        var emails: String = ""
        var usersname: String? = null
        var conpass: String = ""
        var pass: String = ""
    }

    fun populatefields() {
        val userid = FirebaseAuth.getInstance().currentUser?.uid
        val database = Firebase.database
        val myRef = database.getReference("UserDetails")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {

                    val userId = data.child("userid").getValue(String::class.java)
                    if (userId.toString().trim() == userid.toString().trim()) {
                        Log.d("datas", preloads.names)
                        if (preloads.names.isNullOrEmpty()) {
                            preloads.names =
                                data.child("firstname").getValue(String::class.java).toString()
                            preloads.emails =
                                data.child("emailaddress").getValue(String::class.java).toString()
                            preloads.surname =
                                data.child("surname").getValue(String::class.java).toString()
                            preloads.usersname =
                                data.child("displayname").getValue(String::class.java).toString()
                            preloads.conpass =
                                data.child("confirmkey").getValue(String::class.java).toString()
                            preloads.pass =
                                data.child("password").getValue(String::class.java).toString()


                        }
                    }


                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

    fun AllDelete() {

        val db = FirebaseFirestore.getInstance()
        val userid = FirebaseAuth.getInstance().currentUser?.uid

        val storage = Firebase.storage

        db.collection("TaskStorage").whereEqualTo("userIdTask", userid).get()
            .addOnSuccessListener { tasks ->
                for (task in tasks) {
                    val taskName = task.getString("taskName") ?: ""
                    val imageRef = storage.reference.child(taskName)
                    imageRef.delete()
                }
            }




        db.collection("TaskStorage").whereEqualTo("userIdTask", userid).get()
            .addOnSuccessListener { tasks ->
                for (task in tasks) {
                    db.collection("TaskStorage").document(task.id).delete()
                }
            }


        db.collection("CategoryStorage").whereEqualTo("userIdCat", userid).get()
            .addOnSuccessListener { categories ->
                for (category in categories) {
                    db.collection("CategoryStorage").document(category.id).delete()
                }
            }
    }

    fun TasksImageDelete() {
        val db = FirebaseFirestore.getInstance()
        val userid = FirebaseAuth.getInstance().currentUser?.uid

        val storage = Firebase.storage

        db.collection("TaskStorage").whereEqualTo("userIdTask", userid).get()
            .addOnSuccessListener { tasks ->
                for (task in tasks) {
                    val taskName = task.getString("taskName") ?: ""
                    val imageRef = storage.reference.child(taskName)
                    imageRef.delete()
                }
            }




        db.collection("TaskStorage").whereEqualTo("userIdTask", userid).get()
            .addOnSuccessListener { tasks ->
                for (task in tasks) {
                    db.collection("TaskStorage").document(task.id).delete()
                }
            }


    }

    fun ImageDelete() {


        val db = FirebaseFirestore.getInstance()
        val userid = FirebaseAuth.getInstance().currentUser?.uid

        val storage = Firebase.storage

        db.collection("TaskStorage").whereEqualTo("userIdTask", userid).get()
            .addOnSuccessListener { tasks ->
                for (task in tasks) {
                    val taskName = task.getString("taskName") ?: ""
                    val imageRef = storage.reference.child(taskName)
                    imageRef.delete()
                }
            }


    }

    fun DeleteUser() {
        val db = FirebaseFirestore.getInstance()
        val userid = FirebaseAuth.getInstance().currentUser?.uid

        val storage = Firebase.storage

        db.collection("TaskStorage").whereEqualTo("userIdTask", userid).get()
            .addOnSuccessListener { tasks ->
                for (task in tasks) {
                    val taskName = task.getString("taskName") ?: ""
                    val imageRef = storage.reference.child(taskName)
                    imageRef.delete()
                }
            }




        db.collection("TaskStorage").whereEqualTo("userIdTask", userid).get()
            .addOnSuccessListener { tasks ->
                for (task in tasks) {
                    db.collection("TaskStorage").document(task.id).delete()
                }
            }


        db.collection("CategoryStorage").whereEqualTo("userIdCat", userid).get()
            .addOnSuccessListener { categories ->
                for (category in categories) {
                    db.collection("CategoryStorage").document(category.id).delete()
                }
            }
        val TempusUser = FirebaseAuth.getInstance().currentUser
        TempusUser?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val sharedPreferences =
                    getSharedPreferences("preferences", Context.MODE_PRIVATE)
                sharedPreferences.edit().putBoolean("isFirstLogin", true).apply()
                preloads.usersname = null
                val intent = Intent(this@AppSettings, Login::class.java)
                intent.putExtra("login", R.layout.login)
                overridePendingTransition(0, 0)
                Toast.makeText(this, M.DeleteConfirmation, Toast.LENGTH_SHORT).show()
                startActivity(intent)


            }
        }
    }
    fun deleteCache(context: Context) {
        try {
            val dir: File = context.cacheDir
            deleteDir(dir)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children: Array<String> = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir?.delete() ?: false
    }
}