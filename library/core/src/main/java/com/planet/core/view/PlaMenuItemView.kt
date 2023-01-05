package com.planet.core.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.planet.core.R
import com.planet.core.anotation.Visibility
import com.planet.utils.DisplayUtil

/**
 *作者：张硕
 *日期：2022/9/21
 *邮箱：305562245@qq.com
 *谨记：想要完美时，完美即已不存在。
 *描述：列表菜单item
 **/
@Suppress("unused")
class PlaMenuItemView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private lateinit var mLeftIcon: ImageView
    private lateinit var mTitleTv: TextView
    private lateinit var mSubTitleTv: TextView
    private lateinit var mSubIcon: ImageView
    private lateinit var mTagDtv: DrawableTextView
    private lateinit var mRightIcon: ImageView
    private lateinit var mBackgroundDrawable: GradientDrawable

    private var mCornerRadius = 0
    private var mTopLeftRadius = 0
    private var mTopRightRadius = 0
    private var mBottomLeftRadius = 0
    private var mBottomRightRadius = 0

    private var mStokeWidth = 0
    private var mStrokeColor = Color.BLACK
    private var mSolidColor: Any = Color.WHITE


    init {
        mSolidColor = ContextCompat.getColor(context, R.color.tertiaryContainer)
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet) {
        val view = LayoutInflater.from(context).inflate(R.layout.pla_menu_item, this, true)
        with(view) {
            mLeftIcon = findViewById(R.id.icon_img)
            mTitleTv = findViewById(R.id.title_tv)
            mSubTitleTv = findViewById(R.id.subtitle_tv)
            mSubIcon = findViewById(R.id.sub_icon_img)
            mTagDtv = findViewById(R.id.tag_drawable_tv)
            mRightIcon = findViewById(R.id.right_icon_img)
        }

        mBackgroundDrawable = GradientDrawable()

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PlaMenuItemView)
        typedArray.getResourceId(R.styleable.PlaMenuItemView_icon, 0).let {
            if (it != 0) {
                mLeftIcon.setImageResource(it)
            }
        }

        typedArray.getInt(R.styleable.PlaMenuItemView_iconVisibility, 0).let {
            when (it) {
                0 -> mLeftIcon.visibility = View.VISIBLE
                1 -> mLeftIcon.visibility = View.INVISIBLE
                2 -> mLeftIcon.visibility = View.GONE
            }
        }
        typedArray.getString(R.styleable.PlaMenuItemView_title)?.takeIf {
            it.isNotBlank()
        }.run {
            mTitleTv.text = this
        }

        typedArray.getString(R.styleable.PlaMenuItemView_subTitle)?.takeIf {
            it.isNotBlank()
        }.run {
            mSubTitleTv.text = this
        }
        typedArray.getInt(R.styleable.PlaMenuItemView_subTitleVisibility, 2).let {
            when (it) {
                0 -> mSubTitleTv.visibility = View.VISIBLE
                1 -> mSubTitleTv.visibility = View.INVISIBLE
                2 -> mSubTitleTv.visibility = View.GONE
            }
        }
        typedArray.getResourceId(R.styleable.PlaMenuItemView_subIcon, 0).let {
            if (it != 0) {
                mSubIcon.setImageResource(it)
            }
        }

        typedArray.getInt(R.styleable.PlaMenuItemView_subIconVisibility, 2).let {
            when (it) {
                0 -> mSubIcon.visibility = View.VISIBLE
                1 -> mSubIcon.visibility = View.INVISIBLE
                2 -> mSubIcon.visibility = View.GONE
            }
        }
        typedArray.getString(R.styleable.PlaMenuItemView_tagText).let {
            if (it?.isNotBlank() == true) mTagDtv.text = it
            else mTagDtv.visibility = View.GONE
        }
        typedArray.getInt(R.styleable.PlaMenuItemView_tagVisibility, 2).let {
            when (it) {
                0 -> mTagDtv.visibility = View.VISIBLE
                1 -> mTagDtv.visibility = View.INVISIBLE
                2 -> mTagDtv.visibility = View.GONE
            }
        }
        typedArray.getColor(R.styleable.PlaMenuItemView_tagSolidColor, Color.BLACK).let {
            mTagDtv.setSolidColor(it)
        }
        typedArray.getDimension(R.styleable.PlaMenuItemView_tagCornerRadius, 0f).let {
            mTagDtv.setRadius(it.toInt())
        }

        typedArray.getDimension(R.styleable.PlaMenuItemView_radius, 0f).let {
            mCornerRadius = it.toInt()
        }
        typedArray.getDimension(R.styleable.PlaMenuItemView_topLeftRadius, 0f).let {
            mTopLeftRadius = it.toInt()
        }
        typedArray.getDimension(R.styleable.PlaMenuItemView_topRightRadius, 0f).let {
            mTopRightRadius = it.toInt()
        }
        typedArray.getDimension(R.styleable.PlaMenuItemView_bottomLeftRadius, 0f).let {
            mBottomLeftRadius = it.toInt()
        }
        typedArray.getDimension(R.styleable.PlaMenuItemView_bottomRightRadius, 0f).let {
            mBottomRightRadius = it.toInt()
        }
        typedArray.getDimension(R.styleable.PlaMenuItemView_strokeWidth, 0f).let {
            mStokeWidth = it.toInt()
        }

        typedArray.getColor(R.styleable.PlaMenuItemView_strokeColor, mStrokeColor).let {
            mStrokeColor = it
        }
        typedArray.getColor(R.styleable.PlaMenuItemView_solidColor, mSolidColor as Int).let {
            mSolidColor = it
        }
        typedArray.recycle()

        //设置背景色
        configBackgroundDrawable(mBackgroundDrawable)
        ViewCompat.setBackground(this, mBackgroundDrawable)
    }

    private fun configBackgroundDrawable(drawable: GradientDrawable) {
        //边框颜色和宽度
        if (mStokeWidth != 0) {
            drawable.setStroke(DisplayUtil.dp2px(mStokeWidth), mStrokeColor)
        }
        //填充颜色
        with(mSolidColor) {
            if (this is Int) drawable.setColor(this)
            if (this is String) drawable.setColor(Color.parseColor(mSolidColor as String))
        }
        //设置四个角的角度
        if (mCornerRadius > 0) {
            drawable.cornerRadius = mCornerRadius.toFloat()
        } else {
            drawable.cornerRadii = getRadii()
        }
    }

    fun getIconImageView() = mLeftIcon

    fun getSubIconImageView() = mSubIcon

    fun getTitleTextView() = mTitleTv

    fun getSubTitleTextView() = mSubTitleTv

    fun getTagDrawableTextView() = mTagDtv

    fun getRightIconImageView() = mRightIcon

    fun setIcon(resId: Int) {
        mLeftIcon.setImageResource(resId)
    }

    fun setIconVisibility(@Visibility visibility: Int) {
        mLeftIcon.visibility = visibility
    }

    fun setSubIcon(resId: Int) {
        mLeftIcon.setImageResource(resId)
    }

    fun setSubIconVisibility(@Visibility visibility: Int) {
        mSubIcon.visibility = visibility
    }

    fun setRightIcon(resId: Int) {
        mRightIcon.setImageResource(resId)
    }

    fun setTitle(title: String) {
        title.takeIf { it.isNotBlank() }.run { mTitleTv.text = this }
    }

    fun setTitleVisibility(@Visibility visibility: Int) {
        mTitleTv.visibility = visibility
    }

    fun setSubTitle(subTitle: String) {
        subTitle.takeIf { it.isNotBlank() }.run { mSubTitleTv.text = this }
    }

    fun setSubTitleVisibility(@Visibility visibility: Int) {
        mSubTitleTv.visibility = visibility
    }

    fun setTagDrawableTextViewText(tagText: String) {
        tagText.takeIf { it.isNotBlank() }.run { mTagDtv.text = this }
    }

    fun setTagDrawableTextViewSolidColor(color: Int) {
        mTagDtv.solidColor = color
    }

    fun setTagDrawableTextViewCornerRadius(radius: Int) {
        mTagDtv.setRadius(radius)
    }

    fun setTagDrawableTextViewVisibility(visibility: Int) {
        if (mTagDtv.visibility != visibility) {
            mTagDtv.visibility = visibility
        }
    }

    fun setTagDrawableTextViewBackgroundAlpha(alpha: Int) {
        mTagDtv.setBackgroundAlpha(alpha)
    }

    fun setBackgroundCornerRadius(topLeft: Int? = null, topRight: Int? = null, bottomLeft: Int? = null, bottomRight: Int? = null) {
        topLeft?.let {
            mTopLeftRadius = it
        }
        topRight?.let {
            mTopRightRadius = it
        }
        bottomLeft?.let {
            mBottomLeftRadius = it
        }
        bottomRight?.let {
            mBottomRightRadius = it
        }
        mBackgroundDrawable.cornerRadii = getRadii()
    }

    /**
     * 分别设置mBackgroundDrawable的圆角
     */
    private fun getRadii(): FloatArray {
        //左上角 0,1
        //右上角 2,3
        //右下角 4,5
        //右下角 6,7
        return floatArrayOf(
            mTopLeftRadius.toFloat(),
            mTopLeftRadius.toFloat(),
            mTopRightRadius.toFloat(),
            mTopRightRadius.toFloat(),
            mBottomRightRadius.toFloat(),
            mBottomRightRadius.toFloat(),
            mBottomLeftRadius.toFloat(),
            mBottomLeftRadius.toFloat(),
        )
    }
}