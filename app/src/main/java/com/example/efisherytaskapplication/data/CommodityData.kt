package com.example.efisherytaskapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "commodity")
data class CommodityData(
    @PrimaryKey
    val uuid: String,
    val commodity: String,
    val province: String,
    val city: String,
    val size: Int,
    val price: Int,
    val parsedDate: String,
    val timestamp: String
)