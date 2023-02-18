package com.example.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.modals.NewsResponse
import com.example.newsapp.repository.newsrepository
import com.example.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class Newsviewmodel(val newsRepository:newsrepository):ViewModel() {
    val breakingnews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage=1

    init{
        getBreakingNews("in")
    }

    fun getBreakingNews(countryCode:String)=viewModelScope.launch {
        breakingnews.postValue(Resource.Loading())
        val response=newsRepository.getBreakingNews(countryCode,breakingNewsPage)
        breakingnews.postValue(handleBreakingNewsResponse(response))
    }
    private fun handleBreakingNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let{
                resultresponse-> return Resource.Success(resultresponse)
            }
        }
        return Resource.Error(response.message())
    }
}