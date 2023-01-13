package com.dam.restaurants4you.model

import com.google.gson.annotations.SerializedName

data class ImageRest(
    @SerializedName("id")
    val id: Int,

    @SerializedName("path")
    val path: String,

    @SerializedName("restaurantFK")
    val restFK: Int,

    @SerializedName("restaurant")
    val rest: Restaurant,
)
