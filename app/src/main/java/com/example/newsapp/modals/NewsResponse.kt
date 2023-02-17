package com.example.newsapp.modals

import com.example.newsapp.modals.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)