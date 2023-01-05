package com.planet.core

@Suppress("unused")
open class PlanetConstant {

    /**
     * 隐私政策html路径
     */
    val userPrivacy = "file:///android_asset/user_privacy.html"

    /**
     * 三方sdk隐私政策html路径
     */
    val thirdPrivacy = "file:///android_asset/third_privacy.html"

    /**
     * 三方sdk列表html路径
     */
    val thirdSdk = "file:///android_asset/third_sdk.html"

    /**
     * 用户服务协议html路径
     */
    val userAgreement = "file:///android_asset/user_agreement.html"

    /**
     * 友盟appId
     */
    val umAppId = "6258ee7b30a4f67780a3fa46"

    /**
     * 是否同意用户协议 ture or false ,存在sp中
     */
    val isAgreeAgreement = "agree_agreement"

    /**
     * http请求中header值得前缀
     */
    val tokenPrefix = "Bearer "

    /**
     * 微信开放平台 app id
     */
    val appId = "wx38a3baff490550c8"//正式版
    val appIdDebug = "wxff90dd84889c9ce1"//测试版

    companion object {
        /**
         * api路径
         */
        const val API_PREFIX = "/api/v1/"
    }

}