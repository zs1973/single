package com.planet.utils.permisson.os

import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import com.planet.utils.AppUtils
import com.planet.utils.R
import com.planet.utils.ToastUtils

/**
 *作者：张硕
 *日期：2022/03/30
 *邮箱：305562245@qq.com
 *谨记：想要完美时，完美即已不存在。
 *描述：小米-miui
 **/
object MiuiPermissionUtils : OsPermissionUtil {

    override fun windowAlertSetting(context: Context) {
        //跳转MIUI-应用权限设置界面
        try {
            val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity")
            intent.putExtra("extra_pkgname", context.packageName)
            context.startActivity(intent)
        } catch (e: Exception) {
            try { // MIUI 5/6/7
                val localIntent = Intent("miui.intent.action.APP_PERM_EDITOR")
                localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity")
                localIntent.putExtra("extra_pkgname", context.packageName)
                context.startActivity(localIntent)
            } catch (e1: java.lang.Exception) { // 不然跳转到应用详情
                context.startActivity(getAppDetailSettingIntent(context))
            }
        }
    }

    override fun appUsePermissionSetting(context: Context) {
        try {
            val intent = Intent("android.settings.USAGE_ACCESS_SETTINGS")
            intent.component = ComponentName.unflattenFromString("com.android.settings/.Settings\$AppUsageAccessSettingsActivity")
            intent.data = Uri.parse("package:" + context.packageName)
            context.startActivity(intent)
        } catch (e: Exception) {
            ToastUtils.showShort(AppUtils.getApp().getString(R.string.util_nav_fail))
        }
    }

    @SuppressLint("BatteryLife")
    override fun batteryOptimizeSetting(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.data = Uri.parse("package:" + context.packageName)
            context.startActivity(intent)
        } catch (e: Exception) {
            ToastUtils.showShort(AppUtils.getApp().getString(R.string.util_nav_fail))
        }
    }

    override fun autoStartSetting(context: Context) {
        //跳转MIUI系统应用信详情
        try {
            val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
            intent.component = ComponentName.unflattenFromString("com.android.settings/.applications.InstalledAppDetails")
            intent.data = Uri.parse("package:" + context.packageName)
            context.startActivity(intent)
        } catch (e: Exception) {
            ToastUtils.showShort(AppUtils.getApp().getString(R.string.util_nav_fail))
        }
    }

    override fun hasWindowAlertPermission(context: Context): Boolean {
        val version = Build.VERSION.SDK_INT
        if (version >= 19) {
            val manager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            try {
                val managerClass: Class<*> = manager.javaClass
                @SuppressLint("DiscouragedPrivateApi")
                val method = managerClass.getDeclaredMethod("checkOp", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, String::class.java)
                val isAllowNum = method.invoke(manager, 24, Binder.getCallingUid(), context.packageName) as Int
                return AppOpsManager.MODE_ALLOWED == isAllowNum
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }

    override fun hasAppUsePermission(context: Context): Boolean {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val currentTime = System.currentTimeMillis()
        val stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, currentTime - 60 * 1000, currentTime)
        return stats.size != 0
    }

    override fun hasIgnoringBatteryOptimizations(context: Context): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isIgnoringBatteryOptimizations(context.packageName)
    }

    private fun getAppDetailSettingIntent(context: Context): Intent {
        val localIntent = Intent()
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
        localIntent.data = Uri.fromParts("package", context.packageName, null)
        return localIntent
    }
}