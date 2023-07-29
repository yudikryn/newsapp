package com.yudikryn.newsapp.ui.article

import android.annotation.SuppressLint
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yudikryn.newsapp.R
import com.yudikryn.newsapp.data.remote.model.Article
import com.yudikryn.newsapp.databinding.ItemArticleBinding

class ArticleAdapter(private val onClick: (article: Article) -> Unit) : ListAdapter<Article, ArticleAdapter.MyViewHolder>(DIFF_CALLBACK) {

    fun updateData(data: List<Article>){
        val tempList : ArrayList<Article> = arrayListOf()
        if (currentList.isNotEmpty()){
            tempList.addAll(currentList)
        }
        if (data.isNotEmpty()){
            tempList.addAll(data)
        }
        submitList(tempList.toMutableList())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article, onClick)
    }

    class MyViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(article: Article, onClick: (article: Article) -> Unit) {
            binding.apply {

                tvTitle.text = article.title
                tvSources.text = article.source.name

                if (article.author != null){
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        tvAuthor.text = Html.fromHtml(article.author,Html.FROM_HTML_MODE_LEGACY)
                    } else {
                        tvAuthor.text = Html.fromHtml(article.author)
                    }
                }

                Glide.with(itemView.context)
                    .load(article.urlToImage)
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary).error(R.color.colorPrimary))
                    .into(binding.ivArticle)

                root.setOnClickListener {
                    onClick.invoke(article)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Article> =
            object : DiffUtil.ItemCallback<Article>() {
                override fun areItemsTheSame(oldUser: Article, newUser: Article): Boolean {
                    return oldUser.title == newUser.title
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: Article, newUser: Article): Boolean {
                    return oldUser == newUser
                }
            }
    }
}