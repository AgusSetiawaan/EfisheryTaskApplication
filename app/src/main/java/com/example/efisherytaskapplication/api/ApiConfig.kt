package com.example.efisherytaskapplication.api

import android.content.Context
import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.efisherytaskapplication.data.CommodityDataDao
import com.example.efisherytaskapplication.data.CommodityDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiConfig {

    @Singleton
    @Provides
    fun provideApiService(interceptor: ChuckerInterceptor): ApiService{

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://stein.efishery.com/v1/storages/5e1edf521073e315924ceab4/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor{
        return ChuckerInterceptor.Builder(context)
            .collector(ChuckerCollector(context, true))
            .alwaysReadResponseBody(true)
            .build()
    }

    @Singleton
    @Provides
    fun provideCommodityDatabase(@ApplicationContext context: Context): CommodityDatabase{
        return Room.databaseBuilder(
            context,
            CommodityDatabase::class.java, "CommodityDao.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideCommodityDataDao(database: CommodityDatabase): CommodityDataDao{
        return database.commodityDataDao()
    }

}