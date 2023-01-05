package com.planet.utils.permisson.os

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

/**
 *作者：张硕
 *日期：2022/9/16
 *邮箱：305562245@qq.com
 *谨记：想要完美时，完美即已不存在。
 *描述：vivo-OriginOs
 **/
object VivoOriginOsPermissionUtils:OsPermissionUtil {
    override fun windowAlertSetting(context: Context) {
    }

    override fun appUsePermissionSetting(context: Context) {
    }

    override fun batteryOptimizeSetting(context: Context) {
    }

    override fun autoStartSetting(context: Context) {
        val intent = Intent()
        intent.component = ComponentName.unflattenFromString("com.vivo.permissionmanager/com.vivo.permissionmanager.activity.BgStartUpManagerActivity")
        val packageManager = context.packageManager
        val activities = packageManager.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )
        if (activities.size > 0) {
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun hasWindowAlertPermission(context: Context): Boolean {
        return false
    }

    override fun hasAppUsePermission(context: Context): Boolean {
        return false
    }

    override fun hasIgnoringBatteryOptimizations(context: Context): Boolean {
        return true
    }
}