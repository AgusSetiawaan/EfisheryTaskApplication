package com.example.efisherytaskapplication.data

data class FilterAndSortModel(
    val sortType: SortType,
    val province: String,
    val city: String,
    val sizeStart: Int,
    val sizeEnd: Int,
    val priceStart: Int,
    val priceEnd: Int
)