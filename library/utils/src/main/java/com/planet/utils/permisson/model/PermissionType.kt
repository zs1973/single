package com.planet.utils.permisson.model

sealed class PermissionType {
	abstract val name: String
	abstract val code: Int
	abstract val desc: String

	/**
	 * 系统悬浮窗
	 */
	object WindowAlert : PermissionType() {
		override val name: String
			get() = "悬浮窗"
		override val code: Int
			get() = 0
		override val desc: String
			get() = "在桌面显示计时组件"
	}

	/**
	 * 应用使用记录
	 */
	object AppUseUsage : PermissionType() {
		override val name: String
			get() = "使用情况访问"
		override val code: Int
			get() = 1
		override val desc: String
			get() = "保证计时组件正常运行"
	}

	/**
	 * 电池优化
	 */
	object BatteryOptimize : PermissionType() {
		override val name: String
			get() = "关闭电池优化"
		override val code: Int
			get() = 2
		override val desc: String
			get() = "保证计时组件正常运行"
	}

	/**
	 * 自动启动
	 */
	object AutoStartAfterReboot : PermissionType() {
		override val name: String
			get() = "自动启动(不变色)"
		override val code: Int
			get() = 3
		override val desc: String
			get() = "保证计时组件准确统计时间"
	}

	/**
	 * 在后台弹出界面（MIUI）
	 */
	object AlertInBackground:PermissionType(){
		override val name: String
			get() = "后台弹出界面"
		override val code: Int
			get() = 4
		override val desc: String
			get() = "保证计时组件正常显示"
	}

	/**
	 * 读取应用列表（MIUI）
	 */
	object ReadInstalledAppList:PermissionType(){
		override val name: String
			get() = "读取应用列表"
		override val code: Int
			get() = 5
		override val desc: String
			get() = "保证计时组件准确统计时间"
	}

}
