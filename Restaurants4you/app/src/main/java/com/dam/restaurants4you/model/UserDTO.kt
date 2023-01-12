package com.dam.restaurants4you.model

import com.google.gson.annotations.SerializedName

data class UserDTO(
    @SerializedName("username")
    val username: String,

    @SerializedName("Password")
    val pass: String,
)
