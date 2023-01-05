package com.planet.core.ui

import android.os.Bundle

/**
 *作者：张硕
 *日期：11/18/2021
 *邮箱：305562245@qq.com
 **/
interface PlanetView {

    /**
     * view初始化时，为view准备初始化需要的数据;
     */
    fun initData(savedInstanceState: Bundle?)

    /**
     * 执行初始化View操作
     *
     * @param onSavedInstanceState Bundle?
     */
    fun initView(onSavedInstanceState: Bundle?)

    /**
     * 点击事件
     */
    fun listeners()

    /**
     * 请求网络数据
     */
    fun doBusiness()

    /**
     * 处理数据
     */
    fun observerData()

}