package com.planet.core.binding

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.planet.base_imageloader.ImageLoader
import com.planet.utils.DisplayUtil
import java.text.SimpleDateFormat
import java.util.*

/**
 * 设置ShapeableImageView的单个圆角或者所有圆角
 *
 * @param imageView         被设置的Shape able ImageView
 * @param radius            统一设置四个角的圆角
 * @param topLeftRadius     设置左上方圆角
 * @param topRightRadius    设置右上方圆角
 * @param bottomLeftRadius  设置左下方圆角
 * @param bottomRightRadius 设置右下方圆角
 */
@Suppress("unused")
@BindingAdapter(
    value = [
        "radius",
        "topLeftRadius",
        "topRightRadius",
        "bottomLeftRadius",
        "bottomRightRadius"
    ],
    requireAll = false
)
fun bindShapeableImageViewRadius(
    imageView: ShapeableImageView,
    radius: Int?,
    topLeftRadius: Int?,
    topRightRadius: Int?,
    bottomLeftRadius: Int?,
    bottomRightRadius: Int?
) {
    val shapeAppearanceModel = ShapeAppearanceModel.builder()

    if (radius != null) {
        shapeAppearanceModel.setAllCorners(
            CornerFamily.ROUNDED,
            DisplayUtil.dp2px(radius).toFloat()
        )
    } else {
        topLeftRadius?.let {
            shapeAppearanceModel.setTopLeftCorner(
                CornerFamily.ROUNDED,
                DisplayUtil.dp2px(topLeftRadius).toFloat()
            )
        }
        topRightRadius?.let {
            shapeAppearanceModel.setTopRightCorner(
                CornerFamily.ROUNDED,
                DisplayUtil.dp2px(topRightRadius).toFloat()
            )
        }
        bottomLeftRadius?.let {
            shapeAppearanceModel.setBottomLeftCorner(
                CornerFamily.ROUNDED,
                DisplayUtil.dp2px(bottomLeftRadius).toFloat()
            )
        }
        bottomRightRadius?.let {
            shapeAppearanceModel.setBottomRightCorner(
                CornerFamily.ROUNDED,
                DisplayUtil.dp2px(bottomRightRadius).toFloat()
            )
        }
    }
    imageView.shapeAppearanceModel = shapeAppearanceModel.build()
}

/**
 *
 * 给View设置Drawable背景
 *
 * @param view View
 * @param strokeWidth Int?                      边框宽度
 * @param strokeColor Int?                      边框颜色
 * @param solidColor Int?                       填充颜色
 * @param cornerRadius Int?                     圆角大小:单位dp
 * @param startColor Int?                       渐变起始颜色（需要同时设置endColor）
 * @param endColor Int?                         渐变结束颜色（需要同时设置startColor）
 * @param gradientType Int?                     渐变类型（如果要设置渐变色背景可设置此属性）
 *                                              1.线性[GradientDrawable.LINEAR_GRADIENT]
 *                                              2.圆形 [GradientDrawable.RADIAL_GRADIENT]
 *                                              3.扫描 [GradientDrawable.SWEEP_GRADIENT]
 * @param drawableOrientation Orientation?      控制渐变色的绘制方向（）,值参考[GradientDrawable.Orientation]
 *
 */
