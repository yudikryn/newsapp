package com.yudikryn.newsapp.data.remote.api

import com.yudikryn.newsapp.data.remote.model.ArticleResponse
import com.yudikryn.newsapp.data.remote.model.SourcesResponse
import com.yudikryn.newsapp.data.remote.network.ApiConfig.NEWS_API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines/sources")
    suspend fun getSources(
        @Query("category") category: String = "",
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = NEWS_API_KEY
    ): SourcesResponse

    @GET("top-headlines")
    suspend fun getArticle(
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20,
        @Query("category") category: String = "",
        @Query("q") keyword: String = "",
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = NEWS_API_KEY
    ): ArticleResponse

}