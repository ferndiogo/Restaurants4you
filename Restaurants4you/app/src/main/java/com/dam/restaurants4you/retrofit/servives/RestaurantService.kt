package com.dam.restaurants4you.retrofit.servives

import com.dam.restaurants4you.model.Restaurant
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface RestaurantService {

    @GET("api/Restaurants")
    fun listRestaurants(
        @Header("Authorization") token:String
    ): Call<List<Restaurant>>



    @GET("api/Restaurants/{id}")
    fun listRestaurants(
        @Header("Authorization") token:String,
        @Path("id") id: Int
    ): Call<Restaurant>

    @Multipart
    @POST("api/Restaurants")
    fun addRestaurant(
        @Header("Authorization") token:String,
        @Part("name") name: RequestBody,
        @Part("description") descricao: RequestBody,
        @Part("localization") localizacao: RequestBody,
        @Part("contact") contacto: RequestBody,
        @Part("email") email: RequestBody,
        @Part("time") horario: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
    ): Call<Restaurant>

    @DELETE("api/Restaurants/{id}")
    fun deleteRestaurant(
        @Header("Authorization") token:String,
        @Path("id") id: Int
    ):Call<Void>

    @Multipart
    @PUT("api/Restaurants/{id}")
    fun editRestaurant(
        @Header("Authorization") token:String,
        @Part("rt") restaurant: Restaurant,
        @Part("id") id: Int
    ):Call<Void>

    @GET("api/Restaurants/User")
    fun listRestaurantsUser(
        @Header("Authorization") token:String
    ): Call<List<Restaurant>>

}