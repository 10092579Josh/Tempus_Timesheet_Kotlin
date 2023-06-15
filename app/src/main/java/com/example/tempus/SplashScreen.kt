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


class SplashScreen : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var percentageText: TextView
    private lateinit var progressText: TextView

    private val SPLASH_DELAY: Long = 5000 // 3 seconds delay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.load_screen)

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

            val intent = Intent(this@SplashScreen, Home::class.java)
            intent.putExtra("home", R.layout.home)
            startActivity(intent)

            finish()
            updateBar(progressBar, progressText, 75)
            val splashImageView = findViewById<ImageView>(R.id.splashImageView)
            splashImageView.setImageResource(R.drawable.splash_screen_logo) // Replace with your image resource
// Bring the ImageView to the front
            splashImageView.bringToFront()
            splashImageView.invalidate()
            updateBar(progressBar, progressText, 100)
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this@SplashScreen, Home::class.java)
                intent.putExtra("home", R.layout.home)
                startActivity(intent)

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

                val intent = Intent(this@SplashScreen, Login::class.java)
                intent.putExtra("login", R.layout.login)


                startActivity(intent)
                finish()

            }, SPLASH_DELAY)
            updateBar(progressBar, progressText, 100)
        }
    }

    private fun updateBar(progressBar: ProgressBar, progressText: TextView, progress: Int) {
        val progressAnimator =
            ObjectAnimator.ofInt(progressBar, "progress", progressBar.progress, progress)
        progressAnimator.duration = 4900
        progressAnimator.interpolator = LinearInterpolator()
        progressAnimator.addUpdateListener {
            val currentProgress = it.animatedValue as Int
            percentageText.text = "${currentProgress}%"
            when (currentProgress) {
                in 0..25 -> progressText.text = "Downloading data..."
                in 26..50 -> progressText.text = "Optimizing layouts..."
                in 51..75 -> progressText.text = "Installing porn..."
                in 76..100 -> progressText.text = "Done!"
            }
        }
        progressAnimator.start()
    }
}
