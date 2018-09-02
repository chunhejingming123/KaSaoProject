package com.kasao.qintai.activity.login

import android.content.Intent
import com.kasao.qintai.MainActivity
import com.kasao.qintai.R
import com.kasao.qintai.activity.active.CarActionActivity
import com.kasao.qintai.util.ParmarsValue
import com.kasao.qintai.util.SharedPreferencesHelper
import com.kasao.qintaiframework.base.BaseActivity
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity() {
    override fun onLayoutLoad(): Int {
        return R.layout.activity_splash
    }

    override fun findView() {
        Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Long> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(value: Long?) {

                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {
                        rendView()
                    }
                })

    }

    override fun rendView() {
        var isGuide = SharedPreferencesHelper.getInstance(this).getSharedPreference(ParmarsValue.KEY_GUIDE, false) as Boolean
        if (isGuide) {
            startActivity(Intent(SplashActivity@ this, MainActivity::class.java))
        } else {
            startActivity(Intent(SplashActivity@ this, GuideActivity::class.java))
        }
        finish()
    }
}