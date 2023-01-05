package com.planet.core.view

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.planet.core.R


/**
 * 简单的加载等待状态
 *
 * @property tipText TextView
 * @property contentView View
 * @constructor
 */
class LoadingDialog(
    context: Context,
    themeResId: Int = R.style.LoadingDialog
) : AlertDialog(context, themeResId) {

    private val tipText: TextView

    private val contentView: View =
        LayoutInflater.from(context).inflate(R.layout.pla_widget_loading_dialog, null)

    init {
        tipText = contentView.findViewById(R.id.tipText)
        setView(contentView)
        setCancelable(false)
    }

    constructor(context: Context) : this(context, R.style.LoadingDialog)

    constructor(context: Context, text: String) : this(context, R.style.LoadingDialog) {
        if (text.isNotBlank()) {
            tipText.text = text
        }
    }

    constructor(context: Context, text: String, @DrawableRes drawableResId: Int):this(context,text){

    }

    fun setLoadingText(text: String) {
        if (text.isNotBlank()) {
            tipText.text = text
        }
    }
}