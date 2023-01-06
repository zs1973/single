@file:Suppress("unused")

package com.planet.core.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.planet.core.R
import com.planet.core.databinding.PlaDialogBinding
import com.planet.core.ktx.click

/**
 *作者：张硕
 *日期：2022/03/25
 *邮箱：305562245@qq.com
 *谨记：想要完美时，完美即已不存在。
 *描述：
 **/
abstract class SimpleDialogFragment : CenterInDialogFragment<PlaDialogBinding>(R.layout.pla_dialog) {

    private lateinit var mTitleTv: TextView
    private lateinit var mCancelBtn: Button
    private lateinit var mConfirmBtn: Button
    private lateinit var mContentContainer: LinearLayout

    private lateinit var mOnCancelClickListener: OnCancelClickListener
    private lateinit var mOnConfirmClickListener: OnConfirmClickListener

    override fun defaultWidthAndHeight(screenInfoPair: Pair<Int, Int>): Pair<Int, Int>? = null

    override fun initView(onSavedInstanceState: Bundle?) {
        mTitleTv = binding.titleTv
        mCancelBtn = binding.cancelBtn
        mConfirmBtn = binding.confirmBtn
        mContentContainer = binding.contentContainer
        mTitleTv.text = titleText()
        mCancelBtn.text = cancelText()
        mConfirmBtn.text = confirmText()
        mTitleTv.visibility = if (!showTitle()) View.GONE else View.VISIBLE
    }

    override fun listeners() {
        mCancelBtn.click {
            if (::mOnCancelClickListener.isInitialized) mOnCancelClickListener.onCancel()
            dismiss()
        }
        mConfirmBtn.click {
            if (::mOnConfirmClickListener.isInitialized) mOnConfirmClickListener.onConfirm(this)
        }
    }

    protected open fun showTitle() = true

    protected open fun titleText() = getString(R.string.pla_dialog_title)

    protected open fun cancelText() = getString(R.string.pla_dialog_cancel)

    protected open fun confirmText() = getString(R.string.pla_dialog_confirm)

    fun setTitleText(text: String): SimpleDialogFragment {
        mTitleTv.text = text.ifBlank { getString(R.string.pla_dialog_title) }
        return this
    }

    fun setConfirmText(text: String): SimpleDialogFragment {
        mConfirmBtn.text = text.ifBlank { getString(R.string.pla_dialog_confirm) }
        return this
    }

    fun setCancelText(text: String): SimpleDialogFragment {
        mCancelBtn.text = text.ifBlank { getString(R.string.pla_dialog_cancel) }
        return this
    }

    fun addContentView(view: View): SimpleDialogFragment {
        mContentContainer.addView(view)
        return this
    }

    fun addContentView(@LayoutRes layoutResId: Int): SimpleDialogFragment {
        val view = LayoutInflater.from(requireContext()).inflate(layoutResId, null, false)
        mContentContainer.addView(view)
        return this
    }

    fun hideCancelBtn() {
        binding.cancelBtn.visibility = View.GONE
    }

    fun hideTitle() {
        binding.titleTv.visibility = View.GONE
    }

    fun setOnCancelClickListener(onCancelClickListener: OnCancelClickListener) {
        mOnCancelClickListener = onCancelClickListener
    }

    fun setOnConfirmClickListener(onConfirmClickListener: OnConfirmClickListener) {
        mOnConfirmClickListener = onConfirmClickListener
    }

    interface OnCancelClickListener {
        fun onCancel()
    }

    interface OnConfirmClickListener {
        fun onConfirm(dialogFragment: SimpleDialogFragment)
    }

}