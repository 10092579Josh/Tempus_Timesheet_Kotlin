package com.example.tempus

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TempusWidgetConfigureActivity : AppCompatActivity() {
    private lateinit var spinnerTheme: Spinner
    private lateinit var seekBarFontSize: SeekBar
    private lateinit var textViewFontSize: TextView
    private lateinit var buttonConfirm: Button
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    @SuppressLint("SetTextI18n", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tempus_widget_configure)

        spinnerTheme = findViewById(R.id.spinner_theme)
        seekBarFontSize = findViewById(R.id.seek_bar_font_size)
        textViewFontSize = findViewById(R.id.text_view_font_size)
        buttonConfirm = findViewById(R.id.button_confirm)

        // Set up the spinner with some theme options
        val themes = arrayOf("Light", "Dark", "Colorful")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, themes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTheme.adapter = adapter

        // Set up the seek bar with some font size options
        seekBarFontSize.progress = 50 // Default value is 50%
        textViewFontSize.text = "50%" // Show the progress as a percentage



        // Listen to the spinner selection and save the theme preference
        spinnerTheme.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val theme = parent?.getItemAtPosition(position) as String // Get the selected theme
                saveThemePreference(theme) // Save it to a shared preferences file or a database
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Listen to the seek bar progress and save the font size preference
        seekBarFontSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textViewFontSize.text = "$progress%" // Show the progress as a percentage
                saveFontSizePreference(progress) // Save it to a shared preferences file or a database
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Do nothing
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Do nothing
            }
        })

        // Listen to the edit text input and save the default time preference


        // Listen to the button click and finish the configuration activity
        buttonConfirm.setOnClickListener {
            finishConfiguration() // Finish the configuration activity and update the widget
        }

        // Retrieve the app widget id from the intent
        appWidgetId = intent?.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)!!
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }
    }

    private fun saveThemePreference(theme: String) {
        // Save the theme preference to a shared preferences file or a database
        // For example, using a shared preferences file:
        val sharedPreferences = getSharedPreferences("widget_preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("theme", theme)
        editor.apply()
    }

    private fun saveFontSizePreference(fontSize: Int) {
        // Save the font size preference to a shared preferences file or a database
        // For example, using a shared preferences file:
        val sharedPreferences = getSharedPreferences("widget_preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("font_size", fontSize)
        editor.apply()
    }


    private fun finishConfiguration() {
        // Finish the configuration activity and update the widget
        // For example, using an intent:
        val appWidgetId = intent?.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        val appWidgetManager = AppWidgetManager.getInstance(this)
        TempusWidget.updateAppWidget(this, appWidgetManager, appWidgetId ?: return)

        val resultValue = Intent().apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }

        setResult(RESULT_OK, resultValue)

        finish()
    }
}
