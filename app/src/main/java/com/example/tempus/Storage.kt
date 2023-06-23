package com.example.tempus


data class TaskStorage(
    val taskName: String,
    val categoryName: String,
    val description: String,
    val starTime: String,
    val endTime: String,
    val duration: String,
    val minGoal: String,
    val maxGoal: String,
    val dateAdded: String,
    val imageURL: String,
    val tabID: String,
    val userIdTask: String

)

data class CategoryStorage
    (
    val categoryID: String,
    val totalHours: String,
    val userIdCat: String
)

data class User(
    val firstname: String,
    val surname: String,
    val displayname: String,
    val emailaddress: String,
    val password: String,
    val confirmkey: String,
    val userid: String

)
