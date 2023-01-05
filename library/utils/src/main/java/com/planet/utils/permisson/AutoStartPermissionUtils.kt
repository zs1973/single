package com.planet.utils.permisson

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.planet.utils.*
import com.planet.utils.permisson.os.EmuiPermissionUtils
import com.planet.utils.permisson.os.MiuiPermissionUtils

/**
 * 应用开机自启权限
 */
object AutoStartPermissionUtils : PermissionCheck {

    override fun isGranted(context: Context): Boolean {
        //开机自启权限不需要运行时授权，需要手动设置这里默认返回true
        return true
    }

    override fun navToSetting(context: Context) {
        when {
            OsUtils.isMIUI() -> {
                MiuiPermissionUtils.autoStartSetting(context)
            }

            OsUtils.isEMUI() -> {
                EmuiPermissionUtils.autoStartSetting(context)
            }

            else -> {
                navToAutoStart(context)
            }
        }
    }

    private fun navToAutoStart(context: Context) {
        //vivo OriginOs 应用自动启动权限管理
        val vivoOriginOsIntent = Intent()
        vivoOriginOsIntent.component =
            ComponentName.unflattenFromString("com.vivo.permissionmanager/com.vivo.permissionmanager.activity.BgStartUpManagerActivity")

        //华为荣耀 MagicOS 手机管家
        val magicUiIntent = Intent()
        magicUiIntent.component = ComponentName.unflattenFromString(
            "com.hihonor.systemmanager/com.hihonor.systemmanager.mainscreen" +
                    ".MainScreenActivity"
        )

        val intents = arrayOf(vivoOriginOsIntent,magicUiIntent)

        var hit = 0
        for (index in intents.indices) {
            val packageManager = context.packageManager
            val activities = packageManager.queryIntentActivities(
                intents[index],
                PackageManager.MATCH_DEFAULT_ONLY
            )
            if (activities.size > 0) {
                hit = 1
                try {
                    context.startActivity(vivoOriginOsIntent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                break
            }
        }

        if(hit==0){
            ToastUtils.showShort(AppUtils.getApp().getString(R.string.util_nav_fail))
        }
    }
}