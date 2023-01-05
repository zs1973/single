package com.planet.utils.permisson

import android.content.Context
import android.content.Intent
import android.provider.Settings
import kotlin.reflect.KClass


/**
 *作者：张硕
 *日期：2022/03/17
 *邮箱：305562245@qq.com
 *谨记：想要完美时，完美即已不存在。
 *描述：无障碍服务工具类
 **/
object AccessibilityServicePermissionUtils : PermissionCheck {

	private val mTag = this.javaClass.simpleName

	override fun isGranted(context: Context): Boolean {
		//原理是判断这个service有没有在运行
		var enable = Settings.Secure.getInt(context.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
		if (enable == 1) {
			val settingValue = Settings.Secure.getString(
				context.contentResolver,
				Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
			)
			if (settingValue == null || !settingValue.contains("TimeKeeperAccessibilityService")) {
				enable = 0
			}
		}
		return enable == 1
	}

	override fun navToSetting(context: Context) {
		val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
		context.startActivity(intent)
	}

	/**
	 * 开启无障碍辅助服务
	 * @param context Context
	 * @param target KClass<*> XXXXService::class.java
	 */
	fun startService(context: Context, target: KClass<*>) {
		val intent = Intent(context.applicationContext, target.java)
		context.applicationContext.startService(intent)
	}
}