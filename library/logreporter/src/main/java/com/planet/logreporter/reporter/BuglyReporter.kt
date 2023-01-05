package com.planet.logreporter.reporter

import com.planet.utils.AppUtils
import com.tencent.bugly.crashreport.CrashReport

object BuglyReporter : IReporter {
    override fun initReporter(channel: String, isDebug: Boolean) {
       CrashReport.initCrashReport(AppUtils.getApp(),channel,isDebug)
    }
}