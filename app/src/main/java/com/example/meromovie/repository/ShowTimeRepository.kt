package com.example.meromovie.repository


import com.example.meromovie.api.MyApiRequest
import com.example.meromovie.api.ServiceBuilder
import com.example.meromovie.api.ShowTimeAPI
import com.example.meromovie.response.MovieResponse
import com.example.meromovie.response.ShowTimeResponse
import com.example.meromovie.response.SingleMovieResponse

class ShowTimeRepository() : MyApiRequest() {
    private val showTimeApi = ServiceBuilder.buildService(ShowTimeAPI::class.java)

    suspend fun showtime(movieId: String): ShowTimeResponse {
        return apiRequest {
            showTimeApi.showtime(ServiceBuilder.token!!, movieId)
        }
    }

    suspend fun getSingleMovie(movieId: String): SingleMovieResponse {
        return apiRequest {
            showTimeApi.getSingleMovie(ServiceBuilder.token!!, movieId)
        }
    }


}



