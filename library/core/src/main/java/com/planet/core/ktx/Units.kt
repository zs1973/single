@file:Suppress("unused")

package com.planet.core.ktx

import android.content.res.Resources

//<editor-fold desc="尺寸">
val Number.dp2px
    get() = (this.toFloat() * Resources.getSystem().displayMetrics.density).toInt()

val Number.sp2px
    get() = (this.toFloat() * Resources.getSystem().displayMetrics.scaledDensity).toInt()
//</editor-fold>


// <editor-fold default state="collapsed" desc="内存">
val Number.KB: Long
    get() = this.toLong() * 1024L

val Number.MB: Long
    get() = this.KB * 1024

val Number.GB: Long
    get() = this.MB * 1024
// </editor-fold>




