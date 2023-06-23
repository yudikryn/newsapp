package com.yudikryn.newsapp.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexboxLayoutManager
import com.yudikryn.newsapp.data.remote.model.ArticleRequest
import com.yudikryn.newsapp.data.remote.network.Result
import com.yudikryn.newsapp.databinding.FragmentArticleBinding
import com.yudikryn.newsapp.helper.ViewModelFactory
import com.yudikryn.newsapp.ui.detail.DetailActivity
import com.yudikryn.newsapp.ui.main.MainViewModel

class ArticleFragment: Fragment() {
    private lateinit var binding: FragmentArticleBinding
    private lateinit var viewModel: MainViewModel

    private val categoryAdapter by lazy {
        CategoryAdapter{ pos, category ->
            viewModel.setCategory(pos)
            initArticle(articleRequest = ArticleRequest(category = category.category))
        }
    }

    private val articleAdapter by lazy {
        ArticleAdapter{ article ->
            startActivity(
                DetailActivity.newInstance(
                    requireContext(),
                    article.title,
                    article.url
                )
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        initViewModel()
        setupView()
        return binding.root
    }

    private fun initViewModel(){
        val factory = ViewModelFactory.getInstance(requireContext())
        val viewModels: MainViewModel by activityViewModels {
            factory
        }
        viewModel = viewModels
    }

    private fun setupView(){
        initCategory()
        initArticle(ArticleRequest())
    }

    private fun initCategory(){
        viewModel.getCategory().observe(viewLifecycleOwner) { result ->
            when(result){
                is Result.Success ->{
                    result.data.first().selected = true
                    categoryAdapter.submitList(result.data)
                    binding.rvCategory.apply {
                        layoutManager = FlexboxLayoutManager(context)
                        setHasFixedSize(true)
                        adapter = categoryAdapter
                    }
                }
                is Result.Loading -> {}
                is Result.Error -> {
                    Toast.makeText(this.requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.selectedCategory.observe(viewLifecycleOwner){
            if (it != null){
                categoryAdapter.updateData(it)
            }
        }
    }

    private fun initArticle(articleRequest: ArticleRequest){
        viewModel.getArticle(articleRequest).observe(viewLifecycleOwner) { result ->
            when(result){
                is Result.Success ->{
                    articleAdapter.submitList(result.data)
                    binding.rvArticle.apply {
                        layoutManager = LinearLayoutManager(context)
                        setHasFixedSize(true)
                        adapter = articleAdapter
                    }
                }
                is Result.Loading -> {}
                is Result.Error -> {
                    Toast.makeText(this.requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}