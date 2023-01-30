package com.planet.core.ui

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.planet.utils.ActivityControl
import com.umeng.analytics.MobclickAgent

/**
 * 作者：张硕
 * 日期：11/17/2021
 * 邮箱：305562245@qq.com
 *
 * @param V : ViewBinding
 * @property binding V
 * @property tag String
 */
abstract class PlanetActivity<V : ViewDataBinding> : AppCompatActivity(), PlanetView {

    protected lateinit var binding: V
    protected open val tag: String = this.javaClass.simpleName
    private var mApplicationProvider: ViewModelProvider? = null
    private val delay = Handler(Looper.getMainLooper())

    private val mTag = javaClass.simpleName

    abstract fun initViewBinding(): V

    override fun onCreate(savedInstanceState: Bundle?) {
        // <editor-fold default state="collapsed" desc="设置高刷新率">
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            display?.supportedModes.apply {
                this?.sortBy { it.refreshRate }
                this?.last()?.modeId?.let { mode->
                    window.let {
                        val lp = it.attributes
                        //设置最大刷新率
                        lp.preferredDisplayModeId = mode
                        it.attributes = lp
                    }
                }
            }
        } else {
            val modes = window.windowManager.defaultDisplay.supportedModes
            modes.sortBy { it.refreshRate }
            window.let {
                val lp = it.attributes
                //设置最大刷新率
                lp.preferredDisplayModeId = modes.last().modeId
                it.attributes = lp
            }
        }
        // </editor-fold>
        super.onCreate(savedInstanceState)
        ActivityControl.getInstance().add(this)
        binding = initViewBinding()
        setContentView(binding.root)
        initData(savedInstanceState)
        initView(savedInstanceState)
        listeners()
        observerData()
        //doBusiness()
    }

    override fun onStart() {
        super.onStart()
        delay.postDelayed(
            {
                doBusiness()
            }, 200
        )
    }


    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart(mTag)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
        ActivityControl.getInstance().close(this)
        MobclickAgent.onPageEnd(mTag)
    }

}