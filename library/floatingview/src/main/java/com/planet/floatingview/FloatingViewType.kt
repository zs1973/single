package com.planet.floatingview

/**
 * 悬浮窗类型
 */
sealed class FloatingViewType {
	abstract val value: Int
	abstract val name:String

	/**
	 * 计时悬浮窗
	 */
	object Timing : FloatingViewType() {
		override val value: Int
			get() = 0
		override val name: String
			get() = "一直显示"
	}

	/**
	 * 提醒悬浮窗
	 */
	object Notification : FloatingViewType() {
		override val value: Int
			get() = 1
		override val name: String
			get() = "通知显示"
	}

	/**
	 * 全屏提醒悬浮窗
	 */
	object FullScreen : FloatingViewType() {
		override val value: Int
			get() = 2
		override val name: String
			get() = "全屏显示"
	}

	/**
	 * 时间锁悬浮窗
	 */
	object TimeLock:FloatingViewType(){
		override val value: Int
			get() = 3
		override val name: String
			get() = "时间锁"
	}
}