@BindingAdapter(
    value = [
        "strokeWidth",
        "strokeColor",
        "dashWidth",
        "dashGap",
        "solidColor",
        "cornerRadius",
        "topLeftRadius",
        "topRightRadius",
        "bottomLeftRadius",
        "bottomRightRadius",
        "startColor",
        "endColor",
        "gradientType",
        "drawableOrientation"
    ],
    requireAll = false
)
@Suppress("unused")
fun bindViewDrawable(
    view: View,
    strokeWidth: Int?,
    strokeColor: Int?,
    dashWidth: Float = 0.0f,
    dashGap: Float = 0.0f,
    solidColor: Any?,
    cornerRadius: Int = 0,
    topLeftRadius: Int = 0,
    topRightRadius: Int = 0,
    bottomLeftRadius: Int = 0,
    bottomRightRadius: Int = 0,
    startColor: Int?,
    endColor: Int?,
    gradientType: Int?,
    drawableOrientation: GradientDrawable.Orientation?
) {
    val drawable = GradientDrawable()
    //边框颜色和宽度
    if (strokeColor != null && strokeWidth != null) {
        drawable.setStroke(DisplayUtil.dp2px(strokeWidth), strokeColor, dashWidth, dashGap)
    }
    //填充颜色
    solidColor?.let {
        if (it is Int) drawable.setColor(it)
        if (it is String)drawable.setColor(Color.parseColor(solidColor as String))
    }
    //设置四个角的角度
    if (cornerRadius > 0) {
        drawable.cornerRadius = DisplayUtil.dp2px(cornerRadius).toFloat()
    } else {
        //单独设置四个角角度
        val radii = FloatArray(8)
        //左上角
        radii[0] = DisplayUtil.dp2px(topLeftRadius).toFloat()
        radii[1] = DisplayUtil.dp2px(topLeftRadius).toFloat()
        //右上角
        radii[2] = DisplayUtil.dp2px(topRightRadius).toFloat()
        radii[3] = DisplayUtil.dp2px(topRightRadius).toFloat()
        //右下角
        radii[4] = DisplayUtil.dp2px(bottomRightRadius).toFloat()
        radii[5] = DisplayUtil.dp2px(bottomRightRadius).toFloat()
        //右下角
        radii[6] = DisplayUtil.dp2px(bottomLeftRadius).toFloat()
        radii[7] = DisplayUtil.dp2px(bottomLeftRadius).toFloat()
        drawable.cornerRadii = radii
    }
    //渐变色色，需同时设置
    if (startColor != null && endColor != null) {
        val colors = intArrayOf(startColor, endColor)
        drawable.colors = colors
    }
    //渐变类型
    gradientType?.let { drawable.gradientType = it }
    drawableOrientation?.let { drawable.orientation = drawableOrientation }
    ViewCompat.setBackground(view, drawable)
}

//@BindingAdapter(
//    value = [
//        "color",
//        "cornerRadius",
//        "shape"
//    ],
//    requireAll = false
//)
//fun bindViewSelector(view: View, color: Int, cornerRadius: Int?, shape: Int?) {
//    val stateListDrawable = DrawableUtils.createStateListDrawable(color, cornerRadius, shape)
//    ViewCompat.setBackground(view, stateListDrawable)
//}


@BindingAdapter(
    value = [
        "goneWhen"
    ]
)
@Suppress("unused")
fun goneWhen(view: View, gone: Boolean) {
    if (gone) {
        view.visibility = View.GONE
    } else {
        view.visibility = View.VISIBLE
    }
}

/**
 * 根据时间产生格式化字符串
 * @param milli 指定时间, 单位毫秒, 如果小于0将设置为空字符串
 * @param format 格式化文本
 */
@Suppress("unused")
@BindingAdapter(value = ["dateMilli", "dateFormat"], requireAll = false)
fun setDateFromMillis(v: TextView, milli: Long, format: String? = "yyyy-MM-dd") {
    if (milli < 0) {
        v.text = ""
        return
    }
    val finalFormat = if (format.isNullOrBlank()) "yyyy-MM-dd" else format
    val date = Date(milli)
    val sf = SimpleDateFormat(finalFormat, Locale.CHINA)
    val formatText = sf.format(date)
    if (v.text.contentEquals(formatText)) return
    v.text = formatText
}

/**
 *
 * 监听EditText文字输入事件。
 * 这个方法接收一个无参函数作为参数。
 */
@Suppress("unused")
@BindingAdapter("afterTextChanged")
fun afterTextChanged(view: EditText, afterTextChanged: () -> Unit) {
    view.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            afterTextChanged()
        }
    })
}

@Suppress("unused")
@BindingAdapter("afterTextChanged")
fun afterTextChanged(view: EditText, afterTextChanged: (s: Editable?) -> Unit) {
    view.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            afterTextChanged(s)
        }
    })

}

/**
 * ImageView显示Url图片
 *
 * @param imageView 目标ImageView
 * @param imageUrl  Url
 */
@Suppress("unused")
@BindingAdapter("imageUrl")
fun bindImageUrl(imageView: ImageView, imageUrl: String?) {
    imageUrl?.let {
        if (imageUrl.isNotEmpty()) {
            ImageLoader.load(imageView, "$imageUrl", 0.4f)
        }
    }
}

/**
 * ImageView及其子类绑定本地图片资源
 *
 * @param img ImageView
 * @param resId Int
 */
@Suppress("unused")
@BindingAdapter("android:src")
fun setSrc(img: ImageView, @DrawableRes resId: Int?) {
    resId?.let {
        img.setImageResource(resId)
    }

}

/**
 * ImageView及其子类绑定本地图片资源
 *
 * @param img ImageView
 * @param drawable Drawable
 */
@Suppress("unused")
@BindingAdapter("android:src")
fun setSrc(img: ImageView, drawable: Drawable?) {
    drawable?.let {
        ImageLoader.load(img, drawable)
    }
}





