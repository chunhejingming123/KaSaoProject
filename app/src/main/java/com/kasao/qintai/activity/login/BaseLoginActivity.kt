package com.kasao.qintai.activity.login

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import com.kasao.qintai.R
import com.kasao.qintai.api.ApiInterface
import com.kasao.qintai.api.ApiManager
import com.kasao.qintai.model.RtnSuss
import com.kasao.qintai.util.SoftKeybordUtil
import com.kasao.qintaiframework.base.BaseActivity
import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.until.GsonUtil
import com.kasao.qintaiframework.until.ToastUtil
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import okhttp3.ResponseBody
import java.util.concurrent.TimeUnit

abstract class BaseLoginActivity : BaseActivity() {
    var viewTop: View? = null
    var scrollview: ScrollView? = null
    var btnCode: Button? = null
    var etTel: EditText? = null

    fun findBaseView() {
        etTel = findViewById(R.id.etTel)
        btnCode = findViewById(R.id.btnCode)
        viewTop = findViewById(R.id.viewTop)
        scrollview = findViewById(R.id.scrollview)
    }

    fun layoutChange() {
        SoftKeybordUtil.setSoftKeyboardListener(this, object : SoftKeybordUtil.OnSoftKeyboardChangeListener {
            override fun onSoftKeyBoardChange(softKeybardHeight: Int, isVisible: Boolean) {
                if (isVisible) {
                    scrollview?.scrollTo(0, viewTop!!.top)
                } else {
                     scrollview?.scrollTo(0, 0)
                }
            }
        })
    }

    fun getValidateCod(type: String) {
        var telNum = etTel?.text.toString()
        if (telNum.isNullOrEmpty() || telNum.length != 11) {
            ToastUtil.showAlter(getString(R.string.input_moble))
            return
        }
        startTimmer()
        getCode(type)
    }


    //倒计时
    fun startTimmer() {
        var count = 59L
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(object : Function<Long, Long> {
                    override fun apply(t: Long): Long {
                        return (count - t)
                    }
                })
                .take(count + 1)
                .subscribe(object : Observer<Long> {
                    override fun onComplete() {
                        btnCode?.isEnabled = true
                        btnCode?.text = getString(R.string.sendvalidate)
                        btnCode?.setBackgroundResource(R.drawable.bg_border2_validate)
                    }

                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onNext(t: Long?) {
                        btnCode?.text = "${t}后重发"
                        btnCode?.isEnabled = false
                        btnCode?.setBackgroundColor(resources.getColor(R.color.gray2))

                    }

                    override fun onError(e: Throwable?) {
                    }
                })
    }

    // 获取验证码
    fun getCode(type: String) {
        var map = HashMap<String, String>()
        map["phone"] = etTel?.text.toString()
        map["type"] = type
        ApiManager.getInstance.loadDataByParmars(ApiInterface.VERIFICATIONCODE, map, object : HttpRespnse {
            override fun _onComplete() {

            }

            override fun _onNext(t: ResponseBody) {
                var rtn = GsonUtil.GsonToBean(t.string(), RtnSuss::class.java)
                if (null != rtn) {
                    ToastUtil.showAlter(rtn.msg)
                }
            }

            override fun _onError(e: Throwable) {
            }
        })

    }

}
