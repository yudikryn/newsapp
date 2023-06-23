package com.yudikryn.newsapp.di

import android.content.Context
import com.yudikryn.newsapp.data.NewsRepository
import com.yudikryn.newsapp.data.remote.network.ApiConfig

object Injection {
    fun provideRepository(context: Context): NewsRepository {
        val apiService = ApiConfig.getApiService()
        return NewsRepository.getInstance(apiService)
    }
}