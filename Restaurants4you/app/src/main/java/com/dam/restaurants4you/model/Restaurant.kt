package com.dam.restaurants4you.model

import android.provider.ContactsContract.CommonDataKinds.Email
import com.google.gson.annotations.SerializedName

data class Restaurant(
    @SerializedName("id")
    val id: Int,

    @SerializedName("Name")
    val name: String,

    @SerializedName("Description")
    val description:String,

    @SerializedName("Localization")
    val localization: String,

    @SerializedName("Contact")
    val contact: String,

    @SerializedName("Email")
    val email: String,

    @SerializedName("Time")
    val Time: String,

    @SerializedName("Latitude")
    val latitude: Double,

    @SerializedName("Longitude")
    val longitude:Double,

    @SerializedName("Plates")
    val plates: Array<Plate>,

    @SerializedName("Images")
    val images: Array<ImageRest>
)
