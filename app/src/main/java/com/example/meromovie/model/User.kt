package com.example.meromovie.model

//Columns should be same as collection's name
data class User(
    var _Id: String = "",
    var firstname: String? = null,
    var lastname: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var password: String? = null
)


