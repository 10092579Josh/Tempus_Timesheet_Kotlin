package com.example.tempus

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.SystemClock
import android.widget.RemoteViews

class TempusWidget : AppWidgetProvider() {

    companion object {
        const val ACTION_START_STOPWATCH = "com.example.tempus.ACTION_START_STOPWATCH"
        const val ACTION_STOP_STOPWATCH = "com.example.tempus.ACTION_STOP_STOPWATCH"
        const val EXTRA_WIDGET_ID = "widgetId"

        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val remoteViews = RemoteViews(context.packageName, R.layout.tempus_widget)

            // Set click listeners for start and stop buttons
            val intentStart = Intent(context, TempusWidget::class.java).apply {
                action = ACTION_START_STOPWATCH
                putExtra(EXTRA_WIDGET_ID, appWidgetId)
            }
            val pendingIntentStart = PendingIntent.getBroadcast(
                context,
                appWidgetId,
                intentStart,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            remoteViews.setOnClickPendingIntent(R.id.button_start, pendingIntentStart)

            val intentStop = Intent(context, TempusWidget::class.java).apply {
                action = ACTION_STOP_STOPWATCH
                putExtra(EXTRA_WIDGET_ID, appWidgetId)
            }
            val pendingIntentStop = PendingIntent.getBroadcast(
                context,
                appWidgetId,
                intentStop,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            remoteViews.setOnClickPendingIntent(R.id.button_stop, pendingIntentStop)

            // Get the preferences for the widget
            val sharedPreferences = context.getSharedPreferences(
                "widget_preferences",
                Context.MODE_PRIVATE
            )
            val theme = sharedPreferences.getString("theme", "Light") ?: "Light"
            val fontSize = sharedPreferences.getInt("font_size", 50)
            val defaultTime = sharedPreferences.getLong("default_time", 10)

            // Apply the theme preference to the widget background
            val backgroundColor = when (theme) {
                "Light" -> Color.WHITE
                "Dark" -> Color.GRAY
                "Colorful" -> Color.rgb(116, 69, 221)
                else -> Color.WHITE
            }
            remoteViews.setInt(R.id.widget_layout, "setBackgroundColor", backgroundColor)

            // Apply the font size preference to the widget text view
            remoteViews.setFloat(R.id.text_view_countdown, "setTextSize", fontSize.toFloat())

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        when (intent.action) {
            ACTION_START_STOPWATCH -> {
                val widgetId =
                    intent.getIntExtra(EXTRA_WIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
                if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                    val remoteViews =
                        RemoteViews(context.packageName, R.layout.tempus_widget)

                    val intentStop = Intent(context, TempusWidget::class.java).apply {
                        action = ACTION_STOP_STOPWATCH
                        putExtra(EXTRA_WIDGET_ID, widgetId)
                    }
                    val pendingIntentStop = PendingIntent.getBroadcast(
                        context,
                        widgetId,
                        intentStop,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    remoteViews.setOnClickPendingIntent(R.id.button_stop, pendingIntentStop)

                    // Start the stopwatch
                    remoteViews.setChronometer(
                        R.id.chronometer_stopwatch,
                        SystemClock.elapsedRealtime(),
                        null,
                        true
                    )

                    val appWidgetManager = AppWidgetManager.getInstance(context)
                    appWidgetManager.updateAppWidget(widgetId, remoteViews)
                }
            }
            ACTION_STOP_STOPWATCH -> {
                val widgetId =
                    intent.getIntExtra(EXTRA_WIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
                if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                    val remoteViews =
                        RemoteViews(context.packageName, R.layout.tempus_widget)

                    val intentStart = Intent(context, TempusWidget::class.java).apply {
                        action = ACTION_START_STOPWATCH
                        putExtra(EXTRA_WIDGET_ID, widgetId)
                    }
                    val pendingIntentStart = PendingIntent.getBroadcast(
                        context,
                        widgetId,
                        intentStart,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    remoteViews.setOnClickPendingIntent(R.id.button_start, pendingIntentStart)

                    // Stop the stopwatch
                    remoteViews.setChronometer(
                        R.id.chronometer_stopwatch,
                        SystemClock.elapsedRealtime(),
                        null,
                        false
                    )

                    val appWidgetManager = AppWidgetManager.getInstance(context)
                    appWidgetManager.updateAppWidget(widgetId, remoteViews)
                }
            }
        }
    }
}
