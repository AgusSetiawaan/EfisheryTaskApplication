package com.example.efisherytaskapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.efisherytaskapplication.data.HomeRepository
import com.example.efisherytaskapplication.response.CommodityRequest
import com.example.efisherytaskapplication.response.OptionArea
import com.example.efisherytaskapplication.response.OptionSize
import com.example.efisherytaskapplication.response.PostDataResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommodityInputScreenViewModel@Inject constructor(private val homeRepository: HomeRepository): ViewModel() {

    private val _optionAreaListLiveData = MutableLiveData<List<OptionArea>>()
    val optionAreaListLiveData: LiveData<List<OptionArea>>
        get() = _optionAreaListLiveData

    private val _optionSizeListLiveData = MutableLiveData<List<OptionSize>>()
    val optionSizeListLiveData: LiveData<List<OptionSize>>
        get() = _optionSizeListLiveData

    private val _postDataLiveData = MutableLiveData<Result<PostDataResponse>>()
    val postDataLiveData: LiveData<Result<PostDataResponse>>
        get() = _postDataLiveData

    fun getAllList(){
        viewModelScope.launch {
            val listOptionArea = mutableListOf<OptionArea>()
            val listOptionSize = mutableListOf<OptionSize>()

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

            awaitAll(getOptionAreaAsync, getOptionSizeAsync)
            _optionAreaListLiveData.postValue(listOptionArea)
            _optionSizeListLiveData.postValue(listOptionSize)
        }
    }

    fun postData(request: CommodityRequest){
        _postDataLiveData.value = Result.Loading
        viewModelScope.launch {
            try {
                val response = homeRepository.postData(request)
                _postDataLiveData.postValue(Result.Success(response))
            }
            catch (e: Exception){
                _postDataLiveData.postValue(Result.Error(e.toString()))
            }
        }

    }

    companion object{
        private const val TAG = "CommodityInputScreenVie"
    }
}