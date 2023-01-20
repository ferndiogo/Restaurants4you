package com.dam.restaurants4you.retrofit.servives

import com.dam.restaurants4you.model.ImageRest
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File

interface ImageService {
    @Multipart
    @GET("api/Images")
    fun listImages(
        @Header("Authorization") token:String
    ): Call<List<ImageRest>>

    @Multipart
    @GET("api/Images/{id}")
    fun listImages(
        @Header("Authorization") token:String,
        @Part("id") id: String
    ): Call<ImageRest>

    @Multipart
    @POST("api/Images")
    fun addImage(
        @Header("Authorization") token:String,
        @Part("RestaurantFK") restaurantFK: Int,
        @Part imagem: MultipartBody.Part
    ): Call<ImageRest>

    @Multipart
    @DELETE("api/Images/{id}")
    fun deleteImage(
        @Header("Authorization") token:String,
        @Part("id") id: Int
    ): Call<Void>

}