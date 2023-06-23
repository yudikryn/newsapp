package com.yudikryn.newsapp.ui.article

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yudikryn.newsapp.R
import com.yudikryn.newsapp.data.remote.model.Category
import com.yudikryn.newsapp.databinding.ItemCategoryBinding
import java.util.Locale

class CategoryAdapter(private val onClick: (position: Int, category: Category) -> Unit) : ListAdapter<Category, CategoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    fun updateData(position: Int){
        val tempList = this.currentList
        tempList.forEach {
            it.selected = false
        }

        tempList[position].selected = true
        submitList(tempList.toMutableList())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(position, category, onClick)
    }

    class MyViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(position: Int, category: Category, onClick: (position : Int, category: Category) -> Unit) {
            binding.apply {
                if (category.selected){
                    cvCategory.setCardBackgroundColor(ContextCompat.getColor(root.context, R.color.colorPrimary))
                    tvCategory.setTextColor(ContextCompat.getColor(root.context, R.color.white))
                }else{
                    cvCategory.setCardBackgroundColor(ContextCompat.getColor(root.context, R.color.white))
                    tvCategory.setTextColor(ContextCompat.getColor(root.context, R.color.colorPrimary))
                }

                tvCategory.text = category.category.capitalize(Locale.getDefault())

                root.setOnClickListener {
                    onClick.invoke(position, category)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Category> =
            object : DiffUtil.ItemCallback<Category>() {
                override fun areItemsTheSame(oldUser: Category, newUser: Category): Boolean {
                    return oldUser.category == newUser.category
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: Category, newUser: Category): Boolean {
                    return oldUser == newUser
                }
            }
    }
}