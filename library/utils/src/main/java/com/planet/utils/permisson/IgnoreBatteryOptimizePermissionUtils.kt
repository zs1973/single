package com.planet.utils.permisson

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import com.planet.utils.AppUtils
import com.planet.utils.OsUtils
import com.planet.utils.R
import com.planet.utils.ToastUtils
import com.planet.utils.permisson.os.EmuiPermissionUtils
import com.planet.utils.permisson.os.MiuiPermissionUtils


/**
 *作者：张硕
 *日期：2022/03/14
 *邮箱：305562245@qq.com
 *谨记：想要完美时，完美即已不存在。
 *描述：电池优化工具类
 **/
object IgnoreBatteryOptimizePermissionUtils : PermissionCheck {

	private val mTag = this.javaClass.simpleName

	override fun isGranted(context: Context): Boolean {
		return when {
			OsUtils.isMIUI() -> {
				MiuiPermissionUtils.hasIgnoringBatteryOptimizations(context)
			}
			OsUtils.isEMUI() -> {
				EmuiPermissionUtils.hasIgnoringBatteryOptimizations(context)
			}
			else -> {
				val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
				powerManager.isIgnoringBatteryOptimizations(context.packageName)
			}
		}
	}

	@SuppressLint("BatteryLife")
	override fun navToSetting(context: Context) {
		when {
			OsUtils.isMIUI() -> {
				MiuiPermissionUtils.batteryOptimizeSetting(context)
			}
			OsUtils.isEMUI() -> {
				EmuiPermissionUtils.batteryOptimizeSetting(context)
			}
			else -> {
				try {
					val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
					intent.data = Uri.parse("package:" + context.packageName)
					context.startActivity(intent)
				} catch (e: Exception) {
					ToastUtils.showShort(AppUtils.getApp().getString(R.string.util_nav_fail))
				}
			}
		}
	}
}