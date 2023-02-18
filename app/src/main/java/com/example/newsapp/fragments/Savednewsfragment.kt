package com.example.newsapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.adapter.Newsadapter
import com.example.ui.Newsviewmodel
import kotlinx.android.synthetic.main.fragment_breaking_newsfragment.*
import kotlinx.android.synthetic.main.fragment_saved_news.*
import kotlinx.android.synthetic.main.fragment_search_news.*

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {


    var viewModel: Newsviewmodel?=null
    lateinit var newsadapter:Newsadapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as MainActivity).viewModel
        setUpRecyclerView()

        newsadapter.setOnItemClickListener {
            val bundle=Bundle().apply{
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_savedNewsFragment2_to_articleFragment2,
                bundle
            )
        }
    }
    private fun setUpRecyclerView(){
        newsadapter= Newsadapter()
        rvSavedNews.apply {
            adapter=newsadapter
            layoutManager= LinearLayoutManager(activity)
        }
    }
}