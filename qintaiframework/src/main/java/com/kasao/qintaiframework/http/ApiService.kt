package com.kasao.qintaiframework.http

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.QueryMap
import retrofit2.http.Url

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/18 0018:
 * 网络请求接口
 */

interface ApiService {
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST
    fun postGetObservable(@Url requesBaseUrl: String, @QueryMap params: Map<String, String>): Observable<ResponseBody>

}