package com.example.efisherytaskapplication.response

import com.google.gson.annotations.SerializedName

data class CommodityRequest(
    @SerializedName("komoditas")
    val commodity: String,
    @SerializedName("area_provinsi")
    val province: String,
    @SerializedName("area_kota")
    val city: String,
    val size: String,
    val price: String
)
