package com.planet.single

import com.planet.core.app.VmApplication
import com.planet.utils.AppUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : VmApplication() {
    override fun onCreate() {
        super.onCreate()
        AppUtils.init(this)
    }
}