package com.haridroid.newsbtp
import com.haridroid.newsbtp.model.NewsItem
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haridroid.newsbtp.databinding.ActivityMainBinding
import com.haridroid.newsbtp.network.NewsResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerView = findViewById(R.id.recyclerViewMainActivity)
        recyclerView.layoutManager = LinearLayoutManager(this)
        progressBar = findViewById(R.id.progressBar)


        lifecycleScope.launch {
            progressBar.visibility = View.VISIBLE // Show progress bar
            // Log the API request
            val apiRequest = "https://newsapi.org/v2/top-headlines?country=us&apiKey=96762ea15f0d4c65993e90b2cbb0e004"
            Log.d("MainActivity", "API Request: $apiRequest")

            // Toast the API request (optional)
            Toast.makeText(this@MainActivity, "Making API request...", Toast.LENGTH_SHORT).show()

            val response = NewsResponse.RetrofitClient.newsApiService.getTopHeadlines("us", "96762ea15f0d4c65993e90b2cbb0e004")
            if (response.isSuccessful) {
                Toast.makeText(  this@MainActivity , "success!", Toast.LENGTH_SHORT).show()
                val newsItems = response.body()?.articles?.let { articles ->
                    val newsItemsList = mutableListOf<NewsItem>()
                    for (article in articles) {
                        newsItemsList.add(NewsItem(article.title, article.description, article.urlToImage, article.content)) // Add content
                    }
                    newsItemsList
                } ?: emptyList()


                delay(1000)
                progressBar.visibility = View.GONE // Hide progress bar
                newsAdapter = NewsAdapter(newsItems)
                recyclerView.adapter = newsAdapter
            } else {
                // Handle error
                val errorCode = response.code()
                val errorBody = response.errorBody()?.string() // Get error body as string

                // Log the error
                Log.e("MainActivity", "Error: $errorCode - $errorBody")

                // Handle different error codes
                when (errorCode) {
                    400 -> Toast.makeText(this@MainActivity, "Bad Request", Toast.LENGTH_SHORT).show()
                    401 -> Toast.makeText(this@MainActivity, "Unauthorized", Toast.LENGTH_SHORT).show()
                    // ... other error codes ...
                    else -> Toast.makeText(this@MainActivity, "Unknown error", Toast.LENGTH_SHORT).show()
                }


            }
        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_meny, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                // Refresh news data
                // ... (Call the API again to fetch fresh data) ...
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}