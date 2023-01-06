package com.planet.logreporter

import android.content.Context
import android.os.Process
import com.meituan.android.walle.WalleChannelReader
import com.planet.utils.AppUtils
import com.planet.utils.DeviceUtil
import com.planet.utils.ProcessUtils
import com.tencent.bugly.crashreport.CrashReport
import com.umeng.commonsdk.UMConfigure

object Reporter {

    /**
     * 预初始化友盟
     */
    fun preInitUm(){
        val context = AppUtils.getApp()
        val channel: String = WalleChannelReader.getChannel(context)?:"未配置渠道文件"
        UMConfigure.preInit(context, BuildConfig.umengAppKey, channel)
    }

    fun initReporter(reportType: ReportType) {
        val context: Context = AppUtils.getApp().applicationContext
        val channel: String = WalleChannelReader.getChannel(context)?:"未配置渠道文件"
        when (reportType) {
            ReportType.Bugly -> {
                val packageName = context.packageName
                val processName = ProcessUtils.getProcessName(Process.myPid())
                val userStrategy = CrashReport.UserStrategy(context)
                userStrategy.isUploadProcess = processName == null || processName == packageName
                userStrategy.deviceModel = DeviceUtil.getPhoneBrand() +"-"+ DeviceUtil.getPhoneModel()
                userStrategy.appChannel=channel
                CrashReport.initCrashReport(context,userStrategy)
            }
            ReportType.Um->{
                UMConfigure.init(context, BuildConfig.umengAppKey, channel, UMConfigure.DEVICE_TYPE_PHONE, "")
            }
        }
    }

}