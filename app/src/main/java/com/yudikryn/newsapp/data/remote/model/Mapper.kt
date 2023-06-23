package com.yudikryn.newsapp.data.remote.model

fun Sources.toCategory(): Category {
    return Category(
        category = category
    )
}