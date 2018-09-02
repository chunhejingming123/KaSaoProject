package com.kasao.qintaiframework.http

import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/18 0018:
 * 网络请求接口
 */

interface ApiService {
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST
    fun postGetObservable(@Url requesBaseUrl: String, @QueryMap params: Map<String, String>): Observable<ResponseBody>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST
    fun postGetObservable(@Url requesBaseUrl: String): Observable<ResponseBody>

    @Multipart
    @POST
    fun upLoadData(@Url url: String, @QueryMap params: Map<String, String>, @PartMap partMap: HashMap<String, RequestBody>?): Observable<ResponseBody>


    @Multipart
    @POST
    fun upLoadData(@Url url: String,  @PartMap partMap: HashMap<String, RequestBody>?): Observable<ResponseBody>



    @GET
    fun getObservable(@Url requesBaseUrl: String): Observable<ResponseBody>

    @GET
    fun getObservable(@Url requesBaseUrl: String, @QueryMap map: Map<String, String>): Observable<ResponseBody>


}