package com.example.meromovie.model

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity
data class MovieModel (
//    @PrimaryKey
    var Id : String = "",
    var moviename : String?,
    var movierate : String?,
)