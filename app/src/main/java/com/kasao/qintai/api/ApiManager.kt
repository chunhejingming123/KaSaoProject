package com.kasao.qintai.api

import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.http.HttpUtil

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/18 0018:09
 */

class ApiManager private constructor() {

    companion object {
        val getInstance = Helper.instance
    }

    private object Helper {
        val instance = ApiManager()
    }

    fun login(url: String, map: Map<String, String>, mHttpResponse: HttpRespnse) {
        HttpUtil.instance.postRequest(url, map, mHttpResponse)
    }

}