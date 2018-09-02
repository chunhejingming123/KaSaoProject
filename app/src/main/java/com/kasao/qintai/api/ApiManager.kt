package com.kasao.qintai.api

import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.http.HttpUtil
import okhttp3.RequestBody

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

    fun loadDataByParmars(url: String, map: Map<String, String>, mHttpResponse: HttpRespnse) {
        HttpUtil.instance.postRequest(url, map, mHttpResponse)
    }

    fun getDataByParmars(url: String, map: Map<String, String>, mHttpResponse: HttpRespnse) {
        HttpUtil.instance.getRequest(url, map, mHttpResponse)
    }


    fun getDataByUrl(url: String, mHttpResponse: HttpRespnse) {
        HttpUtil.instance.getRequest(url, mHttpResponse)
    }
    fun upLoadData(url: String,strmap:Map<String,String>, map: HashMap<String, RequestBody>?, mHttpResponse: HttpRespnse){
        HttpUtil.instance.upLoadData(url,strmap, map,mHttpResponse)
    }
    fun upLoadData(url: String,map: HashMap<String, RequestBody>?,mHttpResponse: HttpRespnse){
        HttpUtil.instance.upLoadData(url, map,mHttpResponse)
    }
}