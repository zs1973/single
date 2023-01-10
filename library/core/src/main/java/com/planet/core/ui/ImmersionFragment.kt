@file:Suppress("unused")
package com.planet.core.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.planet.core.R
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX
import com.zackratos.ultimatebarx.ultimatebarx.statusBarOnly

/**
 *作者：张硕
 *日期：11/19/2021
 *邮箱：305562245@qq.com
 *
 * 具有沉浸式状态栏的Fragment，尽可以在ViewPager中使用。
 * ViewPager2+Fragment实现沉浸式在
 **/
abstract class ImmersionFragment<V : ViewDataBinding>(@LayoutRes layoutRes: Int) :
    PlanetFragment<V>(layoutRes) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initImmersion()
    }

    /**
     * 配置页面沉浸式样式
     */
    open fun initImmersion() {
        statusBarOnly {
            light = light()
            fitWindow = fitWindow()
            colorRes = colorRes()
        }
        //侵入状态栏时给最上方的View设置padding 通常是标题栏view
        if(!fitWindow()){
            addStatusBarTopPadding()?.let {
                UltimateBarX.addStatusBarTopPadding(it)
            }
        }
    }

    /**
     * 默认深色图标
     */
    open fun light() = true

    /**
     * 布局不侵入状态栏
     * true为不侵入状态栏，false为侵入状态栏
     */
    open fun fitWindow() = true

    /**
     * 状态栏白色
     */
    open fun colorRes() = ContextCompat.getColor(mContext, R.color.primary)

    /**
     * 底部导航栏颜色
     */
    open fun bottomNavColor() = ContextCompat.getColor(mContext,R.color.background)

    /**
     * 实现沉浸式状态栏时需设置
     *
     * @return
     */
    open fun addStatusBarTopPadding(): View? = null
}