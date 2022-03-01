package com.example.meromoviewearos.repository

import com.example.meromovie.api.MyApiRequest
import com.example.meromovie.api.ServiceBuilder
import com.example.meromovie.api.UserAPI
import com.example.meromovie.entity.User
import com.example.meromovie.response.UserResponse

import okhttp3.MultipartBody

class UserRepository : MyApiRequest() {

    private val userApi =
        ServiceBuilder.buildService(UserAPI::class.java)

    //Register user
    suspend fun registerUser(user: User): UserResponse {
        return apiRequest {
            userApi.registerUser(user)
        }
    }

    // Login user
    suspend fun login(email: String, password: String): UserResponse {
        return apiRequest {
            userApi.login(email, password)
        }
    }

    suspend fun getuser(id:String): UserResponse {
        return apiRequest {
            userApi.getuser(ServiceBuilder.token!!, id)
        }
    }

    suspend fun updateuser(data: User): UserResponse {
        return apiRequest {
            userApi.updateuser(ServiceBuilder.token!!, data)
        }
    }

    suspend fun updateimage(body:MultipartBody.Part): UserResponse {
        return apiRequest {
            userApi.updateimage(ServiceBuilder.token!!,  body)
        }
    }

    suspend fun deleteuser(id:String): UserResponse {
        return apiRequest {
            userApi.deleteuser(ServiceBuilder.token!!, id)
        }
    }

}