package com.planet.utils

import androidx.recyclerview.widget.RecyclerView

object RecyclerViewUtils {

	fun setScrollSpeed(recyclerView: RecyclerView, velocity: Int) {
		try {
			val field = recyclerView.javaClass.getDeclaredField("mMaxFlingVelocity")
			field.isAccessible = true
			field.set(recyclerView, velocity)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

}