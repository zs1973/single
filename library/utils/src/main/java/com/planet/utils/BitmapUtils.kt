package com.planet.utils

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream


object BitmapUtils {

	/**
	 * bitmap转字节数组
	 * @param bitmap Bitmap
	 * @return ByteArray?
	 */

	fun bitmap2Bytes(bitmap: Bitmap): ByteArray {
		return bitmap2Bytes(bitmap, 100)
	}

	fun bitmap2Bytes(bitmap: Bitmap, quality: Int): ByteArray {
		val baos = ByteArrayOutputStream()
		bitmap.compress(CompressFormat.PNG, quality, baos)
		return baos.toByteArray()
	}

	/**
	 * 字节数组转bitmap
	 * @param data ByteArray
	 * @return Bitmap?
	 */
	fun byte2Bitmap(data: ByteArray): Bitmap {
		return BitmapFactory.decodeByteArray(data, 0, data.size)
	}
}