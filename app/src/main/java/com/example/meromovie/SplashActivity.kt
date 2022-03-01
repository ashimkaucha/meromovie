package com.example.meromovie

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
//import com.example.meromovie.db.UserDB
//import com.example.meromovie.entity.User
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
//    var username: String?=""
//    var password: String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val intent =Intent(this@SplashActivity, LogInActivity::class.java)
            startActivity(intent)

        },3000)

//        startActivity(Intent(this@SplashActivity, LogInActivity::class.java))

//        checkUsernamePassword()
//        login()
    }

//    private fun checkUsernamePassword(){
//        val sharePref =getSharedPreferences("usernamePassword", MODE_PRIVATE)
//        username = sharePref.getString("username", "")
//        password = sharePref.getString("password", "")
//    }

//    private fun login(){
//        var user: User? =null
//        CoroutineScope(Dispatchers.IO).launch {
//            delay(1000)
//            user = UserDB.getInstance(this@SplashActivity)
//                .getUserDao().checkUser(username!!, password!!)
//            if(user==null){
//                startActivity(
//                    Intent(
//                        this@SplashActivity,
//                        LogInActivity::class.java
//                    )
//                )
//            }else{
//                //save username and password to share preference
//                startActivity(
//                    Intent(
//                        this@SplashActivity,
//                        MainActivity::class.java
//                    )
//                )
//            }
//        }
//    }
}