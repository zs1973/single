package com.planet.core.app

import androidx.lifecycle.ViewModelProvider

/**
 * [VmApplication]实现此接口，可以提供一个存在于应用生命周期之中的ViewModel
 */
interface AppViewModelProviderOwner {
    fun getAppViewModelProvider(): ViewModelProvider
}