package com.dam.restaurants4you.retrofit

import com.dam.restaurants4you.retrofit.servives.ImageService
import com.dam.restaurants4you.retrofit.servives.RestaurantService
import com.dam.restaurants4you.retrofit.servives.UserService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Incia o retrofit para permitir comunicações com a API
 */
class RetrofitInitializer {

    private val host = "https://restaurants4you-api.azurewebsites.net/"
    //private val host = "http://10.0.2.2:5194/"

    private val gson: Gson = GsonBuilder().setLenient().create()

    private val retrofit =
        Retrofit.Builder()
            .baseUrl(host)
            .addConverterFactory( GsonConverterFactory.create(gson))
            .build()

    fun userService(): UserService = retrofit.create(UserService::class.java)
    fun restaurantService():RestaurantService = retrofit.create(RestaurantService::class.java)
    fun imageService():ImageService = retrofit.create(ImageService::class.java)

}