package com.dam.restaurants4you.model

import com.google.gson.annotations.SerializedName

data class ImageRest(
    @SerializedName("Id")
    val id: Int,

    @SerializedName("Path")
    val path: String,

    @SerializedName("RestaurantFK")
    val restFK: Int,

    @SerializedName("Restaurant")
    val rest: Restaurant,
)
