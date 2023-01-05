package com.planet.logreporter.reporter

/**
 *作者：张硕
 *日期：7/16/2022
 *邮箱：305562245@qq.com
 *谨记：想要完美时，完美即已不存在。
 *描述：
 **/
interface IReporter {
    fun initReporter(channel: String, isDebug: Boolean)
}