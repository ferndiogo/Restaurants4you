package com.dam.restaurants4you.retrofit.servives

import com.dam.restaurants4you.model.User
import com.dam.restaurants4you.model.UserDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
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
    fun login(@Part("user")Userdto: UserDTO): Call<String>

}