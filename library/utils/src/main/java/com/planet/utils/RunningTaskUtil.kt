package com.planet.utils

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.ComponentName
import android.content.Context
import java.util.*

/**
 * 获取前台正在运行的程序
 */
object RunningTaskUtil {

    private const val TAG = "RunningTaskUtil"

    private var mUsageStatsManager: UsageStatsManager = AppUtils.getApp()
        .getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    private lateinit var topComponentName: ComponentName

    fun getTopRunningTasksByEvent(): String {
        try {
            val time = System.currentTimeMillis()
            val usageEvents: UsageEvents =
                mUsageStatsManager.queryEvents(time - 3 * 60 * 1000, time)
            var out: UsageEvents.Event
            val map: TreeMap<Long?, UsageEvents.Event?> = TreeMap()
            if (usageEvents != null) {
                while (usageEvents.hasNextEvent()) {
                    out = UsageEvents.Event() //这里一定要初始化，不然getNextEvent会报空指针
                    if (usageEvents.getNextEvent(out)) {
                        if (out != null) {
                            map[out.timeStamp] = out
                        } else {
                            LogUtils.loge(TAG, " out is NULL")
                        }
                    } else {
                        LogUtils.loge(TAG, " usageEvents is unavailable")
                    }
                }
                if (!map.isEmpty()) {
                    //将key set颠倒过来，让最近的排列在上面
                    val keySets = map.navigableKeySet()
                    val iterator: Iterator<*> = keySets.descendingIterator()
                    while (iterator.hasNext()) {
                        val event = map[iterator.next()]
                        if (event!!.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                            //当遇到有app移动到前台时，就更新topComponentName
                            topComponentName = ComponentName(event.packageName, "")
                            break
                        }
                    }
                }
            } else {
                return ""
            }
            if (::topComponentName.isInitialized) {
                return topComponentName.packageName
            }
        } catch (e: Exception) {
            return ""
        }
        return ""
    }
}