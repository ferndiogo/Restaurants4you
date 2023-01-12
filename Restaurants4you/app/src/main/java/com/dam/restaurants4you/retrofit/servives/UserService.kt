package com.dam.restaurants4you.retrofit.servives

import com.dam.restaurants4you.model.User
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface UserService {

    @Multipart
    @POST("api/Auth/register")
    fun register(@Part("username") user: RequestBody?,
                 @Part("password") pass: RequestBody?): Call<User>

    @Multipart
    @POST("api/Auth/login")
    fun login(@Part("username") user: RequestBody?,
              @Part("password") pass: RequestBody?): Call<String>

}