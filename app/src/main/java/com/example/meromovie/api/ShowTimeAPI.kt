package com.example.meromovie.api


import com.example.meromovie.response.BookingResponse
import com.example.meromovie.response.MovieResponse
import com.example.meromovie.response.ShowTimeResponse
import com.example.meromovie.response.SingleMovieResponse
import retrofit2.Response
import retrofit2.http.*

interface ShowTimeAPI {
    @GET("movieShowtime/{movieId}")
    suspend fun showtime(
        @Header("Authorization")token:String,
        @Path("movieId") movieId:String
    ): Response<ShowTimeResponse>

    @GET("movie/single/{movieId}")
    suspend fun getSingleMovie(
        @Header("Authorization") token: String,
        @Path("movieId") movieId: String
    ): Response<SingleMovieResponse>


}

