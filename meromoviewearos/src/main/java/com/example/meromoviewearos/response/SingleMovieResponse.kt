package com.example.meromoviewearos.response

import com.example.meromovie.entity.Movie

data class SingleMovieResponse (
    val success : Boolean? = null,
    val data : Movie? =null

)