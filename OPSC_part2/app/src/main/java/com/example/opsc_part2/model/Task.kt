package com.example.opsc_part2.model

import android.widget.DatePicker

data class Task (
    val imageSrc: String,
    val categoryName: String,
    val startDate: DatePicker,
    val endDate: String,
    val minGoal: String,
    val maxGoal: String,
    val tag: String,
    val description: String

        )