package com.yudikryn.newsapp.data.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Sources(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("url")
    val url: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("language")
    val language: String,
    @SerializedName("country")
    val country: String,
) : Parcelable