package com.planet.logreporter.reporter

import com.meituan.android.walle.WalleChannelReader
import com.planet.logreporter.BuildConfig
import com.planet.utils.AppUtils
import com.umeng.commonsdk.UMConfigure

object UmReporter :IReporter {

    fun preInit() {
        val context = AppUtils.getApp()
        val channel: String = WalleChannelReader.getChannel(context)?:"未配置渠道文件"
        UMConfigure.preInit(context, BuildConfig.umengAppKey, channel)
    }
    override fun initReporter(channel: String, isDebug: Boolean) {
        UMConfigure.init(AppUtils.getApp(),BuildConfig.umengAppKey, channel, UMConfigure.DEVICE_TYPE_PHONE, "")
    }
}