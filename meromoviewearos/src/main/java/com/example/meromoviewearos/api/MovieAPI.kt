package com.example.meromoviewearos.api

import com.example.meromovie.entity.Movie
import com.example.meromovie.response.BookingResponse
import com.example.meromovie.response.MovieResponse
import retrofit2.Response
import retrofit2.http.*

interface MovieAPI {
    @GET("movies")
    suspend fun getAllMovieAPI(
        @Header("Authorization") token: String
    ): Response<MovieResponse>



    @POST("showtime/book/{showtimeId}/{seat}")
    suspend fun booking(
        @Header("Authorization") token: String,
        @Path("showtimeId") showtimeId: String,
        @Path("seat") seat: String
    ): Response<BookingResponse>

}

