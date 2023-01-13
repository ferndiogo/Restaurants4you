package com.dam.restaurants4you.model

import com.google.gson.annotations.SerializedName

data class Plate(
    @SerializedName("Id")
    val id: Int,

    @SerializedName("Name")
    val name: String,

    @SerializedName("Description")
    val descripton: String,

    @SerializedName("RestaurantFK")
    val restId: Int,

    @SerializedName("Restaurant")
    val rest: Restaurant,
)
