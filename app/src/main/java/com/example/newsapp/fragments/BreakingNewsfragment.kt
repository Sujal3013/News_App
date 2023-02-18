package com.example.newsapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.adapter.Newsadapter
import com.example.newsapp.util.Resource
import com.example.ui.Newsviewmodel
import kotlinx.android.synthetic.main.fragment_breaking_newsfragment.*

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_newsfragment) {

    var viewModel: Newsviewmodel?=null
    lateinit var newsadapter: Newsadapter

    val TAG="Breaking News Fragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as MainActivity).viewModel
        setUpRecyclerView()

        newsadapter.setOnItemClickListener {
            val bundle=Bundle().apply{
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment2_to_articleFragment2,
                bundle
            )
        }

        viewModel?.breakingnews?.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resource.Success->{
                    hideProgressBar()
                    response.data?.let{
                        newsResponse -> newsadapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error->{
                    hideProgressBar()
                    response.message?.let{
                        message-> Log.e(TAG,"An error occured : $message")
                    }
                }
                is Resource.Loading->{
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility=View.INVISIBLE
    }
    private fun showProgressBar() {
        paginationProgressBar.visibility=View.VISIBLE
    }

    private fun setUpRecyclerView(){
        newsadapter= Newsadapter()
        rvBreakingNews.apply {
            adapter=newsadapter
            layoutManager=LinearLayoutManager(activity)
        }
    }
}