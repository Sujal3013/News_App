package com.example.newsapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.adapter.Newsadapter
import com.example.newsapp.util.Constants.Companion.QUERY_PAGE_SIZE
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
                        newsResponse -> newsadapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages=newsResponse.totalResults / QUERY_PAGE_SIZE +2
                        isLastPage=viewModel?.breakingNewsPage==totalPages
                        if(isLastPage){
                            rvBreakingNews.setPadding(0,0,0,0)
                        }
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
        isLoading=false
    }
    private fun showProgressBar() {
        paginationProgressBar.visibility=View.VISIBLE
        isLoading=true
    }

    var isLoading=false
    var isLastPage=false
    var isScrolling=false

    val scrollListener=object:RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager=recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemposition=layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount=layoutManager.childCount
            val totalItemcount=layoutManager.itemCount

            val isNotloadingandNotLastPage= !isLoading && !isLastPage
            val isAtLastItem= firstVisibleItemposition+visibleItemCount>=totalItemcount

            val isNotAtbeginning=firstVisibleItemposition>=0
            val isTotsalMorethanvisible=totalItemcount>=QUERY_PAGE_SIZE
            val shouldpaginat=isNotloadingandNotLastPage && isAtLastItem && isNotAtbeginning && isTotsalMorethanvisible &&
                    isScrolling
            if(shouldpaginat){
                viewModel?.getBreakingNews("in")
                isScrolling=false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling=true
            }
        }

    }

    private fun setUpRecyclerView(){
        newsadapter= Newsadapter()
        rvBreakingNews.apply {
            adapter=newsadapter
            layoutManager=LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }
}