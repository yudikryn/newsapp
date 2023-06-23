package com.yudikryn.newsapp.ui.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.yudikryn.newsapp.databinding.ActivityDetailBinding

class DetailActivity: AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent?.let {
            setupToolbar(it.getStringExtra(EX_TITLE).orEmpty())
            setupWebView()
            binding.wvDetail.loadUrl(
                it.getStringExtra(EX_URL).orEmpty()
            )
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        binding.wvDetail.let {
            it.settings.loadsImagesAutomatically = true
            it.settings.javaScriptEnabled = true
            it.settings.useWideViewPort = true
            it.settings.loadWithOverviewMode = true
            it.webViewClient = WebViewController()
        }
    }

    inner class WebViewController : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            view?.loadUrl(request?.url.toString())
            return true
        }
    }

    private fun setupToolbar(toolbarTitle: String) {
        setSupportActionBar(binding.tbDetail)
        supportActionBar?.apply {
            title = toolbarTitle
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val EX_TITLE = "ex-title"
        private const val EX_URL = "ex-url"

        fun newInstance(ctx: Context, title: String, url: String) =
            Intent(ctx, DetailActivity::class.java).apply {
                putExtra(EX_TITLE, title)
                putExtra(EX_URL, url)
            }
    }
}