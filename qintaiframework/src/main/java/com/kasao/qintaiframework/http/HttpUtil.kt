package com.kasao.qintaiframework.http

import android.util.Log
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/18 0018:09
 */

class HttpUtil private constructor() {
    companion object {
        @JvmStatic
        val instance: HttpUtil by lazy {
            HttpUtil()
        }
    }

    private val mApiService: ApiService = Api.default

    //    fun getRequest(url:String,map:HashMap<String,String>,request:HttpSimpleString):Observable<Any>{
//
//        return
//    }
    fun postRequest(url: String, map: Map<String, String>, mHttpRespnse: HttpRespnse) {
        var observer = mApiService.postGetObservable(url, map)
        observer.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ResponseBody> {
                    override fun onComplete() {
                        mHttpRespnse._onComplete()
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: ResponseBody) {
                        mHttpRespnse._onNext(t)
                    }

                    override fun onError(e: Throwable) {
                        mHttpRespnse._onError(e)
                    }
                })

    }
}