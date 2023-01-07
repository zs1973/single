package com.planet.core.anotation

import android.view.View
import androidx.annotation.IntDef

/**
 * View可见性注解
 */
@IntDef(value = [View.VISIBLE, View.INVISIBLE, View.GONE])
@Retention(AnnotationRetention.SOURCE)
annotation class Visibility