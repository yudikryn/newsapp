package com.yudikryn.newsapp.data.remote.model

data class ArticleRequest(
    val page: Int = 1,
    val pageSize: Int = 10,
    val category: String = "",
    val keyword: String = "",
)