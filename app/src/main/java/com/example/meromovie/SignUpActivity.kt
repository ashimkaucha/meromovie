package com.example.meromovie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.meromovie.entity.User
import com.example.meromovie.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {
    private lateinit var etfname: EditText
    private lateinit var etlname : EditText
    private lateinit var etemail: EditText
    private lateinit var etpassword: EditText
    private lateinit var etphone: EditText
    private lateinit var tvsignUp: Button
    private lateinit var etlogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        etfname = findViewById(R.id.etfname)
        etlname = findViewById(R.id.etlname)
        etemail = findViewById(R.id.etemail)
        etpassword = findViewById(R.id.etpassword)
        etphone = findViewById(R.id.etphone)
        tvsignUp = findViewById(R.id.tvsignUp)
        etlogin = findViewById(R.id.etlogIn)

        etlogin.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LogInActivity::class.java))
        }

        tvsignUp.setOnClickListener{
            registerUser()
        }

    }

    private fun registerUser(){
        val fname = etfname.text.toString()
        val lname = etlname.text.toString()
        val email = etemail.text.toString()
        val pass = etpassword.text.toString()
        val phone = etphone.text.toString()

        val user = User(fname = fname,lname= lname, email = email, password = pass,phoneNumber = phone)

        Log.d("usreta", user.toString())

        //Coroutines -> IO, MAIN

        CoroutineScope(Dispatchers.IO).launch {
//            UserDB.getInstance(this@SignUpActivity)
//                .getUserDao().registerUser(user)
//
//            //Switch to main thread
//            withContext(Dispatchers.Main) {
//                Toast.makeText(
//                    this@SignUpActivity,
//                    "UserAdded", Toast.LENGTH_SHORT
//                ).show()
//            }

            try {
                val repository = UserRepository()
                val response = repository.registerUser(user)
                if(response.success == true){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@SignUpActivity, "Successfully registered", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SignUpActivity, LogInActivity::class.java))
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("errorrrr", ex.printStackTrace().toString())
                    Toast.makeText(this@SignUpActivity
                        , ex.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}
