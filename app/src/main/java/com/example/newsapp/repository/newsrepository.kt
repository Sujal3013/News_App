package com.example.newsapp.repository

import com.example.newsapp.Database.Article_Database
import com.example.newsapp.api.Retrofit_instance
import com.example.newsapp.modals.Article

class newsrepository(
    val db:Article_Database
) {
    suspend fun getBreakingNews(countryCode:String,pageNumber:Int)=
        Retrofit_instance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun Searchnews(searchQuery:String,pagenumber:Int)=
        Retrofit_instance.api.searchforNews(searchQuery,pagenumber)

    suspend fun upsert(article:Article)=db.getArticleDao().upsert(article)

    fun getSavednews()=db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article:Article)=db.getArticleDao().deleteArticle(article)



}