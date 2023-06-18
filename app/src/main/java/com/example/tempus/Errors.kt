package com.example.tempus

data class Errors(
    val LoginError: String = "INCORRECT EMAIL OR PASSWORD",
    val RegEmailError: String = "EMAIL ALREADY IN USE",
    val ValidationError: String = " please check credentials",
    val EmailValidationEmptyError: String = "Email cant be empty",
    val NoNullsPassWord: String = " PASASWORD CANT BE NULLL",
    val IllegalCharacterHash: String = "# Cant be used in Emails",
    var PasswordCantBeEmpty: String = "Password field cant be empty",
    var ConfirmPasswordCantBeEmpty: String = "Confirm Password field cant be empty",
    var PasswordNotMatch: String = "Passwords dont match",
    var NewSignInRequired: String = " Please Sign in again"


)

data class messages
    (
    val ConfirmedLogin: String = "user details verified!",
    val DeleteConfirmation:String = "Account has been deleted"


)
