package com.planet.core.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.planet.core.R
import com.planet.core.databinding.PlaTextDialogBinding

/**
 *作者：张硕
 *日期：2022/03/29
 *邮箱：305562245@qq.com
 *谨记：想要完美时，完美即已不存在。
 *描述：将应用移出监控任务弹窗
 **/
class TextDialogFragment(
    private val mTitle: String? = null,
    private val mContent: String? = null,
    private val mCancelText: String? = null,
    private val mConfirmText: String? = null,
    private val mConfirmClickListener: OnConfirmClickListener? = null,
    private val mCancelClickListener: OnCancelClickListener? = null
) : SimpleDialogFragment() {

    private lateinit var mContentBinding: PlaTextDialogBinding
    override fun defaultWidthAndHeight(screenInfoPair: Pair<Int, Int>): Pair<Int, Int> {
        val width = screenInfoPair.first * 0.8
        return Pair(width.toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun initData(savedInstanceState: Bundle?) {
        mContentBinding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.pla_text_dialog, null, false)
    }

    override fun observerData() {}

    override fun initView(onSavedInstanceState: Bundle?) {
        super.initView(onSavedInstanceState)
        addContentView(mContentBinding.root)
        if (mTitle == null) hideTitle() else setTitleText(mTitle)
        mContent?.let { mContentBinding.contentTv.text = it }
        mCancelText?.let { setCancelText(it) }
        mConfirmText?.let { setConfirmText(it) }
        mConfirmClickListener?.let { setOnConfirmClickListener(mConfirmClickListener) }
        mCancelClickListener?.let { setOnCancelClickListener(mCancelClickListener) }
    }

    class Builder {
        var title: String? = null
        var content: String? = null
        var cancelText: String? = null
        var confirmText: String? = null
        var confirmClickListener: OnConfirmClickListener? = null
        var cancelClickListener: OnCancelClickListener? = null

        fun build() = TextDialogFragment(title, content, cancelText, confirmText, confirmClickListener, cancelClickListener)
    }

    companion object {
        /**
         * 带接收者的函数类型,这意味着我们需要向函数传递一个Builder类型的实例
         */
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }
}