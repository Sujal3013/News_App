package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.repository.newsrepository

class newsvieewmodelprviderfactory(
    val newsrepository: newsrepository
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return Newsviewmodel(newsrepository) as T
    }
}