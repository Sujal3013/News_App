package com.example.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.repository.newsrepository

class newsvieewmodelprviderfactory(
    val app:Application,
    val newsrepository: newsrepository
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return Newsviewmodel(app,newsrepository) as T
    }
}