package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.modals.Article
import kotlinx.android.synthetic.main.item_artcile.view.*

class Newsadapter:RecyclerView.Adapter<Newsadapter.ArtcileViewHolder>() {

    inner class ArtcileViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    private val differCallback=object:DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url==newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem==newItem
        }
    }
    val differ=AsyncListDiffer(this,differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtcileViewHolder {
        return ArtcileViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_artcile,
            parent,false)
        )
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    override fun onBindViewHolder(holder: ArtcileViewHolder, position: Int) {
        val article =differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(ivArticleImage)
            tvTitle.text=article.title
            tvSource.text=article.source.name
            tvDescription.text=article.description
            tvPublishedAt.text=article.publishedAt
            setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    private var onItemClickListener:((Article)->Unit)? = null

    fun setOnItemClickListener(listener:(Article)->Unit){
        onItemClickListener=listener
    }

}