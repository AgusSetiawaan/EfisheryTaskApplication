package com.example.efisherytaskapplication.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.room.Query
import com.example.efisherytaskapplication.Result
import com.example.efisherytaskapplication.SqlHelper
import com.example.efisherytaskapplication.api.ApiService
import com.example.efisherytaskapplication.response.*
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apiService: ApiService, private val dao: CommodityDataDao){

    suspend fun getAllList(): List<CommodityResponse> = apiService.getAllList()

    suspend fun getOptionArea(): List<OptionArea> = apiService.getOptionArea()

    suspend fun getOptionSize(): List<OptionSize> = apiService.getOptionSize()

    suspend fun postData(request: CommodityRequest): PostDataResponse = apiService.postData(listOf(request))

    fun getListFromDatabase(): LiveData<List<CommodityData>> = dao.getCommodityList()

    suspend fun insertList(list: List<CommodityData>) = dao.insertAllCommodityData(list)

    suspend fun deleteAll() = dao.deleteAll()

    fun getSearchCommodity(query: String) = dao.getSearchCommodity(query)

    fun getFilteredCommodity(data: FilterAndSortModel): LiveData<List<CommodityData>>{
        val query = SqlHelper.getSortedQuery(data)
        return dao.getFilteredCommodity(query)
    }

    fun getRowCount() = dao.getRowCount()



}