package com.kasao.qintaiframework.http

import okhttp3.ResponseBody

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/18 0018:14
 */

interface HttpRespnse {
    fun _onComplete()

    fun _onNext(t: ResponseBody)

    fun _onError(e: Throwable)
}