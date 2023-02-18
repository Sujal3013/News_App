package com.example.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.modals.Article
import com.example.newsapp.modals.NewsResponse
import com.example.newsapp.repository.newsrepository
import com.example.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class Newsviewmodel(val newsRepository:newsrepository):ViewModel() {
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
        breakingnews.postValue(Resource.Loading())
        val response=newsRepository.getBreakingNews(countryCode,breakingNewsPage)
        breakingnews.postValue(handleBreakingNewsResponse(response))
    }

    fun searchNews(searchQuery:String)=viewModelScope.launch{
        searchnews.postValue(Resource.Loading())
        val response=newsRepository.Searchnews(searchQuery,searchNewsPage)
        searchnews.postValue(handleSearchNewsResponse(response))
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
}