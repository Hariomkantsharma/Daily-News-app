package com.haridroid.newsbtp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.semantics.text
import com.bumptech.glide.Glide

class NewsDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content") // Get content
        val imageUrl = intent.getStringExtra("imageUrl")

        val titleTextView: TextView = findViewById(R.id.detailTitleTextView)
        val descriptionTextView: TextView = findViewById(R.id.detailDescriptionTextView)
        val imageView: ImageView = findViewById(R.id.detailImageView)

        titleTextView.text = title
        descriptionTextView.text = content // Display content in descriptionTextView
        Glide.with(this).load(imageUrl).into(imageView)
    }
}