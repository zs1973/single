@file:Suppress("unused")
package com.planet.core.ktx

import android.view.View
import com.planet.core.R


/**
 *作者：张硕
 *日期：2022/02/24
 *邮箱：305562245@qq.com
 *描述：
 **/

/**
 * get set
 * 给view添加一个上次触发时间的属性（用来屏蔽连击操作）
 */
private var <T : View>T.triggerLastTime: Long
	get() = if (getTag(R.id.triggerLastTimeKey) != null) getTag(R.id.triggerLastTimeKey) as Long else 0
	set(value) {
		setTag(R.id.triggerLastTimeKey, value)
	}

/**
 * get set
 * 给view添加一个延迟的属性（用来屏蔽连击操作）
 */
private var <T : View> T.triggerDelay: Long
	get() = if (getTag(R.id.triggerDelayKey) != null) getTag(R.id.triggerDelayKey) as Long else -1
	set(value) {
		setTag(R.id.triggerDelayKey, value)
	}

/**
 * 判断时间是否满足再次点击的要求（控制点击）
 */
private fun <T : View> T.clickEnable(): Boolean {
	var clickable = false
	val currentClickTime = System.currentTimeMillis()
	if (currentClickTime - triggerLastTime >= triggerDelay) {
		clickable = true
	}
	triggerLastTime = currentClickTime
	return clickable
}

/***
 * 带延迟过滤点击事件的 View 扩展
 * @param delay Long 延迟时间，默认200毫秒
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
fun <T : View> T.click(delay: Long = 200, block: (T) -> Unit) {
	triggerDelay = delay
	setOnClickListener {
		if (clickEnable()) {
			block(this)
		}
	}
}

