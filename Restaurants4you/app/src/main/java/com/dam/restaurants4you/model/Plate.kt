package com.dam.restaurants4you.model

import com.google.gson.annotations.SerializedName

data class Plate(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val descripton: String,

    @SerializedName("restaurantFK")
    val restId: Int,

    @SerializedName("restaurant")
    val rest: Restaurant,
)
