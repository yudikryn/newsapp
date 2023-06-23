package com.yudikryn.newsapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class SourcesResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("sources")
    val sources: List<Sources>,
)