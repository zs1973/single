package com.planet.base_imageloader

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.ofu.base_imageloader.R
import com.planet.utils.DisplayUtil
import java.io.File
import java.util.UUID

/**
 *作者：张硕
 *日期：2022/03/01
 *邮箱：305562245@qq.com
 *描述：
 **/
object ImageLoader {

	/**
	 * 加载图片
	 *
	 * @param imageView ImageView
	 * @param url String
	 * @param thumbnail Float?
	 */
	fun load(imageView: ImageView, url: String, thumbnail: Float? = 0.0f) {
		Glide.with(imageView.context)
			.load(url)
			//.apply(getRequestOptions())
			.transition(GenericTransitionOptions.with(R.anim.image_fade_in))
			.placeholder(R.drawable.pla_ic_image_place)
			.error(R.drawable.pla_ic_image_place)
			.thumbnail(thumbnail ?: 0.0f)
			.into(imageView)
	}

	/**
	 * 加载图片
	 *
	 * @param imageView ImageView
	 * @param file File
	 * @param thumbnail Float?
	 */
	fun load(imageView: ImageView, file: File, thumbnail: Float? = 0.0f) {
		Glide.with(imageView.context)
			.load(file)
			.signature(ObjectKey(UUID.randomUUID().toString().replace("-","")))
			//.apply(getRequestOptions())
			.transition(GenericTransitionOptions.with(R.anim.image_fade_in))
			.placeholder(R.drawable.pla_ic_image_place)
			.error(R.drawable.pla_ic_image_place)
			.thumbnail(thumbnail ?: 0.0f)
			.into(imageView)
	}

	/**
	 * 加载图片
	 *
	 * @param imageView ImageView
	 * @param resourceId Int
	 */
	fun load(imageView: ImageView,  @DrawableRes resourceId: Int) {
		Glide.with(imageView.context)
			.load(resourceId)
			.transition(GenericTransitionOptions.with(R.anim.image_fade_in))
			.placeholder(R.drawable.pla_ic_image_place)
			.error(R.drawable.pla_ic_image_place)
			.into(imageView)
	}

	/**
	 * 加载图片
	 *
	 * @param imageView ImageView
	 * @param uri Uri
	 */
	fun load(imageView: ImageView, uri: Uri) {
		Glide.with(imageView.context)
			.load(uri)
			.transition(GenericTransitionOptions.with(R.anim.image_fade_in))
			.placeholder(R.drawable.pla_ic_image_place)
			.error(R.drawable.pla_ic_image_place)
			.into(imageView)
	}

	/**
	 * 加载图片
	 *
	 * @param imageView ImageView
	 * @param drawable Drawable
	 */
	fun load(imageView: ImageView, drawable: Drawable) {
		Glide.with(imageView.context)
			.load(drawable)
			.transition(GenericTransitionOptions.with(R.anim.image_fade_in))
			.placeholder(R.drawable.pla_ic_image_place)
			.error(R.drawable.pla_ic_image_place)
			.into(imageView)
	}


	private fun getRequestOptions(): RequestOptions {
		return RequestOptions().placeholder(R.drawable.pla_ic_image_place)
			.error(R.drawable.pla_ic_image_place)
			.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
			.skipMemoryCache(false)
			.priority(Priority.NORMAL)
	}

	/**
	 * 等比缩放图片
	 *
	 * @param imageView       被缩放图片
	 * @param originalWidth   原始宽度
	 * @param originalHeight  原始高度
	 * @param targetWidth     目标宽度
	 * 目标高度：目标宽度*图片原始高度/图片原始宽度
	 * 目标宽度：目标高度*图片原始宽度/图片原始高度
	 *
	 */
	@JvmStatic
	fun resetImageWh(
		imageView: ImageView,
		originalWidth: Int,
		originalHeight: Int,
		targetWidth: Int
	) {
		//缩放比例
		val scaling: Float
		val imgTargetWidth: Int = when {
			originalWidth > originalHeight -> {
				(targetWidth * 0.5f).toInt()
			}
			//正方形图片直接返回图片的宽 但宽度不能超过targetWidth
			originalWidth == originalHeight -> {
				DisplayUtil.dp2px(120)
			}
			else -> {
				DisplayUtil.dp2px(120)
			}
		}
		//计算缩放比例 ow:tw=oh:th
		scaling = originalWidth.toFloat() / imgTargetWidth.toFloat()
		//计算图片等比例放大后的高
		val imageViewHeight = (originalHeight / scaling).toInt()
		val params = imageView.layoutParams
		params.width = imgTargetWidth
		params.height = imageViewHeight
		imageView.layoutParams = params
	}


	fun resetImageWh(view: View, targetWidth: Int) {
		val params = view.layoutParams as ViewGroup.LayoutParams
		params.width = targetWidth
		params.height = targetWidth
		//if(params is  GridLayoutManager.LayoutParams){
		//    params.width = targetWidth
		//    params.height = targetWidth
		//}else if(params is FrameLayout.LayoutParams){
		//    params.width = targetWidth
		//    params.height = targetWidth
		//}
		view.layoutParams = params
	}

	fun clearImage(context:Context,view: View){
		Glide.with(context).clear(view)
	}

}