package com.example.tempus

data class Errors(
    val LoginError: String = "INCORRECT EMAIL OR PASSWORD",
    val RegEmailError: String = "EMAIL ALREADY IN USE",
    val ValidationError: String = " please check credentials",
    val EmailValidationEmptyError: String = "Email cant be empty",
    val NoNullsPassWord: String = " PASASWORD CANT BE EMPTY",
    val IllegalCharacterHash: String = "# Cant be used in Emails",
    val PasswordCantBeEmpty: String = "Password field cant be empty",
    val ConfirmPasswordCantBeEmpty: String = "Confirm Password field cant be empty",
    val PasswordNotMatch: String = "Passwords dont match",
    val PasswordTooShort:String =" Password does not meet the required length",
    val ConfirmPasswordTooShort:String =" Confirm Password does not meet the required length",
    val NewSignInRequired: String = " Please Sign in again",
    val NotYourUsername: String = "no username detected , please enter a valid username",
    val EmptyUserName: String = "Username Field is empty!",
    val EmptyCat: String = "Category Field is empty!",
    val NoDetailsEntered: String = "Please Enter your Details",
    val EmptyTaskName: String = " Please enter a Task name",
    val EmptyDesc: String = " Please enter a description",
    val StartTimeNotChosen: String = "Please enter a Start time",
    val EndTimeNotChosen: String = "Please enter a Start time",
    val NoStartDate: String = "Please Enter A Start date",
    val NoEndDate: String = "Please Enter A Start date",
    val NoMinGoal: String = " Please enter a Minimum Goal",
    val NoMaxGoal: String = "Please enter a Max Goal",
    val NoFName:String = " no First Name Entered",
    val NoSName:String = "No Surname Entered"



)

data class messages
    (
    val ConfirmedLogin: String = "user details verified!",
    val DeleteConfirmation: String = "Account has been deleted"


)
