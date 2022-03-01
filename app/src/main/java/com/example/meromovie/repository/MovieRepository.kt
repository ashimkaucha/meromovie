package com.example.meromovie.repository

import com.example.meromovie.api.MovieAPI
import com.example.meromovie.api.MyApiRequest
import com.example.meromovie.api.ServiceBuilder
import com.example.meromovie.response.BookingResponse
import com.example.meromovie.response.MovieResponse

class MovieRepository() : MyApiRequest() {
    private val movieApi = ServiceBuilder.buildService(MovieAPI::class.java)
//    private val movieDao = movieDB.getInstance(context).getMovieDao()

    suspend fun getAllMovieWithAPI(): MovieResponse {
        return apiRequest {
            movieApi.getAllMovieAPI(ServiceBuilder.token!!)
        }
    }


    suspend fun booking(showtimeId: String, seat : String): BookingResponse {
        return apiRequest {
            movieApi.booking(ServiceBuilder.token!!, showtimeId, seat)
        }
    }


}



