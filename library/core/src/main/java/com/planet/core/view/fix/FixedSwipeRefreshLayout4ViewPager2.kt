package com.planet.core.view.fix

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlin.math.abs

/**
 * 修复与ViewPager2一起使用时出现的滑动冲突问题
 */
class FixedSwipeRefreshLayout4ViewPager2 constructor(
	context: Context, attrs: AttributeSet
) : SwipeRefreshLayout(context, attrs) {
	private var startX = 0
	private var startY: Int = 0
	//private var beginScroll = false //是否开始滑动
	override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
		when (ev.action) {
			MotionEvent.ACTION_DOWN -> {
				startX = ev.x.toInt()
				startY = ev.y.toInt()
				//parent.requestDisallowInterceptTouchEvent(true)
			}
			MotionEvent.ACTION_MOVE -> {
				val endX = ev.x.toInt()
				val endY = ev.y.toInt()
				val disX = abs(endX - startX)
				val disY = abs(endY - startY)
				if (disX > disY) {
					//if (!beginScroll)
					parent.requestDisallowInterceptTouchEvent(false)
				} else {
					//beginScroll = true
					parent.requestDisallowInterceptTouchEvent(true)
				}
			}
			MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
				parent.requestDisallowInterceptTouchEvent(false)
				//beginScroll=false
			}
		}
		return super.dispatchTouchEvent(ev)
	}
}