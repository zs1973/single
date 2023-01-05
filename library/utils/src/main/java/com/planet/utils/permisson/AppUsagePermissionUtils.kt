package com.planet.utils.permisson

import android.app.usage.UsageStatsManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.planet.utils.*
import com.planet.utils.permisson.os.EmuiPermissionUtils
import com.planet.utils.permisson.os.MiuiPermissionUtils


/**
 *作者：张硕
 *日期：2022/03/14
 *邮箱：305562245@qq.com
 *谨记：想要完美时，完美即已不存在。
 *描述：访问App使用记录权限工具类
 **/
object AppUsagePermissionUtils : PermissionCheck {

	@RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
	override fun isGranted(context: Context): Boolean {
		return when {
			OsUtils.isMIUI() -> {
				MiuiPermissionUtils.hasAppUsePermission(context)
			}
			OsUtils.isEMUI() -> {
				EmuiPermissionUtils.hasAppUsePermission(context)
			}
			else -> {
				val usageStatsManager =
					context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
				val currentTime = System.currentTimeMillis()
				val stats = usageStatsManager.queryUsageStats(
					UsageStatsManager.INTERVAL_DAILY, currentTime - 60 * 1000, currentTime
				)
				return stats.size != 0
			}
		}
	}

	override fun navToSetting(context: Context) {
		context.let {
			when {
				OsUtils.isMIUI() -> {
					MiuiPermissionUtils.appUsePermissionSetting(context)
				}
				OsUtils.isEMUI() -> {
					EmuiPermissionUtils.appUsePermissionSetting(context)
				}
				else -> {
					try {
						val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
						intent.data = Uri.parse("package:" + context.packageName)
						context.startActivity(intent)
					} catch (e: ActivityNotFoundException) {
						ToastUtils.showShort(AppUtils.getApp().getString(R.string.util_nav_fail))
						LogUtils.loge("PermissionCheckUtils", e.message.toString())
					}
				}
			}
		}
	}
  }