package com.dam.restaurants4you.retrofit.servives

import com.dam.restaurants4you.model.ImageRest
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
        @Part("images") img: ImageRest,
        @Part("imagem") file: File
    ): Call<ImageRest>

    @Multipart
    @DELETE("api/Images/{id}")
    fun deleteImage(
        @Header("Authorization") token:String,
        @Part("id") id: Int
    ): Call<Void>

}