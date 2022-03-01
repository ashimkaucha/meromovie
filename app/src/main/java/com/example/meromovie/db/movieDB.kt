//package com.example.meromovie.db
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import com.example.meromovie.dao.MovieDAO
//import com.example.meromovie.model.MovieModel
//
//@Database(
//    entities =[(MovieModel::class)],
//    version = 1
//)
//abstract class movieDB : RoomDatabase(){
//    abstract fun getMovieDao(): MovieDAO
//    companion object{
//        @Volatile
//        private var instance: movieDB? =null
//        fun getInstance(context: Context):movieDB{
//            if (instance==null){
//                synchronized(movieDB::class){
//                    instance = createDatabase(context)
//                }
//            }
//            return instance!!
//        }
//        private fun createDatabase(context: Context)=
//            Room.databaseBuilder(
//                context.applicationContext,
//                movieDB::class.java,
//                "movieDB"
//            ).build()
//    }
//}