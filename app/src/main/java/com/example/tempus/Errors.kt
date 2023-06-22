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
    var NewSignInRequired: String = " Please Sign in again",
    var NotYourUsername:String = "no username detected , please enter a valid username",
    var EmptyUserName:String = "Username Field is empty!",
    var EmptyCat:String = "Category Field is empty!",
    val NoDetailsEntered:String = "Please Enter your Details",
    val EmptyTaskName:String = " Please enter a Task name",
    val EmptyDesc:String = " Please enter a description",
    val StartTimeNotChosen:String = "Please enter a Start time",
    val EndTimeNotChosen:String = "Please enter a Start time",
    val NoStartDate:String = "Please Enter A Start date",
    val NoEndDate:String = "Please Enter A Start date",
    val NoMinGoal:String = " Please enter a Minimum Goal",
    val NoMaxGoal:String = "Please enter a Max Goal"


)

data class messages
    (
    val ConfirmedLogin: String = "user details verified!",
    val DeleteConfirmation:String = "Account has been deleted"


)
