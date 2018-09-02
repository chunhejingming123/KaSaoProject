package com.kasao.qintaiframework.http

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/18 0018:09
 */

object Api {
    private val DEFAULT_TIMEOUT = 10L
    private var SERVICE: ApiService? = null
    @JvmStatic
    val default: ApiService
        get() {
            if (null == SERVICE) {
                val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                    //打印日志
                    Log.d("RetrofitLog", "retrofitBack = " + message)
                })
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                //创建一个okhttpclient
                val client = OkHttpClient.Builder()
                        .addInterceptor(loggingInterceptor)
                        .addInterceptor(RequestInterceptor())
                        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .build()
                SERVICE = Retrofit.Builder()
                        .client(client)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        // .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(UriConsts.BASE_URL)
                        .build()
                        .create(ApiService::class.java)
            }
            return SERVICE!!
        }
}