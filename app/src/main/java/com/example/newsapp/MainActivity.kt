package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.Database.Article_Database
import com.example.newsapp.repository.newsrepository
import com.example.ui.Newsviewmodel
import com.example.ui.newsvieewmodelprviderfactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var viewModel: Newsviewmodel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository=newsrepository(Article_Database(this))
        val viewModelProviderFactory=newsvieewmodelprviderfactory(repository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory)[Newsviewmodel::class.java]

        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }
}