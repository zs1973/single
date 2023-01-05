package com.planet.utils

import android.content.Context
import android.content.pm.PackageManager

object AppInstallUtil {

    fun isInstall(context: Context, packageName: String): Boolean {
        try {
            val packageManager: PackageManager = context.packageManager
            val pkm = packageManager.getInstalledPackages(0)
            for (i in pkm.indices) {
                val pn = pkm[i].packageName
                if (pn == packageName) {
                    return true
                }
            }
        } catch (e: Exception) {
            LogUtils.loge("AppInstallUtil.isInstall", "isInstall:${e.message.toString()}")
        }
        return false
    }

}