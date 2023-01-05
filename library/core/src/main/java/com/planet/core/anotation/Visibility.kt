package com.planet.core.anotation

import android.view.View
import androidx.annotation.IntDef

@IntDef(value = [View.VISIBLE, View.INVISIBLE, View.GONE])
@Retention(AnnotationRetention.SOURCE)
annotation class Visibility