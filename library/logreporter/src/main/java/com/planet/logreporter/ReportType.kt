package com.planet.logreporter

sealed class ReportType{
    /**
     * 腾讯Bugly
     */
    object Bugly: ReportType()

    /**
     * 友盟
     */
    object Um: ReportType()
}
