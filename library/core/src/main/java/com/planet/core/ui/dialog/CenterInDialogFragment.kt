package com.planet.core.ui.dialog

import android.view.Gravity
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.planet.core.ui.PlanetDialogFragment

/**
 *作者：张硕
 *日期：2022/03/25
 *邮箱：305562245@qq.com
 *谨记：想要完美时，完美即已不存在。
 *描述：从底部弹出的Dialog
 **/
abstract class CenterInDialogFragment<V : ViewDataBinding>(@LayoutRes contentLayout: Int) :
    PlanetDialogFragment<V>(contentLayout) {

    override fun gravity() = Gravity.CENTER

    override fun setupWindowAnimationStyle() = 0

}