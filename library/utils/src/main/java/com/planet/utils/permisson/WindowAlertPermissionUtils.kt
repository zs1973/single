package com.planet.utils.permisson

import android.content.Context
import androidx.annotation.Keep
import com.planet.utils.AppUtils
import com.planet.utils.OsUtils
import com.planet.utils.R
import com.planet.utils.ToastUtils
import com.planet.utils.permisson.os.EmuiPermissionUtils
import com.planet.utils.permisson.os.MiuiPermissionUtils


/**
 *作者：张硕
 *日期：2022/03/16
 *邮箱：305562245@qq.com
 *谨记：想要完美时，完美即已不存在。
 *描述：悬浮窗权限工具类
 **/
object WindowAlertPermissionUtils : PermissionCheck {

	@Keep
	override fun isGranted(context: Context): Boolean {
		return when {
			OsUtils.isMIUI() -> {
				MiuiPermissionUtils.hasWindowAlertPermission(context)
			}
			OsUtils.isEMUI() -> {
				EmuiPermissionUtils.hasWindowAlertPermission(context)
			}
			else -> {
				true
			}
		}
	}

	override fun navToSetting(context: Context) {
		when {
			OsUtils.isMIUI() -> {
				MiuiPermissionUtils.windowAlertSetting(context)
			}
			OsUtils.isEMUI() -> {
				EmuiPermissionUtils.windowAlertSetting(context)
			}
			else -> {
				//com.vivo.permissionmanager/com.vivo.permissionmanager.activity.FloatWindowManagerActivity
				ToastUtils.showShort(AppUtils.getApp().getString(R.string.util_nav_fail))
			}
		}
	}

}