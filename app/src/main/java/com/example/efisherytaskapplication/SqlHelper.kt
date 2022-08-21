package com.example.efisherytaskapplication

import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.efisherytaskapplication.data.FilterAndSortModel
import com.example.efisherytaskapplication.data.SortType

object SqlHelper {

    fun getSortedQuery(data: FilterAndSortModel): SimpleSQLiteQuery{
        val simpleQuery = StringBuilder().append("SELECT * FROM commodity WHERE 1=1 ")
        if(data.province.isNotBlank()) simpleQuery.append("AND province = '${data.province}' ")
        if(data.city.isNotBlank()) simpleQuery.append("AND city = '${data.city}' ")
        if(data.sizeStart != -1) simpleQuery.append("AND size >= ${data.sizeStart} ")
        if(data.sizeEnd != -1) simpleQuery.append("AND size <= ${data.sizeEnd} ")
        if(data.priceStart != -1) simpleQuery.append("AND price >= ${data.priceStart} ")
        if(data.priceEnd != -1) simpleQuery.append("AND price <= ${data.priceStart} ")


        when(data.sortType){
            SortType.A_TO_Z -> {
                simpleQuery.append("ORDER BY commodity ASC")
            }
            SortType.Z_TO_A -> {
                simpleQuery.append("ORDER BY commodity DESC")
            }
            SortType.SIZE_ASC -> {
                simpleQuery.append("ORDER BY size ASC")
            }
            SortType.SIZE_DESC -> {
                simpleQuery.append("ORDER BY size DESC")
            }
            SortType.PRICE_ASC -> {
                simpleQuery.append("ORDER BY price ASC")
            }
            SortType.PRICE_DESC -> {
                simpleQuery.append("ORDER BY price DESC")
            }
            else -> {

            }
        }
        return SimpleSQLiteQuery(simpleQuery.toString())

    }
}