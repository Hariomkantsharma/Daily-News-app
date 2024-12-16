package com.haridroid.newsbtp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.semantics.text
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haridroid.newsbtp.model.NewsItem
import com.haridroid.newsbtp.sentimentanalysis.SentimentAnalyzer

class NewsAdapter(private val context: Context, private val newsItems: List<NewsItem>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private var filteredNewsItems: List<NewsItem> = newsItems
    private var selectedSentiment: String? = null
    private val sentimentAnalyzer = SentimentAnalyzer(context)

    fun filterBySentiment(sentiment: String?) {
        selectedSentiment = sentiment
        filteredNewsItems = if (sentiment == null) {
            newsItems
        } else {
            newsItems.filter { newsItem ->
                sentimentAnalyzer.analyzeSentiment(newsItem.title ?: "") == sentiment
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_item_layout, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = filteredNewsItems[position]
        holder.titleTextView.text = newsItem.title
        holder.descriptionTextView.text = newsItem.description
        Glide.with(holder.itemView.context)
            .load(newsItem.urlToImage)
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, NewsDetailActivity::class.java)
            intent.putExtra("title", newsItem.title)
            intent.putExtra("content", newsItem.content)
            intent.putExtra("imageUrl", newsItem.urlToImage)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = filteredNewsItems.size

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titalTextNewsItem)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextNewsItem)
        val imageView: ImageView = itemView.findViewById(R.id.imgNewsItem)
    }
}