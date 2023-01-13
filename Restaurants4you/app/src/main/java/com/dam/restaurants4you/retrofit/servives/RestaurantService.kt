package com.dam.restaurants4you.retrofit.servives

import com.dam.restaurants4you.model.Restaurant
import retrofit2.Call
import retrofit2.http.*

interface RestaurantService {
    @Multipart
    @GET("api/Restaurants")
    fun listRestaurants(
        @Header("Authorization") token:String
    ): Call<List<Restaurant>>

    @Multipart
    @GET("api/Restaurants/{id}")
    fun listRestaurants(
        @Header("Authorization") token:String,
        @Path("id") id: String
    ): Call<Restaurant>

    @Multipart
    @POST("api/Restaurants")
    fun addRestaurant(
        @Header("Authorization") token:String,
        @Part("rt") rt: Restaurant
    ): Call<Restaurant>

    @Multipart
    @DELETE("api/Restaurants/{id}")
    fun deleteRestaurant(
        @Header("Authorization") token:String,
        @Path("id") id: Int,
        @Part("id") idBody: Int
    ):Call<Void>

    @Multipart
    @PUT("api/Restaurants")
    fun editRestaurant(
        @Header("Authorization") token:String,
        @Part("rt") restaurant: Restaurant,
        @Path("id") id: Int
    ):Call<Void>



}