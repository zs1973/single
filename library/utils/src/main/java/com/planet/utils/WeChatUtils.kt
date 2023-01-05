package com.planet.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

/**
 * 微信工具类
 *
 * @constructor Create empty We chat utils
 */
object WeChatUtils {

    private val mTag = this.javaClass.simpleName

    fun isInstall(context: Context): Boolean {
        try {
            val packageManager: PackageManager = context.packageManager
            val pkm = packageManager.getInstalledPackages(0)
            if (pkm != null) {
                for (i in pkm.indices) {
                    val pn = pkm[i].packageName
                    if (pn == "com.tencent.mm") {
                        return true
                    }
                }
            }
        } catch (e: Exception) {
            LogUtils.loge(mTag, "isInstall:${e.message.toString()}")
        }
        return false
    }

    fun openWeChat(context: Context) {
        //适用微信双开
        try {
            val launcher = context.packageManager.getLaunchIntentForPackage("com.tencent.mm")
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.component = launcher?.component
            context.startActivity(intent)
        } catch (e: Exception) {
            LogUtils.loge(mTag,"openWeChat:${e.message.toString()}")
        }
    }
}