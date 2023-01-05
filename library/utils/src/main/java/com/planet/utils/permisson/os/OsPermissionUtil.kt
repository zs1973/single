package com.planet.utils.permisson.os

import android.content.Context

/**
 *作者：张硕
 *日期：2022/03/30
 *邮箱：305562245@qq.com
 *谨记：想要完美时，完美即已不存在。
 *描述：
 **/
interface OsPermissionUtil {
	/**
	 * 跳转设置悬浮窗界面
	 * @param context Context
	 */
	fun windowAlertSetting(context: Context)

	/**
	 * 跳转使用访问权限设置界面
	 * @param context Context
	 */
	fun appUsePermissionSetting(context: Context)

	/**
	 * 跳转忽略电池优化
	 * @param context Context
	 */
	fun batteryOptimizeSetting(context: Context)

	/**
	 * 跳转自动启动设置
	 * @param context Context
	 */
	fun autoStartSetting(context: Context)

	/**
	 * 是否有悬浮窗权限
	 * @param context Context
	 * @return Boolean
	 */
	fun hasWindowAlertPermission(context: Context):Boolean

	/**
	 * 是否有应用访问权限
	 * @param context Context
	 * @return Boolean
	 */
	fun hasAppUsePermission(context: Context):Boolean

	/**
	 * 是否忽略了电池优化
	 * @param context Context
	 * @return Boolean
	 */
	fun hasIgnoringBatteryOptimizations(context: Context):Boolean
}