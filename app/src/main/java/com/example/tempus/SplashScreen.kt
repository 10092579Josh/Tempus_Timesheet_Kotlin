package com.example.tempus

import android.content.Intent
import android.widget.ImageView
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class SplashScreen : AppCompatActivity() {

    private val SPLASH_DELAY: Long = 3000 // 3 seconds delay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.load_screen)

        val splashImageView = findViewById<ImageView>(R.id.splashImageView)
        splashImageView.setImageResource(R.drawable.splash_screen_logo) // Replace with your image resource
// Bring the ImageView to the front
        splashImageView.bringToFront()

        // Invalidate the view hierarchy to ensure the changes take effect
        splashImageView.invalidate()

        Handler().postDelayed({
            val intent = Intent(this@SplashScreen, Login::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_DELAY)
    }
}
