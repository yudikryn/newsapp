package com.yudikryn.newsapp.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.yudikryn.newsapp.R
import com.yudikryn.newsapp.data.remote.model.ArticleRequest
import com.yudikryn.newsapp.data.remote.network.Result
import com.yudikryn.newsapp.databinding.FragmentArticleBinding
import com.yudikryn.newsapp.helper.ViewModelFactory
import com.yudikryn.newsapp.ui.detail.DetailActivity
import com.yudikryn.newsapp.ui.main.MainViewModel

class ArticleFragment: Fragment() {
    private lateinit var binding: FragmentArticleBinding
    private lateinit var viewModel: MainViewModel
    private var categoryName: String = ""
    private var currentPage = 1
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var isLoading = false
    private var isLastPage = false
    private val pageSize = 10

    private val categoryAdapter by lazy {
        CategoryAdapter{ pos, category ->
            currentPage = 1
            viewModel.setCategory(pos, category.category)
            initArticle(articleRequest = ArticleRequest(page = currentPage, category = category.category))
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        initViewModel()
        setupView()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.tbArticle.toolbar)
        (activity as AppCompatActivity).title = "News"
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)
        val searchItem = menu.findItem(R.id.menu_list_search)
        searchItem.actionView
        if (searchItem != null){
            val searchView = searchItem.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        initArticle(fromSearch = true, articleRequest = ArticleRequest(page = currentPage, category = categoryName, keyword = it))
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }

            })
        }
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_list_search-> {

            }
        }
        return super.onOptionsItemSelected(item)
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
        initScrollArticle()
    }

    private fun initScrollArticle(){
        binding.rvArticle.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = linearLayoutManager.childCount
                val totalItemCount = linearLayoutManager.itemCount
                val firstVisibleItemPosition =
                    linearLayoutManager.findFirstVisibleItemPosition()
                if (!isLoading && !isLastPage) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= pageSize
                    ) {
                        currentPage ++
                        isLoading = true
                        initArticle(
                            articleRequest = ArticleRequest(
                                page = currentPage,
                                category = categoryName
                            )
                        )
                    }
                }
            }
        })

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

        viewModel.nameCategory.observe(viewLifecycleOwner){
            if (it != null){
                categoryName = it
            }
        }
    }

    private fun initArticle(articleRequest: ArticleRequest, fromSearch: Boolean = false){
        viewModel.getArticle(articleRequest).observe(viewLifecycleOwner) { result ->
            when(result){
                is Result.Success ->{
                    isLoading = false
                    if (result.data.isNotEmpty()){
                        isLastPage = false
                        if (currentPage == 1){
                            articleAdapter.submitList(result.data)
                            linearLayoutManager = LinearLayoutManager(context)
                            binding.rvArticle.apply {
                                layoutManager = linearLayoutManager
                                setHasFixedSize(true)
                                adapter = articleAdapter
                            }
                        }else{
                            articleAdapter.updateData(result.data)
                        }
                        binding.tvEmptyData.visibility = View.GONE
                        binding.rvArticle.visibility = View.VISIBLE
                    }else{
                        if (fromSearch){
                            binding.tvEmptyData.visibility = View.VISIBLE
                            binding.rvArticle.visibility = View.GONE
                        }else{
                            isLastPage = true
                            if (articleAdapter.itemCount == 0){
                                binding.tvEmptyData.visibility = View.VISIBLE
                                binding.rvArticle.visibility = View.GONE
                            }
                        }
                    }
                }
                is Result.Loading -> {}
                is Result.Error -> {
                    isLoading = false
                    Toast.makeText(this.requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}