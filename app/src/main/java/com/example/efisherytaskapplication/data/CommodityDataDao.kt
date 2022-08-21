package com.example.efisherytaskapplication.data

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface CommodityDataDao {

    @Query("SELECT * FROM commodity ORDER BY uuid ASC")
    fun getCommodityList(): LiveData<List<CommodityData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCommodityData(commodityList: List<CommodityData>)

    @Insert
    suspend fun insertCommodityData(commodityData: CommodityData)

    @Update
    suspend fun updateCommodityData(commodityData: CommodityData)

    @Query("SELECT COUNT(uuid) FROM commodity")
    fun getRowCount(): LiveData<Int>

    @Query("SELECT * FROM commodity where commodity LIKE :query OR province LIKE :query OR city LIKE :query")
    fun getSearchCommodity(query: String): LiveData<List<CommodityData>>

    @RawQuery(observedEntities = [CommodityData::class])
    fun getFilteredCommodity(query: SupportSQLiteQuery): LiveData<List<CommodityData>>

    @Query("DELETE FROM commodity")
    suspend fun deleteAll()


}