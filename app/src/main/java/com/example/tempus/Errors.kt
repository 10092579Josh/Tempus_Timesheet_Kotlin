package com.example.tempus

public data class Errors(
    val LoginError:String = "INCORRECT EMAIL OR PASSWORD",
    val RegEmailError: String = "EMAIL ALREADY IN USE",
    val ValidationError:String = " please check credentials",
    val NoNullsPassWord:String = " PASASWORD CANT BE NULLL"



)
