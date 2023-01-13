package com.dam.restaurants4you.retrofit.servives

import com.dam.restaurants4you.model.User
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface UserService {

    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST("api/Auth/register")
    fun register(
        @Field("username") user: String,
        @Field("password") pass: String
    ): Call<User>


    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST("api/Auth/login")
    fun login(
        @Field("username") user: String,
        @Field("password") pass: String
    ): Call<String>


}