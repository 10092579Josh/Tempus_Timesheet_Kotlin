package com.example.opsc_part2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class registrationActivty : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration)
        input()

    }


    object SourceClass {
        val rows = 5
        val columns = 7

        val datas = Array(rows) { arrayOfNulls<String>(columns) }
    }

        fun input ()
        { //variables

            var back:ImageButton = findViewById(R.id.back_btn)

            var submitting: Button = findViewById(R.id.sub)
            var name: com.google.android.material.textfield.TextInputLayout = findViewById(R.id.firstName)
            var names = name.editText

            var surname: com.google.android.material.textfield.TextInputLayout =
                findViewById(R.id.surname)
            var surnames = surname.editText
            var username: com.google.android.material.textfield.TextInputLayout =
                findViewById(R.id.usernames)
            var user = username.editText
            var password: com.google.android.material.textfield.TextInputLayout =
                findViewById(R.id.enterPassword)
            var pass = password.editText
            var confirmpassword: com.google.android.material.textfield.TextInputLayout =
                findViewById(R.id.confirm_password)
            var pass2 = confirmpassword.editText
            var email: com.google.android.material.textfield.TextInputLayout = findViewById(R.id.email)
            var emails = email.editText
            back.setOnClickListener(){

                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish();

            }

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
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                    finish()

                    SourceClass.datas[0][0] = user?.text.toString().replace("\\s".toRegex(), "")
                    SourceClass.datas[0][1] = pass?.text.toString().replace("\\s".toRegex(), "")
                    SourceClass.datas[0][2] = names?.text.toString().replace("\\s".toRegex(), "")
                    SourceClass.datas[0][3] = surnames?.text.toString().replace("\\s".toRegex(), "")
                    SourceClass.datas[0][4] = emails?.text.toString().replace("\\s".toRegex(), "")
                    SourceClass.datas[0][5] = pass2?.text.toString().replace("\\s".toRegex(), "")

                    var message  = "USER ${user?.text} HAS REGISTERED "
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()




                }



            }
        }


    }

