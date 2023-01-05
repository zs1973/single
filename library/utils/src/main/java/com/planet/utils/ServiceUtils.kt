package com.planet.utils

import android.app.ActivityManager
import android.content.Context
import android.text.TextUtils


object ServiceUtils {

	/**
	 * 指定服务是否正在运行
	 * @param context Context
	 * @param ServiceName String 服务名称 xxx::java.class.name
	 * @return Boolean
	 */
	fun isServiceRunning(context: Context, ServiceName: String): Boolean {
		if (TextUtils.isEmpty(ServiceName)) {
			return false
		}
		val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
		val runningService =
			manager.getRunningServices(1000) as ArrayList<ActivityManager.RunningServiceInfo>
		for (i in 0 until runningService.size) {
			if (runningService[i].service.className == ServiceName) {
				return true
			}
		}
		return false
	}
}