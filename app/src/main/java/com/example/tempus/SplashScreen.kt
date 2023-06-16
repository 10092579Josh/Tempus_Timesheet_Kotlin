package com.example.tempus

import android.animation.ObjectAnimator
import android.content.Intent
import android.widget.ImageView
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class SplashScreen : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var percentageText: TextView
    private lateinit var progressText: TextView

    private val SPLASH_DELAY: Long = 5000 // 3 seconds delay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.load_screen)
try {
    progressBar = findViewById(R.id.progressBar)
    percentageText = findViewById(R.id.percentageText)
    progressText = findViewById(R.id.progressText2)
    updateBar(progressBar, progressText, 25)

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    Log.d("MyApp", "$currentUser")
    if (currentUser != null) {
        updateBar(progressBar, progressText, 50)
        // User is signed in, redirect to main activity
        Log.d("MyApp", "Method X started")

        // Preload the layout for the Home activity
        populatefields()
        updateBar(progressBar, progressText, 75)
        val splashImageView = findViewById<ImageView>(R.id.splashImageView)
        splashImageView.setImageResource(R.drawable.splash_screen_logo) // Replace with your image resource
// Bring the ImageView to the front
        splashImageView.bringToFront()
        splashImageView.invalidate()
        updateBar(progressBar, progressText, 100)
        Handler(Looper.getMainLooper()).postDelayed({
            val homepage = Intent(this@SplashScreen, Home::class.java)
            homepage.putExtra("home", R.layout.home)
            startActivity(homepage)

            finish()

        }, SPLASH_DELAY)
    } else {
        updateBar(progressBar, progressText, 50)
        val splashImageView = findViewById<ImageView>(R.id.splashImageView)
        splashImageView.setImageResource(R.drawable.splash_screen_logo) // Replace with your image resource
// Bring the ImageView to the front
        splashImageView.bringToFront()
        splashImageView.invalidate()
        updateBar(progressBar, progressText, 75)

        Handler(Looper.getMainLooper()).postDelayed({

            val loginpage = Intent(this@SplashScreen, Login::class.java)
            loginpage.putExtra("login", R.layout.login)


            startActivity(loginpage)
            finish()

        }, SPLASH_DELAY)
        updateBar(progressBar, progressText, 100)
    }
}catch (E:Exception)
{ val mesage = Exception()
    Log.d("myapp","${mesage}")
}
    }

    private fun updateBar(progressBar: ProgressBar, progressText: TextView, progress: Int) {
        val Animator = ObjectAnimator.ofInt(progressBar, "progress", progressBar.progress, progress)
        Animator.duration = 4900
        Animator.interpolator = LinearInterpolator()
        Animator.addUpdateListener {
            val currentProgress = it.animatedValue as Int
            percentageText.text = "${currentProgress}%"
            when (currentProgress) {
                in 0..25 -> progressText.text = "Downloading data..."
                in 26..50 -> progressText.text = "Optimizing layouts..."
                in 51..75 -> progressText.text = "Installing porn..."
                in 76..100 -> progressText.text = "Done!"
            }
        }
        Animator.start()
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

                        AppSettings.preloads.names = data.child("name").getValue(String::class.java).toString()
                        AppSettings.preloads.emails = data.child("email").getValue(String::class.java).toString()
                        AppSettings.preloads.surname = data.child("surname").getValue(String::class.java).toString()
                        AppSettings.preloads.usersname = data.child("usersname").getValue(String::class.java).toString()
                        AppSettings.preloads.conpass = data.child("confirm").getValue(String::class.java).toString()
                        AppSettings.preloads.pass = data.child("password").getValue(String::class.java).toString()

                    }


                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })

    }
}
