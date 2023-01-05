package com.planet.utils

import androidx.lifecycle.LifecycleObserver

/**
 * 倒计时器
 */
class CountDownTimerManger : LifecycleObserver {

	private lateinit var mCountDownTimer: CountDownTimer

	/**
	 *
	 * @param tick Function1<Long, Unit>
	 * @return CountDownTimerManger
	 */
	fun startTimeCountDown(millisInFuture:Long,countDownInterval:Long, tick: (Long) -> Unit): CountDownTimerManger {
		if(!::mCountDownTimer.isInitialized){
			mCountDownTimer =CountDownTimer(millisInFuture, countDownInterval).apply {
				start(object :CountDownTimer.OnTimerCallBack{
					override fun onStart() {

					}

					override fun onTick(times: Long) {

					}

					override fun onFinish() {

					}
				})

			}
		}
		return this
	}

	fun cancel() {
		mCountDownTimer.cancel()
	}
}