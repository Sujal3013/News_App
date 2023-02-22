package com.example.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.NewsApplication
import com.example.newsapp.adapter.Newsadapter
import com.example.newsapp.modals.Article
import com.example.newsapp.modals.NewsResponse
import com.example.newsapp.repository.newsrepository
import com.example.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class Newsviewmodel(
    app:Application,
    val newsRepository:newsrepository): AndroidViewModel(app) {
    val breakingnews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage=1
    var breakingnewsResponse:NewsResponse?=null

    val searchnews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage=1
    var searchnewsResponse:NewsResponse?=null

    init{
        getBreakingNews("in")
    }

    fun getBreakingNews(countryCode:String)=viewModelScope.launch {
        safeBreak(countryCode)
    }

    fun searchNews(searchQuery:String)=viewModelScope.launch{
        safeSearchNewsCall(searchQuery)
    }
    private fun handleBreakingNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let{
                resultresponse->
                breakingNewsPage++
                if (breakingnewsResponse==null){
                    breakingnewsResponse=resultresponse
                }else{
                    val oldArticles=breakingnewsResponse?.articles
                    val newArticle=resultresponse.articles
                    if (oldArticles != null) {
                        oldArticles.addAll(newArticle)
                    }
                    return Resource.Success(breakingnewsResponse?:resultresponse)
                }
                return Resource.Success(resultresponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let{
                    resultresponse->
                searchNewsPage++
                if (searchnewsResponse==null){
                    searchnewsResponse=resultresponse
                }else{
                    val oldArticles=searchnewsResponse?.articles
                    val newArticle=resultresponse.articles
                    if (oldArticles != null) {
                        oldArticles.addAll(newArticle)
                    }
                    return Resource.Success(searchnewsResponse?:resultresponse)
                }
                return Resource.Success(resultresponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun savearticle(article: Article)=viewModelScope.launch {
        newsRepository.upsert(article)
    }
    fun getsavednews()=newsRepository.getSavednews()

    fun deletearticle(article: Article)=viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    private suspend fun safeBreak(countryCode: String){
        breakingnews.postValue(Resource.Loading())
        try{
            if(hasInternetConnection()){
                val response=newsRepository.getBreakingNews(countryCode,breakingNewsPage)
                breakingnews.postValue(handleBreakingNewsResponse(response))
            }
            else{
                breakingnews.postValue(Resource.Error("No Internet connection"))
            }
        } catch (t:Throwable){
            when(t){
                is IOException->breakingnews.postValue(Resource.Error("Network Failure"))
                else->breakingnews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeSearchNewsCall(SearchQuery: String){
        searchnews.postValue(Resource.Loading())
        try{
            if(hasInternetConnection()){
                val response=newsRepository.Searchnews(SearchQuery,searchNewsPage)
                searchnews.postValue(handleSearchNewsResponse(response))
            }
            else{
                searchnews.postValue(Resource.Error("No Internet connection"))
            }
        } catch (t:Throwable){
            when(t){
                is IOException->searchnews.postValue(Resource.Error("Network Failure"))
                else->searchnews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }


    private fun hasInternetConnection():Boolean {
        val connectivityManager=getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            val activeNetwork=connectivityManager.activeNetwork?: return false
            val capibilties=connectivityManager.getNetworkCapabilities(activeNetwork)?:return false
            return when{
                capibilties.hasTransport(TRANSPORT_WIFI)->true
                capibilties.hasTransport(TRANSPORT_CELLULAR)->true
                capibilties.hasTransport(TRANSPORT_ETHERNET)->true
                else->false
            }
        }
        else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI->true
                    TYPE_MOBILE->true
                    TYPE_ETHERNET->true
                    else->false

                }
            }
        }
        return false

    }
}