package com.example.newsapp.modals

import com.example.newsapp.modals.Article

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)