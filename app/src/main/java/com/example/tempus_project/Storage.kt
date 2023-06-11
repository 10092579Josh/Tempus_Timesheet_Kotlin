package com.example.tempus_project


data class Storage(

    val stuff:String


)
data class taskstore(
      val taskname:String,
      val catergorytask:String,
      val description:String,
      val startime:String,
      val endtime:String,
      val hours:String,
      val mingoal:String,
      val maxgoal:String,
      val date:String,
      val image:String,
      val userid:String

)

data class catergories
    (
    val catname:String,
    val cathours : String,
    val userid:String
)
data class User(
    val name: String,
    val surname: String,
    val usersname: String,
    val email: String,
    val password: String,
    val confirm: String,
    val userid: String


    )
