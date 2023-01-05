package com.planet.core.app

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore

/**
 * 提供一个Application级别的ViewModelStore
 */
open class VmApplication : Application(), AppViewModelProviderOwner {


    private val mViewModelStore: ViewModelStore by lazy {
        ViewModelStore()
    }

    private val mFactory: ViewModelProvider.Factory by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this)
    }

    override fun getAppViewModelProvider(): ViewModelProvider = ViewModelProvider(mViewModelStore, mFactory)

}