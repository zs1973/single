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
 *描述：文字提示弹窗
 *
 * @property mTitle String? 标题
 * @property mContent String? 提示内容
 * @property mCancelText String? 取消按钮文字
 * @property mConfirmText String? 确定按钮文字
 * @property mHideCancelBtn Boolean 是否隐藏取消按钮
 * @property mConfirmClickListener OnConfirmClickListener? 确定按钮点击事件
 * @property mCancelClickListener OnCancelClickListener? 取消按钮点击事件
 * @property mContentBinding PlaTextDialogBinding  内容binding
 * @constructor
 */
class TextDialogFragment private constructor(
    private val mTitle: String? = null,
    private val mContent: String? = null,
    private val mCancelText: String? = null,
    private val mConfirmText: String? = null,
    private val mHideTitle: Boolean = false,
    private val mHideCancelBtn: Boolean = false,
    private val mConfirmClickListener: OnConfirmClickListener? = null,
    private val mCancelClickListener: OnCancelClickListener? = null
) : SimpleDialogFragment() {

    private lateinit var mContentBinding: PlaTextDialogBinding

    override fun defaultWidthAndHeight(screenInfoPair: Pair<Int, Int>): Pair<Int, Int> {
        val width = screenInfoPair.first * 0.8
        return Pair(width.toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun initData(savedInstanceState: Bundle?) {
        mContentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.pla_text_dialog,
            null,
            false
        )
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
        if(mHideTitle) hideTitle()
        if (mHideCancelBtn) hideCancelBtn()
    }

    class Builder {
        var title: String? = null
        var content: String? = null
        var cancelText: String? = null
        var confirmText: String? = null
        var hideTitle: Boolean = false
        var hideCancelBtn: Boolean = false
        var confirmClickListener: OnConfirmClickListener? = null
        var cancelClickListener: OnCancelClickListener? = null

        fun build() = TextDialogFragment(
            title,
            content,
            cancelText,
            confirmText,
            hideTitle,
            hideCancelBtn,
            confirmClickListener,
            cancelClickListener
        )

    }

    companion object {
        /**
         * @param block Function1<Builder, Unit>
         * @return TextDialogFragment
         */
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }
}