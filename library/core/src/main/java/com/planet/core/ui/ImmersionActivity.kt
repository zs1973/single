package com.planet.core.ui

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.planet.core.R
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX
import com.zackratos.ultimatebarx.ultimatebarx.statusBarOnly


/**
 * 作者：张硕
 * 日期：11/19/2021
 * 邮箱：305562245@qq.com
 *
 * 具有沉浸式状态栏的Activity
 * 注意：
 * 1.ViewPager2+Fragment的场景下，不建议在Fragment中控制状态栏的样式，因为Fragment初始化需要时间，设置沉浸
 * 样式的时候可能会发生延迟，显示效果不好。建议在host viewpager的host activity中控制状态栏样式。
 * 2.Activity+Fragment的场景下，可使用[ImmersionFragmentActivity]，将沉浸样式交由Fragment控制，因为沉浸式
 * 框架支持在Fragment中设置状态栏样式（也是设置host activity的状态栏样式）。
 */
abstract class ImmersionActivity<V : ViewDataBinding> : PlanetActivity<V>() {

    override fun initView(onSavedInstanceState: Bundle?) {
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
        if (!fitWindow()) {
            addStatusBarTopPadding()?.let {
                UltimateBarX.addStatusBarTopPadding(it)
            }
        }
    }

    /**
     * 设置状态栏图标颜色
     *
     * true深色图标，false浅色图标
     */
    open fun light() = true

    /**
     * 布局不侵入状态栏
     *
     * true为不侵入状态栏，false为侵入状态栏
     */
    open fun fitWindow() = true

    /**
     * 子类复写次方法返回自己的状态栏颜色，必须是R.color.xx形式
     */
    open fun colorRes() = R.color.primary

    /**
     * 底部导航栏颜色
     */
    open fun bottomNavColor() = getColor(R.color.background)

    /**
     * 侵入状态栏时给最上方的View设置padding，通常是标题栏。
     */
    open fun addStatusBarTopPadding(): View? = null

}