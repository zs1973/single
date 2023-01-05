package com.planet.logreporter

import android.app.Application
import android.content.Context
import android.os.Process
import com.meituan.android.walle.WalleChannelReader
import com.planet.logreporter.model.ReportType
import com.planet.utils.AppUtils
import com.planet.utils.DeviceUtil
import com.planet.utils.ProcessUtils
import com.tencent.bugly.crashreport.CrashReport
import com.umeng.commonsdk.UMConfigure


object Reporter {

    private lateinit var mContext: Application

    private const val UMENG_APPKEY:String ="6258ee7b30a4f67780a3fa46"

    fun getInstance(): Reporter {
        this.mContext = AppUtils.getApp()
        return this
    }

    /**
     * 预初始化友盟
     */
    fun preInitUMENG(){

    }

    fun initReporter(reportType: ReportType) {
        val channel: String = WalleChannelReader.getChannel(mContext)?:"未配置渠道文件"
        when (reportType) {
            ReportType.Bugly -> {
                val context: Context = AppUtils.getApp().applicationContext
                val packageName = context.packageName
                val processName = ProcessUtils.getProcessName(Process.myPid())
                val userStrategy = CrashReport.UserStrategy(mContext)
                userStrategy.isUploadProcess = processName == null || processName == packageName
                userStrategy.deviceModel = DeviceUtil.getPhoneBrand() +"-"+ DeviceUtil.getPhoneModel()
                userStrategy.appChannel=channel
                CrashReport.initCrashReport(mContext,userStrategy)
            }
            ReportType.UMeng->{
                UMConfigure.init(mContext, UMENG_APPKEY, channel, UMConfigure.DEVICE_TYPE_PHONE, "")
            }
        }
    }

}