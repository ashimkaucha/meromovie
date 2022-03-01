package com.example.meromoviewearos.response

import com.example.meromovie.entity.Movie

data class MovieResponse(
    val success: Boolean? = null,
    val data:ArrayList<Movie>? =null
)
//
//class MovieReview(
//    val success: Boolean? = null,
//    val data: Movie? = null
//    )