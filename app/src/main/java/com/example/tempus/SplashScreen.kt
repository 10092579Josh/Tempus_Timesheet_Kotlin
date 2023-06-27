package com.example.tempus

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SplashScreen : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var percentageText: TextView
    private lateinit var progressText: TextView

    private val splashDelay: Long = 5000 // 3 seconds delay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.load_screen)
        withq()
        try {
            progressBar = findViewById(R.id.progressBar)
            percentageText = findViewById(R.id.percentageText)
            progressText = findViewById(R.id.progressText2)
            updateBar(progressBar, progressText, 25)

            val auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            Log.d("MyApp", "$currentUser")
            when {
                currentUser != null -> {
                    updateBar(progressBar, progressText, 50)

                    Log.d("MyApp", "Method X started")


                    updateBar(progressBar, progressText, 75)
                    val splashImageView = findViewById<ImageView>(R.id.splashImageView)
                    splashImageView.setImageResource(R.drawable.splash_screen_logo)

                    splashImageView.bringToFront()
                    splashImageView.invalidate()
                    updateBar(progressBar, progressText, 100)
                    Handler(Looper.getMainLooper()).postDelayed({
                        val homepage = Intent(this@SplashScreen, Home::class.java)
                        homepage.putExtra("home", R.layout.home)
                        startActivity(homepage)

                        finish()

                    }, splashDelay)
                }

                else -> {
                    updateBar(progressBar, progressText, 50)
                    val splashImageView = findViewById<ImageView>(R.id.splashImageView)
                    splashImageView.setImageResource(R.drawable.splash_screen_logo)

                    splashImageView.bringToFront()
                    splashImageView.invalidate()
                    updateBar(progressBar, progressText, 75)

                    Handler(Looper.getMainLooper()).postDelayed({

                        val loginpage = Intent(this@SplashScreen, Login::class.java)
                        loginpage.putExtra("login", R.layout.login)


                        startActivity(loginpage)
                        finish()

                    }, splashDelay)
                    updateBar(progressBar, progressText, 100)
                }
            }
        } catch (E: Exception) {
            val message = Exception()
            Log.d("myapp", "$message")
        }
    }

    private fun updateBar(progressBar: ProgressBar, progressText: TextView, progress: Int) {
        try {
            val animator =
                ObjectAnimator.ofInt(progressBar, "progress", progressBar.progress, progress)
            animator.duration = 4900
            animator.interpolator = LinearInterpolator()
            animator.addUpdateListener {
                val currentProgress = it.animatedValue as Int
                percentageText.text = "${currentProgress}%"
                when (currentProgress) {
                    in 0..25 -> progressText.text = "Downloading data..."
                    in 26..50 -> progressText.text = "Optimizing layouts..."
                    in 51..75 -> progressText.text = "Organising Your Life ;)..."
                    in 76..100 -> progressText.text = "And Here we GO!"
                }
            }
            animator.start()
        } catch (E: Exception) {
            //stuff to do

        }
    }

    fun withq ()
    {
        val firestore = Firebase.firestore
        val userid = Firebase.auth.currentUser?.uid
        val itemAdd = firestore.collection("SecurityQuestions")

        val q1 = "YOUR MOTHERS MAIDEN NAME?"
        val q2 =  "YOUR FAVORITE PETS NAME?"
        val q3 = "WHAT HIGH SCHOOL DID YOU ATTEND?"
        val q4 = "YOUR FAVORITE MOVIE?"
        val q5 = "YOUR FIRST GIRLFRIEND?"

        val breaks = SecurityQuestions(q1, q2, q3,q4,q5)
val b = "securityQuestions"
        val docRef = itemAdd.document(b)
        docRef.set(breaks)
    }
}
