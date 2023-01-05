package com.planet.utils.permisson

import android.content.Context
import androidx.annotation.Keep
import androidx.annotation.NonNull

/**
 *作者：张硕
 *日期：2022/03/14
 *邮箱：305562245@qq.com
 *谨记：想要完美时，完美即已不存在。
 *描述：权限、功能检查
 **/
@Keep
interface PermissionCheck {

	/**
	 * 检查是否拥有指定权限或者已经开启指定功能
	 * @param context Context
	 * @return Boolean true拥有
	 */
	fun isGranted(@NonNull context: Context): Boolean

	/**
	 * 跳转至权限设置或开启功能界面
	 * @param context Context
	 */
	fun navToSetting(@NonNull context: Context)

}