package com.planet.core.ui

import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.ProgressBar

/**
 *作者：张硕
 *日期：2022/9/9
 *邮箱：305562245@qq.com
 *谨记：想要完美时，完美即已不存在。
 *描述：
 **/
open class PlanetWebChromeClient : WebChromeClient() {

    var progress: ProgressBar? = null

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        progress?.let {
            if (newProgress == 100) {
                progress!!.visibility = View.GONE
            } else {
                progress!!.visibility = View.VISIBLE
            }
            progress!!.progress = newProgress
        }
    }

}