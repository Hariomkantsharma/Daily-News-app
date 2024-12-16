package com.haridroid.newsbtp.sentimentanalysis

import android.content.Context
import com.chaquo.python.Python
import okhttp3.internal.platform.AndroidPlatform

class SentimentAnalyzer(context: Context) {

    private val python: Python

    init {
        if (!Python.isStarted()) {
            Python.start(com.chaquo.python.android.AndroidPlatform(context))
        }
        python = Python.getInstance()
        python.getModule("nltk").callAttr("download", "vader_lexicon") // Download VADER lexicon
    }

    fun analyzeSentiment(text: String): String {
        val pyObject = python.getModule("sentiment_analysis") // Assuming you have a Python module named sentiment_analysis
        val result = pyObject.callAttr("analyze_sentiment", text)
        return result.toString()
    }
}