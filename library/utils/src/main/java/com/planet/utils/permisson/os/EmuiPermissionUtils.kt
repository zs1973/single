package com.planet.utils.permisson.os

import android.annotation.SuppressLint
import android.app.usage.UsageStatsManager
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import com.planet.utils.LogUtils

/**
 *作者：张硕
 *日期：2022/03/30
 *邮箱：305562245@qq.com
 *谨记：想要完美时，完美即已不存在。
 *描述：华为-emui
 **/
object EmuiPermissionUtils : OsPermissionUtil {

	private val mTag = javaClass.simpleName

	override fun windowAlertSetting(context: Context) {
		val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
		intent.data = Uri.parse("package:" + context.packageName)
		context.startActivity(intent)
	}

	override fun appUsePermissionSetting(context: Context) {
		val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
		intent.data = Uri.parse("package:" + context.packageName)
		try {
			context.startActivity(intent)
		} catch (e: ActivityNotFoundException) {
			LogUtils.loge(mTag, e.message.toString())
		}
	}

	@SuppressLint("BatteryLife")
	override fun batteryOptimizeSetting(context: Context) {
		val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
		intent.data = Uri.parse("package:" + context.packageName)
		context.startActivity(intent)
	}

	override fun autoStartSetting(context: Context) {
		//华为-手机管家
		val intent = Intent()
		intent.component =
			ComponentName.unflattenFromString("com.huawei.systemmanager/.mainscreen.MainScreenActivity")
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
		Build.VERSION_CODES.M
		return Settings.canDrawOverlays(context)
	}

	override fun hasAppUsePermission(context: Context): Boolean {
		val usageStatsManager =
			context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
		val currentTime = System.currentTimeMillis()
		val stats = usageStatsManager.queryUsageStats(
			UsageStatsManager.INTERVAL_DAILY, currentTime - 60 * 1000, currentTime
		)
		return stats.size != 0
	}

	override fun hasIgnoringBatteryOptimizations(context: Context): Boolean {
		val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
		return powerManager.isIgnoringBatteryOptimizations(context.packageName)
	}

}