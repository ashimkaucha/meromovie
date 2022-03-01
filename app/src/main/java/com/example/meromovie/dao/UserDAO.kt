//package com.example.meromovie.dao
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.Query
//import com.example.meromovie.entity.User
//
//@Dao
//interface UserDAO {
//    @Insert
//    fun registerUser(user: User)
//
//    @Query("select * from User where uname=(:username) and password=(:password)")
//    fun checkUser(username: String, password: String): User
//}