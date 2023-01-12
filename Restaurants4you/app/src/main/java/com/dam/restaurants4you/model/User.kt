package com.dam.restaurants4you.model

import com.google.gson.annotations.SerializedName
import java.util.*

class User(
    @SerializedName("username")
    val username: String,

    @SerializedName("PasswordSalt")
    val passSalt: Array<Byte>,

    @SerializedName("PasswrodHash")
    val passHash:Array<Byte>,

    @SerializedName("TokenCreated")
    val tokenCreated: Date,

    @SerializedName("TokenExpires")
    val tokenExpire: Date,

    )