package com.planet.core.ktx

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

fun <V : ViewDataBinding> Activity.setContent(@LayoutRes layout: Int): V {
    return DataBindingUtil.setContentView(this, layout)
}

fun <V : ViewDataBinding> ViewGroup.inflate(@LayoutRes layout: Int): V {
    return DataBindingUtil.inflate(LayoutInflater.from(context), layout, this, false)
}

fun <V : ViewDataBinding> Context.inflate(@LayoutRes layout: Int): V {
    return DataBindingUtil.inflate(LayoutInflater.from(this), layout, null, false)
}

fun <V : ViewDataBinding> View.bind(): V {
    return DataBindingUtil.bind(this)!!
}