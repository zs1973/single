package com.planet.core.ui

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.planet.core.R
import com.planet.utils.DisplayUtil

/**
 *作者：张硕
 *日期：2022/03/25
 *邮箱：305562245@qq.com
 *谨记：想要完美时，完美即已不存在。
 *描述：
 **/
abstract class PlanetDialogFragment<V : ViewDataBinding>(@LayoutRes contentLayout: Int) :
    DialogFragment(contentLayout), PlanetView {

    protected lateinit var binding: V
    protected open var mWindow: Window? = null
    private var screenInfoPair: Pair<Int, Int>? = null
    private val mTag: String = this.javaClass.simpleName

    /**
     * 设置Dialog弹出动画
     *
     * @return Int 动画样式style，可选[R.style.BottomAnimationStyle],[R.style.TopAnimationStyle],
     * [R.style.LeftAnimationStyle],[R.style.RightAnimationStyle]
     */
    abstract fun setupWindowAnimationStyle(): Int

    /**
     * 配置Dialog显示的方位，默认在屏幕中心显示
     *
     * @return Int
     */
    open fun gravity() = Gravity.CENTER
    open fun cancel() = true
    open fun canceledOnTouchOutside() = true

    /**
     * 设置Dialog的宽高。
     * - 可以使具体值。
     * - 也可以使WindowManager.LayoutParams.WRAP_CONTENT WindowManager.LayoutParams.MATCH_PARENT
     *
     * @param screenInfoPair 屏幕宽高信息
     * @return Pair<Int, Int> 新的宽高
     */
    abstract fun defaultWidthAndHeight(screenInfoPair: Pair<Int, Int>): Pair<Int, Int>?

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = view.let<View, V> { DataBindingUtil.bind(it)!! }.apply { lifecycleOwner = viewLifecycleOwner }
        mWindow = dialog?.window
        screenInfoPair = getScreenInfo()
        setStyle(STYLE_NORMAL, R.style.Dialog)
        setupDialog()
        initData(savedInstanceState)
        initView(savedInstanceState)
        doBusiness()
        observerData()
        listeners()
    }

    private fun setupDialog() {
        val widthAndHeightPair =
            defaultWidthAndHeight(getScreenInfo()) ?: Pair((screenInfoPair!!.first * 0.8).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        dialog?.window?.apply {
            //默认透明背景
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(gravity())
            setLayout(widthAndHeightPair.first, widthAndHeightPair.second)
            setWindowAnimations(setupWindowAnimationStyle())
        }
        dialog?.setCancelable(cancel())
        dialog?.setCanceledOnTouchOutside(canceledOnTouchOutside())
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        //旋转屏幕时重新获取屏幕宽高
        screenInfoPair = getScreenInfo()
    }

    override fun doBusiness() {}

    /**
     * 获取屏幕宽高
     *
     * @return Pair<Int, Int>? 屏幕宽高信息
     */
    private fun getScreenInfo(): Pair<Int, Int> {
        return Pair(DisplayUtil.getScreenWidth(), DisplayUtil.getScreenHeight())
    }
}
