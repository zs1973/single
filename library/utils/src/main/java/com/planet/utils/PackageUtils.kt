package com.planet.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import androidx.annotation.Keep
import androidx.annotation.Nullable
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.Method


@Suppress("unused")
@Keep
object PackageUtils {

    var installedAppPackagesList: List<String>? = null

    fun initData(showSysApp: Boolean): PackageUtils {
        val pkgs = getPackageList(AppUtils.getApp().packageManager, showSysApp)
        installedAppPackagesList = pkgs.ifEmpty { getPackageListNew(AppUtils.getApp().packageManager, showSysApp) }
            return this
    }

    /**
     * 通过adb shell命令获取本机已安装应用的包名
     * @return List<String>
     */
    @Keep
    private fun getPackageList(packageManager: PackageManager, showSysApp: Boolean): List<String> {
        val packages: MutableList<String> = ArrayList()
        try {
            val p = Runtime.getRuntime().exec("pm list packages")
            val isr = InputStreamReader(p.inputStream, "utf-8")
            val br = BufferedReader(isr)
            var line: String = br.readLine()
            while (line != null) {
                line = line.trim { it <= ' ' }
                if (line.length > 8) {
                    val prefix = line.substring(0, 8)
                    if (prefix.equals("package:", ignoreCase = true)) {
                        line = line.substring(8).trim { it <= ' ' }
                        if (!TextUtils.isEmpty(line)) {
                            val packageInfo = packageManager.getPackageInfo(line, 0)
                            if (showSysApp) {
                                packages.add(line)
                            } else {
                                if (!isSystemApp(packageInfo)) {
                                    packages.add(line)
                                }
                            }
                        }
                    }
                }
                line = br.readLine()
            }
            br.close()
            p.destroy()
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return packages
    }

    /**
     * 系统API获取本机已安装应用的包名
     * @return List<String>
     */
    @SuppressLint("QueryPermissionsNeeded")
    fun getPackageListNew(packageManager: PackageManager, showSysApp: Boolean): List<String> {
        val packages: MutableList<String> = ArrayList()
        try {
            val packageInfos: List<PackageInfo> =
                packageManager.getInstalledPackages(
                    PackageManager.GET_ACTIVITIES or
                            PackageManager.GET_SERVICES
                )
            for (info in packageInfos) {
                val pkg = info.packageName
                if (showSysApp) {
                    packages.add(pkg)
                } else {
                    if (!isSystemApp(info)) {
                        packages.add(pkg)
                    }
                }
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return packages
    }

    /**
     * 根据PackageInfo判断是否为系统应用
     *
     * @param pi PackageInfo
     * @return Boolean
     */
    private fun isSystemApp(pi: PackageInfo): Boolean {
        val isSysApp = pi.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 1
        val isSysUpd = pi.applicationInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP == 1
        return isSysApp || isSysUpd
    }

    /**
     * 获取当前应用的VersionCode
     *
     * @param mContext
     * @return
     */
    fun getVersionCode(mContext: Context): String {
        val versionCode: String = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            mContext.packageManager.getPackageInfo(mContext.packageName, 0).versionCode.toString()
        } else {
            mContext.packageManager.getPackageInfo(
                mContext.packageName,
                0
            ).longVersionCode.toString()
        }
        return versionCode
    }

    /**
     * 获取当前应用的VersionName
     *
     * @param context 上下文
     * @return
     */
    fun getVerName(context: Context): String {
        var versionName = ""
        try {
            versionName = context.packageManager
                .getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionName
    }

    /**
     * 获取当前应用的名称
     * @param context Context
     * @return String
     */
    fun getAppName(context: Context): String {
        val packageManager = context.packageManager
        val applicationInfo = packageManager.getApplicationInfo(context.packageName, 0)
        return packageManager.getApplicationLabel(applicationInfo).toString()
    }

    fun getAppSelfIcon(context: Context): Int {
        val packageManager = context.packageManager
        val applicationInfo = packageManager.getApplicationInfo(context.packageName, 0)
        return applicationInfo.icon
    }


    /**
     * 获取应用
     * @param context Context
     * @return Drawable
     */
    @Nullable
    fun getAppIcon(context: Context, packageName: String): Drawable? {
        try {
            val packageManager = context.packageManager
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            return applicationInfo.loadIcon(packageManager)
        } catch (_: Exception) {
        }
        return null
    }

    /**
     * 停止指定包名的app
     * @param pkgName String
     */
    @Keep
    fun stopApp(pkgName: String) {
        try {
            val activityManager =
                AppUtils.getApp().getSystemService(ACTIVITY_SERVICE) as ActivityManager?
            val method: Method =
                Class.forName("android.app.ActivityManager")
                    .getMethod("forceStopPackage", String::class.java)
            method.isAccessible = true
            method.invoke(activityManager, pkgName)
        } catch (e: Exception) {
            LogUtils.loge("stopApp", e.message.toString())
        }
    }

}