package com.example.newsapp.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.ui.Newsviewmodel


class ArticleFragment : Fragment(R.layout.fragment_article) {

    var viewModel:Newsviewmodel?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as MainActivity).viewModel
    }
}