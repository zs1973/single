package com.planet.core.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.annotation.NonNull
import com.planet.core.databinding.PlaActivityWebviewBinding

/**
 *作者：张硕
 *日期：2022/04/20
 *邮箱：305562245@qq.com
 *谨记：想要完美时，完美即已不存在。
 *描述：
 **/
abstract class PlanetWebViewActivity : ImmersionActivity<PlaActivityWebviewBinding>() {

    override fun initViewBinding(): PlaActivityWebviewBinding {
        return PlaActivityWebviewBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@PlanetWebViewActivity
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView(onSavedInstanceState: Bundle?) {
        super.initView(onSavedInstanceState)

        binding.webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = true
            databaseEnabled = true
            cacheMode = WebSettings.LOAD_NO_CACHE
        }
        provideWebViewClient()?.let { binding.webView.webViewClient = it }
        val plaWebChromeClient = provideWebChromeClient(this)
        if(plaWebChromeClient!=null){
            binding.webView.webChromeClient = plaWebChromeClient.apply {
                progress = binding.progressBar
            }
        }else{
            val defaultPalWebChromeClient = PlanetWebChromeClient().apply {
                progress = binding.progressBar
            }
            binding.webView.webChromeClient = defaultPalWebChromeClient
        }

        val url = url()
        if (url.isNotBlank()) binding.webView.loadUrl(url)

        binding.toolBar.title = title()
        binding.toolBar.setNavigationOnClickListener { finish() }
    }

    override fun listeners() {
    }

    override fun doBusiness() {
    }

    override fun observerData() {
    }

    /**
     * 点击返回按钮返回前一个页面
     *
     * @param keyCode
     * @param event
     * @return boolean
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.webView.canGoBack()) {
            binding.webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.webView.removeAllViews()
        binding.webView.destroy()
    }

    /**
     * 返回被加载的Html地址
     */
    @NonNull
    abstract fun url(): String

    /**
     * 页面标题
     */
    @NonNull
    abstract fun title(): String

    abstract fun provideWebViewClient(): WebViewClient?

    open fun provideWebChromeClient(activity: Activity): PlanetWebChromeClient? = null

    override fun fitWindow(): Boolean {
        return false
    }
    override fun addStatusBarTopPadding(): View {
        return binding.toolBar
    }
}
