package com.dam.restaurants4you.model

import android.provider.ContactsContract.CommonDataKinds.Email
import com.google.gson.annotations.SerializedName

data class Restaurant(
    @SerializedName("id")
    val id: Int?,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description:String,

    @SerializedName("localization")
    val localization: String,

    @SerializedName("contact")
    val contact: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("time")
    val Time: String,

    @SerializedName("latitude")
    val latitude: String,

    @SerializedName("longitude")
    val longitude: String,

    @SerializedName("plates")
    val plates: Array<Plate>?,

    @SerializedName("images")
    val images: Array<ImageRest>?
)
