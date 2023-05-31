package com.example.opsc_part2
//login file is the registration page cause i needed that first
//luca forget to add a go back to sign in if someone accidentally clicked sign up , please fix advise?
//opengl error but doesn't seem to affect anything
//forgot password can only be complete when database is done as it requires updating the database
// need a screen for forgot password as it will require username to find the user on the database, new password confirm password
// alternative would be to grab password from login screen but runs the risk of repeated data and mistaken pressing
// no delete user ? please advise
// no remember me ? please advise
// sign up colour needs to be changed due to can not see if it works
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.sql.*

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)
   input()


    }
    object SourceClass {
        val datas = arrayOfNulls<String>(10)
    }


    // SourceClass.kt

    // this method will grab and check data from the input
    // user will not be allowed to move on till fields are filled
    // error has not been fixed but workaround has been implemented
    //stuff before the listener is variables stuff after is action code
    // keep toast?
    //intent moves between screens


    // Usage: Call this function with the values retrieved from your GUI components

fun input ()
{ //variables

    var submitting: Button = findViewById(R.id.sub)
    var name: com.google.android.material.textfield.TextInputLayout = findViewById(R.id.name)
    var names = name.editText

    var surname: com.google.android.material.textfield.TextInputLayout =
        findViewById(R.id.surname)
    var surnames = surname.editText
    var username: com.google.android.material.textfield.TextInputLayout =
        findViewById(R.id.username)
    var user = username.editText
    var password: com.google.android.material.textfield.TextInputLayout =
        findViewById(R.id.enterPassword)
    var pass = password.editText
    var confirmpassword: com.google.android.material.textfield.TextInputLayout =
        findViewById(R.id.confirmPassword)
    var pass2 = confirmpassword.editText
    var email: com.google.android.material.textfield.TextInputLayout = findViewById(R.id.email)
    var emails = email.editText


    submitting.setOnClickListener()

    {

  //action statements tp check fields if empty
        if (pass?.text.toString() != pass2?.text.toString())
        { var message  = " passwords do not match"
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

        }
        else if(names?.text.toString().isEmpty())
        {
            var message  = " no name entered "
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
        else if(surnames?.text.toString().isEmpty())
        {
            var message  = " no surname entered "
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
        else if(user?.text.toString().isEmpty())
        {
            var message  = " no username entered "
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
        else if(pass?.text.toString().length <7)
        {
            var message  = " enter password is too short"
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
        else if(pass2?.text.toString().length <7)
        { var message  = " confirm password is too short "
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

        }

        else if(emails?.text.toString().isEmpty())
        {
            var message  = " no email entered "
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
        else
        { // move to the next screen if filled
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            SourceClass.datas[0] = user?.text.toString()
            SourceClass.datas[1] = pass?.text.toString()
            SourceClass.datas[2] = names?.text.toString()
            SourceClass.datas[3] = surnames?.text.toString()
            SourceClass.datas[4] = emails?.text.toString()



        }



    }
}
    // to do database code below
fun databasewriter()
{
    var submitting: Button = findViewById(R.id.sub)
    var name: com.google.android.material.textfield.TextInputLayout = findViewById(R.id.name)
    var names = name.editText

    var surname: com.google.android.material.textfield.TextInputLayout =
        findViewById(R.id.surname)
    var surnames = surname.editText
    var username: com.google.android.material.textfield.TextInputLayout =
        findViewById(R.id.username)
    var user = username.editText
    var password: com.google.android.material.textfield.TextInputLayout =
        findViewById(R.id.enterPassword)
    var pass = password.editText
    var confirmpassword: com.google.android.material.textfield.TextInputLayout =
        findViewById(R.id.confirmPassword)
    var pass2 = confirmpassword.editText
    var email: com.google.android.material.textfield.TextInputLayout = findViewById(R.id.email)
    var emails = email.editText
    //josh here goes the database code use above variables to write to the database

}
}