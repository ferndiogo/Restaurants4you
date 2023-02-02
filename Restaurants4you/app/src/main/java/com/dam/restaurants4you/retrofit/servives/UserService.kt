package com.dam.restaurants4you.retrofit.servives

import com.dam.restaurants4you.model.User
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/**
 * conjunto de funções para acesso a API
 */
interface UserService {

    @Multipart
    @POST("api/Auth/register")
    fun register(
        @Part("username") user: RequestBody,
        @Part("password") pass: RequestBody
    ): Call<User>


    @Multipart
    @POST("api/Auth/login")
    fun login(
        @Part("username") user: RequestBody,
        @Part("password") pass: RequestBody
    ): Call<String>

    @GET("api/Auth/Username")
    fun getUsername(
        @Header("Authorization") token: String
    ): Call<String>

    @GET("api/Auth/Roles")
    fun getRoles(
        @Header("Authorization") token: String
    ): Call<String>

    @GET
    fun getId(
        @Header("Authorization") token: String,
    ): Call<String>
}