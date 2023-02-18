package com.example.newsapp.fragments


import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.adapter.Newsadapter
import com.example.newsapp.util.Constants
import com.example.newsapp.util.Constants.Companion.DELAY
import com.example.newsapp.util.Resource
import com.example.ui.Newsviewmodel
import kotlinx.android.synthetic.main.fragment_breaking_newsfragment.*
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.android.synthetic.main.fragment_search_news.paginationProgressBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    var viewModel:Newsviewmodel?=null
    lateinit var newsadapter:Newsadapter
    val TAG="Search News"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as MainActivity).viewModel
        setUpRecyclerView()

        newsadapter.setOnItemClickListener {
            val bundle=Bundle().apply{
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment3_to_articleFragment2,
                bundle
            )
        }

        var job:Job?=null
        etSearch.addTextChangedListener{editable->
            job?.cancel()
            job= MainScope().launch{
                delay(DELAY)
                editable?.let{
                    if(editable.toString().isNotEmpty()){
                        viewModel?.searchNews(editable.toString())
                    }
                }
            }

        }



        viewModel?.searchnews?.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resource.Success->{
                    hideProgressBar()
                    response.data?.let{
                            newsResponse -> newsadapter.differ.submitList(newsResponse.articles.toList())
                            val totalPages=newsResponse.totalResults / Constants.QUERY_PAGE_SIZE +2
                        isLastPage=viewModel?.searchNewsPage==totalPages
                        if(isLastPage){
                            rvSearchNews.setPadding(0,0,0,0)
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
    var isLoading=false
    var isLastPage=false
    var isScrolling=false

    val scrollListener=object: RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager=recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemposition=layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount=layoutManager.childCount
            val totalItemcount=layoutManager.itemCount

            val isNotloadingandNotLastPage= !isLoading && !isLastPage
            val isAtLastItem= firstVisibleItemposition+visibleItemCount>=totalItemcount

            val isNotAtbeginning=firstVisibleItemposition>=0
            val isTotsalMorethanvisible=totalItemcount>= Constants.QUERY_PAGE_SIZE
            val shouldpaginat=isNotloadingandNotLastPage && isAtLastItem && isNotAtbeginning && isTotsalMorethanvisible &&
                    isScrolling
            if(shouldpaginat){
                viewModel?.searchNews(etSearch.text.toString())
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

    private fun hideProgressBar() {
        paginationProgressBar.visibility=View.INVISIBLE
        isLoading=false
    }
    private fun showProgressBar() {
        paginationProgressBar.visibility=View.VISIBLE
        isLoading=true
    }
    private fun setUpRecyclerView(){
        newsadapter= Newsadapter()
        rvSearchNews.apply {
            adapter=newsadapter
            layoutManager= LinearLayoutManager(activity)
            addOnScrollListener(this@SearchNewsFragment.scrollListener)
        }
    }
}