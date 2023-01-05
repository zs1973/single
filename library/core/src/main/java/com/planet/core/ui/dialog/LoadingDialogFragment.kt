package com.planet.core.ui.dialog

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.planet.core.R
import com.planet.core.databinding.PlaLoadingDialogBinding
import com.planet.core.ui.PlanetDialogFragment

/**
 *作者：张硕
 *日期：2022/04/10
 *邮箱：305562245@qq.com
 *谨记：想要完美时，完美即已不存在。
 *描述：
 **/
class LoadingDialogFragment : PlanetDialogFragment<PlaLoadingDialogBinding>(R.layout.pla_loading_dialog) {
    override fun defaultWidthAndHeight(screenInfoPair: Pair<Int, Int>): Pair<Int, Int> {
        return Pair(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initView(onSavedInstanceState: Bundle?) {
        binding.tipText.visibility = View.GONE
    }

    override fun listeners() {
    }

    override fun observerData() {
    }

    override fun setupWindowAnimationStyle(): Int = 0

}