package com.yudikryn.newsapp.ui.sources

import android.annotation.SuppressLint
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yudikryn.newsapp.data.remote.model.Sources
import com.yudikryn.newsapp.databinding.ItemSourcesBinding

class SourcesAdapter(private val onClick: (sources: Sources) -> Unit) : ListAdapter<Sources, SourcesAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemSourcesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article, onClick)
    }

    class MyViewHolder(private val binding: ItemSourcesBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(sources: Sources, onClick: (sources: Sources) -> Unit) {
            binding.apply {
                tvAuthor.text = sources.name

                if (sources.description != null){
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        tvDesc.text = Html.fromHtml(sources.description, Html.FROM_HTML_MODE_LEGACY)
                    } else {
                        tvDesc.text = Html.fromHtml(sources.description)
                    }
                }

                root.setOnClickListener {
                    onClick.invoke(sources)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Sources> =
            object : DiffUtil.ItemCallback<Sources>() {
                override fun areItemsTheSame(oldUser: Sources, newUser: Sources): Boolean {
                    return oldUser.name == newUser.name
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: Sources, newUser: Sources): Boolean {
                    return oldUser == newUser
                }
            }
    }
}