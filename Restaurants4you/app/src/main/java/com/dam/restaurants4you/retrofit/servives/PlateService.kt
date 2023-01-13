package com.dam.restaurants4you.retrofit.servives

import com.dam.restaurants4you.model.Plate
import retrofit2.Call
import retrofit2.http.*

interface PlateService {
    @Multipart
    @GET("api/Plates")
    fun listPlates(
        @Header("Authorization") token:String
    ): Call<List<Plate>>

    @Multipart
    @GET("api/Plates/{id}")
    fun listPlates(
        @Header("Authorization") token:String,
        @Part("id") id: String
    ): Call<Plate>

    @Multipart
    @POST("api/Plates")
    fun addPlate(
        @Header("Authorization") token:String,
        @Part("rt") plate: Plate
    ): Call<Plate>

    @Multipart
    @DELETE("api/Plates/{id}")
    fun deletePlate(
        @Header("Authorization") token:String,
        @Part("id") idBody: Int
    ): Call<Void>

    @Multipart
    @PUT("api/Plates/{id}")
    fun editPlate(
        @Header("Authorization") token:String,
        @Part("plate") plate: Plate,
        @Part("id") idBody: Int
    ): Call<Void>
}