package com.planet.utils

import android.graphics.drawable.StateListDrawable
import androidx.annotation.ColorRes

object SelectorUtils {

    @JvmStatic
    fun createSelector(
        @ColorRes color: Int,
        cornerRadius: Int?,
        shape: Int?
    ): StateListDrawable {
//        val normalDrawable = DrawableUtils.createGradientDrawable(
//	        null,
//	        null,
//	        color,
//	        cornerRadius,
//	        shape,
//	        null,
//	        null,
//	        null
//        )
//
//        val pressedDrawable = DrawableUtils.createGradientDrawable(
//	        null,
//	        null,
//	        ColorUtils.getBrighterColor(color),
//	        cornerRadius,
//	        shape,
//	        null,
//	        null,
//	        null
//        )

        val pressed = android.R.attr.state_pressed
        val stateListDrawable = StateListDrawable()
//        stateListDrawable.addState(intArrayOf(pressed), pressedDrawable)
//        stateListDrawable.addState(intArrayOf(-pressed), normalDrawable)
        return stateListDrawable
    }
}

