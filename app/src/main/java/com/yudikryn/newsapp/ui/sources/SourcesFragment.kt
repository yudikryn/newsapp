package com.yudikryn.newsapp.ui.sources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.yudikryn.newsapp.data.remote.network.Result
import com.yudikryn.newsapp.databinding.FragmentSourcesBinding
import com.yudikryn.newsapp.helper.ViewModelFactory
import com.yudikryn.newsapp.ui.main.MainViewModel

class SourcesFragment: Fragment() {

    private lateinit var binding: FragmentSourcesBinding
    private lateinit var viewModel: MainViewModel

    private val soucesAdapter by lazy {
        SourcesAdapter{ }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSourcesBinding.inflate(inflater, container, false)
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
        initSources()
    }

    private fun initSources(){
        viewModel.getSourcesByCategory().observe(viewLifecycleOwner) { result ->
            when(result){
                is Result.Success ->{
                    soucesAdapter.submitList(result.data)
                    binding.rvSources.apply {
                        layoutManager = LinearLayoutManager(context)
                        setHasFixedSize(true)
                        adapter = soucesAdapter
                    }

                    if (result.data.isNotEmpty()){
                        soucesAdapter.submitList(result.data)
                        binding.rvSources.apply {
                            layoutManager = LinearLayoutManager(context)
                            setHasFixedSize(true)
                            adapter = soucesAdapter
                        }
                        binding.rvSources.visibility = View.VISIBLE
                        binding.tvEmptyData.visibility = View.GONE
                    }else{
                        binding.rvSources.visibility = View.GONE
                        binding.tvEmptyData.visibility = View.VISIBLE
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