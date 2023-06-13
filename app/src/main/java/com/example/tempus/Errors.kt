package com.example.tempus

public data class Errors(
    val LoginError:String = "INCORRECT EMAIL OR PASSWORD",
    val RegEmailError: String = "EMAIL ALREADY IN USE",
    val ValidationError:String = "cant find user details please check user&password"



)
