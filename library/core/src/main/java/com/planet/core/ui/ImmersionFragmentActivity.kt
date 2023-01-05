package com.planet.core.ui

import androidx.databinding.ViewDataBinding


/**
 * 作者：张硕
 * 日期：11/19/2021
 * 邮箱：305562245@qq.com
 *
 * 具有沉浸式状态栏的Activity，仅用于Activity内部展示Fragment的场景。
 * 因为使用的沉浸式状态栏框架支持在Fragment中直接设置host activity的状态栏，
 * 所以这里不需要给activity设置默认的沉浸状态，沉浸状态交由附加的Fragment设置。
 */
abstract class ImmersionFragmentActivity<V : ViewDataBinding> : PlanetActivity<V>() {

}