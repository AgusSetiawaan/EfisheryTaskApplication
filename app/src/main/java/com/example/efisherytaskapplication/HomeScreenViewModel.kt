package com.example.efisherytaskapplication

import android.util.Log
import androidx.lifecycle.*
import androidx.room.Query
import com.example.efisherytaskapplication.data.CommodityData
import com.example.efisherytaskapplication.data.FilterAndSortModel
import com.example.efisherytaskapplication.data.HomeRepository
import com.example.efisherytaskapplication.response.OptionArea
import com.example.efisherytaskapplication.response.OptionSize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val homeRepository: HomeRepository): ViewModel() {

    private var _commodityListLiveData = MutableLiveData<Result<List<CommodityData>>>()
    val commodityListLiveData: LiveData<Result<List<CommodityData>>>
    get() = _commodityListLiveData

    private val _optionAreaListLiveData = MutableLiveData<List<OptionArea>>()
    val optionAreaListLiveData: LiveData<List<OptionArea>>
        get() = _optionAreaListLiveData

    private val _optionSizeListLiveData = MutableLiveData<List<OptionSize>>()
    val optionSizeListLiveData: LiveData<List<OptionSize>>
        get() = _optionSizeListLiveData

    fun getAllList(){
        _commodityListLiveData.value = Result.Loading
        viewModelScope.launch {
            var listCommodityData: Result<List<CommodityData>> = Result.Loading
            val listOptionArea = mutableListOf<OptionArea>()
            val listOptionSize = mutableListOf<OptionSize>()

            val getCommodityListAsync = async {
                try {
                    val response = homeRepository.getAllList()
                    val data: List<CommodityData> = response.filter { commodityResponse -> commodityResponse.uuid != null  }.map { commodity->
                        CommodityData(
                            commodity.uuid?:"",
                            commodity.commodity?:"",
                            commodity.province?:"",
                            commodity.city?:"",
                            commodity.size?.toInt()?:0,
                            commodity.price?.toInt()?:0,
                            commodity.parsedDate?:"",
                            commodity.timestamp?:""
                        )
                    }
                    homeRepository.deleteAll()
                    homeRepository.insertList(data)
                    listCommodityData = Result.Success(data)
                }
                catch (e: Exception){
                    Log.d("$TAG CommodityList", e.toString())
                    listCommodityData = Result.Error(e.toString())
                }
            }
            val getOptionAreaAsync = async {
                try {
                    val response = homeRepository.getOptionArea()
                    listOptionArea.addAll(response)
                }
                catch (e: Exception){
                    Log.d("$TAG getOptionAreaAsync", e.toString())
                }
            }
            val getOptionSizeAsync = async {
                try {
                    val response = homeRepository.getOptionSize()
                    listOptionSize.addAll(response)
                }
                catch (e: Exception){
                    Log.d("$TAG getOptionSizeAsync", e.toString())
                }
            }
            awaitAll(getCommodityListAsync, getOptionAreaAsync, getOptionSizeAsync)

            _commodityListLiveData.postValue(listCommodityData)
            _optionAreaListLiveData.postValue(listOptionArea)
            _optionSizeListLiveData.postValue(listOptionSize)
        }
    }

    fun getAllListFromDatabase(): LiveData<List<CommodityData>> = homeRepository.getListFromDatabase()

    fun getSearchCommodity(query: String): LiveData<List<CommodityData>> = homeRepository.getSearchCommodity(query = query)

    fun getFilteredCommodity(data: FilterAndSortModel): LiveData<List<CommodityData>> = homeRepository.getFilteredCommodity(data)

    companion object{
        private const val TAG = "HomeScreenViewModel"
    }
}