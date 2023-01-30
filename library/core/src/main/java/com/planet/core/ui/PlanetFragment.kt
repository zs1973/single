package com.planet.core.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.umeng.analytics.MobclickAgent

/**
 *作者：张硕
 *日期：11/17/2021
 *邮箱：305562245@qq.com
 */
abstract class PlanetFragment<V : ViewDataBinding>(@LayoutRes layoutRes: Int) : Fragment(layoutRes), PlanetView {

    private val openLog: Boolean = true

    protected lateinit var binding: V
    protected lateinit var mContext: Context

    private var firstLoad = true
    private val dataDelay = Handler(Looper.getMainLooper())
    private var mApplicationProvider: ViewModelProvider? = null
    private val delay = Handler(Looper.getMainLooper())

    protected open val mTag: String = javaClass.simpleName

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!
        binding.apply { lifecycleOwner = viewLifecycleOwner }
        initData(savedInstanceState)
        initView(savedInstanceState)
        listeners()
        observerData()
    }

    override fun onResume() {
        super.onResume()
        alwaysPerform()
        //懒加载
        if (openLazyLoad()) {
            if (firstLoad) {
                delay.postDelayed({doBusiness()},200)
                firstLoad = false
            }
        } else {
            delay.postDelayed({doBusiness()},200)
        }
        //友盟统计
        MobclickAgent.onPageStart(mTag)
    }

    /**
     * 懒加载状态下可能需要在onResume生命周期里执行一些其他的逻辑。
     * 比如每次到这个界面的时候都检查一次权限
     */
    open fun alwaysPerform() {}

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPageEnd(mTag)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    /**
     * 是否开启懒加载模式。
     *
     * @return Boolean true开启
     * 参考：https://blog.csdn.net/zhaoyanjun6/article/details/113545036
     */
    protected open fun openLazyLoad(): Boolean = false


}