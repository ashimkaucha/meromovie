package com.example.meromovie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.meromovie.api.ServiceBuilder
import com.example.meromovie.repository.UserRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogInActivity : AppCompatActivity() {
    private lateinit var linearLayout: LinearLayout
    private lateinit var etemail: EditText
    private lateinit var etpassword: EditText
    private lateinit var tvforgetpassword: TextView
    private lateinit var btnlogin: Button
    private lateinit var tvsignup: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        linearLayout = findViewById(R.id.linearLayout)
        etemail = findViewById(R.id.etemail)
        etpassword = findViewById(R.id.etpassword)
        tvforgetpassword = findViewById(R.id.tvforgetpassword)
        btnlogin = findViewById(R.id.btnlogin)
        tvsignup = findViewById(R.id.signup)

        tvsignup.setOnClickListener {
            startActivity(Intent(this@LogInActivity, SignUpActivity::class.java))
        }
        btnlogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = etemail.text.toString()
        val password = etpassword.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
//            val user = UserDB.getInstance(this@LogInActivity)
//                .getUserDao().checkUser(email, password)
//
//            if (user == null) {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(this@LogInActivity, "Wrong Credential", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            } else {
//                saveUsernamePassword()
//                startActivity(
//                    Intent(
//                        this@LogInActivity,
//                        MainActivity::class.java
//                    )
//                )
//            }

            try {
                val repository = UserRepository()
                val response = repository.login(email, password)

                if (response.success == true) {
                    // Save token
                    ServiceBuilder.token = "Bearer ${response.accessToken}"
                    ServiceBuilder.userID = response.userid!!
                    //Save username and password in shared preferences
                    // saveUsernamePassword()
                    startActivity(
                        Intent(
                            this@LogInActivity, MainActivity::class.java
                        )
                    )
                    finish()
                } else {
                    withContext(Dispatchers.Main) {
                        val snack =
                            Snackbar.make(
                                linearLayout,
                                "Invalid credentials",
                                Snackbar.LENGTH_LONG
                            )
                        snack.setAction("OK", View.OnClickListener {
                            snack.dismiss()
                        })
                        snack.show()
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("Error", ex.toString())
                    Toast.makeText(
                        this@LogInActivity,
                        ex.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }

//    private fun saveUsernamePassword() {
//        val sharedPreferences = getSharedPreferences("usernamePassword", MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putString("username", etUsername.text.toString())
//        editor.putString("password", etpassword.text.toString())
//        editor.apply()
//    }
//
//    private suspend fun saveUserName() {
//        val dataStore = createDataStore(name = "username")
//        val dataStoreKey = stringPreferencesKey("username")
//        dataStore.edit {
//            it[dataStoreKey] = etUsername.text.toString()
//        }
//    }

}