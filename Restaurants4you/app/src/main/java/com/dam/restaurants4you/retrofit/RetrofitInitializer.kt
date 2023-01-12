package com.dam.restaurants4you.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {
    private val host = "https://127.0.0.1/"

    private val gson: Gson = GsonBuilder().setLenient().create()

    private val retrofit =
        Retrofit.Builder()
            .baseUrl(host)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()



}