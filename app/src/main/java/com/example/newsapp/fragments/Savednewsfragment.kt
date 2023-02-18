package com.example.newsapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.ui.Newsviewmodel

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {

    var viewModel: Newsviewmodel?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as MainActivity).viewModel
    }
}