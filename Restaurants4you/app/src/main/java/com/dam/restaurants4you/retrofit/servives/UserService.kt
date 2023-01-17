package com.dam.restaurants4you.retrofit.servives

import com.dam.restaurants4you.model.User
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface UserService {

    @Multipart
    @POST("api/Auth/register")
    fun register(
        @Part("username") user: String,
        @Part("password") pass: String
    ): Call<User>


    @Multipart
    @POST("api/Auth/login")
    fun login(
        @Part("username") user: String,
        @Part("password") pass: String
    ): Call<String>

    @GET("api/Auth/Username")
    fun getUsername(
        @Header("Authorization") token: String
    ): Call<String>

    @GET("api/Auth/Roles")
    fun getRoles(
        @Header("Authorization") token: String
    ): Call<String>
}