package com.yudikryn.newsapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.yudikryn.newsapp.data.remote.api.ApiService
import com.yudikryn.newsapp.data.remote.model.Article
import com.yudikryn.newsapp.data.remote.model.ArticleRequest
import com.yudikryn.newsapp.data.remote.model.Category
import com.yudikryn.newsapp.data.remote.model.Sources
import com.yudikryn.newsapp.data.remote.model.toCategory
import com.yudikryn.newsapp.data.remote.network.Result

class NewsRepository private constructor(
    private val apiService: ApiService,
) {

    fun getCategory(): LiveData<Result<List<Category>>> = liveData {
        emit(Result.Loading)
        try {
            val mCategory = MutableLiveData<List<Category>>()
            val category: LiveData<List<Category>> = mCategory

            mCategory.value = apiService.getSources().sources.map { it.toCategory() }.distinctBy { it.category }

            val result: LiveData<Result<List<Category>>> = category.map { Result.Success(it) }
            emitSource(result)
        } catch (e: Exception) {
            Log.d("NewsRepository", "getCategory: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getSourcesByCategory(category: String): LiveData<Result<List<Sources>>> = liveData {
        emit(Result.Loading)
        try {
            val mSources = MutableLiveData<List<Sources>>()
            val sources: LiveData<List<Sources>> = mSources

            mSources.value = apiService.getSources(category = category).sources

            val result: LiveData<Result<List<Sources>>> = sources.map { Result.Success(it) }

            emitSource(result)
        } catch (e: Exception) {
            Log.d("NewsRepository", "getSourcesByCategory: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getArticle(articleRequest: ArticleRequest): LiveData<Result<List<Article>>> = liveData {
        emit(Result.Loading)
        try {
            val mSources = MutableLiveData<List<Article>>()
            val sources: LiveData<List<Article>> = mSources

            mSources.value = apiService.getArticle(
                page = articleRequest.page,
                pageSize = articleRequest.pageSize,
                category = articleRequest.category,
                keyword = articleRequest.keyword
            ).articles

            val result: LiveData<Result<List<Article>>> = sources.map { Result.Success(it) }
            emitSource(result)
        } catch (e: Exception) {
            Log.d("NewsRepository", "getArticle: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: NewsRepository? = null
        fun getInstance(
            apiService: ApiService,
        ): NewsRepository =
            instance ?: synchronized(this) {
                instance ?: NewsRepository(apiService)
            }.also { instance = it }
    }
}