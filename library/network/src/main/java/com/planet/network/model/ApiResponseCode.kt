package com.planet.network.model

sealed class ApiResponseCode {

    protected abstract val code: Int
    protected abstract val name:String

    /**
     * 接口请求成功
     */
    object Success : ApiResponseCode() {
        public override val code: Int
            get() = 0
        override val name: String
            get() = "接口请求成功"

    }

    /**
     * token过期
     */
    object TokenExpire : ApiResponseCode() {
        public override val code: Int
            get() = 1005
        override val name: String
            get() = "Token过期"
    }

    /**
     * 登录失效，需重新登录
     */
    object LoginExpire : ApiResponseCode() {
        public override val code: Int
            get() = 1009
        override val name: String
            get() = "登录过期"
    }

}