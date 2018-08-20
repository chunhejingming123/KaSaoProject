package com.kasao.qintaiframework.http

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/18 0018:09
 */

class RequestInterceptor :Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain?): Response {
val original=chain?.request()
        val request=original?.newBuilder()
                ?.header("Cache-Control","no-cache")
                ?.method(original.method(),original.body())
                ?.build()
        return  chain!!.proceed(request)
    }
}