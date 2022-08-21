package com.example.efisherytaskapplication.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CommodityData::class], version = 1, exportSchema = false)
abstract class CommodityDatabase: RoomDatabase() {

    abstract fun commodityDataDao(): CommodityDataDao
}