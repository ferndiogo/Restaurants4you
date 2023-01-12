package com.dam.restaurants4you.retrofit.servives

import com.dam.restaurants4you.model.User
import com.dam.restaurants4you.model.UserDTO
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @FormUrlEncoded
    @POST("api/Auth/register")
    fun register(@Field("Username") user: String,
                 @Field("Password") pass: String): Call<User>

    @FormUrlEncoded
    @POST("api/Auth/login")
    fun login(@Field("user")Userdto: UserDTO): Call<String>

}