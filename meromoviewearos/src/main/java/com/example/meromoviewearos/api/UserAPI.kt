package com.example.meromoviewearos.api


import com.example.meromovie.entity.User
import com.example.meromovie.response.UserResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserAPI {
    // user api
    @POST("user/register")
    suspend fun registerUser(
        @Body user: User
    ): Response<UserResponse>

    //Invoke
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<UserResponse>

    @GET("user/{id}")
    suspend fun getuser(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<UserResponse>

    @PUT("user/update")
    suspend fun updateuser(
        @Header("Authorization") token: String,
        @Body data : User
    ): Response<UserResponse>
    @Multipart
    @PUT("user/update-profile")
    suspend fun updateimage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): Response<UserResponse>

    @DELETE("/profile/delete/{id}")
    suspend fun deleteuser(
        @Header("Authorization") token: String,
        @Path("id") id: String,

    ):Response<UserResponse>
}