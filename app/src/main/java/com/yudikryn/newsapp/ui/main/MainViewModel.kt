package com.yudikryn.newsapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yudikryn.newsapp.data.NewsRepository
import com.yudikryn.newsapp.data.remote.model.ArticleRequest

class MainViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private val mSelectedCategory = MutableLiveData<Int>()
    var selectedCategory: LiveData<Int> = mSelectedCategory

    fun setCategory(category: Int){
        mSelectedCategory.postValue(category)
    }

    fun getCategory() = newsRepository.getCategory()
    fun getArticle(articleRequest: ArticleRequest) = newsRepository.getArticle(articleRequest)
    fun getSourcesByCategory(category: String = "") = newsRepository.getSourcesByCategory(category)
}