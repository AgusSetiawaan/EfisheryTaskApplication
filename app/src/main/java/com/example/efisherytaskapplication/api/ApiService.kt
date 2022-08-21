package com.example.efisherytaskapplication.api

import com.example.efisherytaskapplication.response.*
import retrofit2.http.*

interface ApiService {

    @GET("list")
    suspend fun getAllList(): List<CommodityResponse>

    @GET("option_area")
    suspend fun getOptionArea(): List<OptionArea>

    @GET("option_size")
    suspend fun getOptionSize(): List<OptionSize>

    @POST("list")
    suspend fun postData(@Body requestBody: List<CommodityRequest>): PostDataResponse

}