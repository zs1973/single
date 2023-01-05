package com.planet.logreporter.model

sealed class ReportType{
    object Bugly:ReportType()

    object UMeng:ReportType()
}
