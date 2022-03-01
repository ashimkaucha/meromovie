package com.example.meromovie.response

import com.example.meromovie.entity.User

data class UserResponse(
    val success: Boolean? = null,
    val accessToken: String? = null,
    val data: User? = null,
    val userid: String?= null
)