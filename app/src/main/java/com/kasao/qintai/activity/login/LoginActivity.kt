package com.kasao.qintai.activity.login

import android.content.Intent
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.kasao.qintai.R
import com.kasao.qintai.api.ApiInterface
import com.kasao.qintai.api.ApiManager
import com.kasao.qintai.base.BaseKasaoApplication
import com.kasao.qintai.model.domain.UserDomain
import com.kasao.qintai.util.SharedPreferencesHelper
import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.until.GsonUtil
import com.kasao.qintaiframework.until.ScreenUtil
import com.kasao.qintaiframework.until.ToastUtil
import okhttp3.ResponseBody

class LoginActivity : BaseLoginActivity() {
    var etvalidataCode: EditText? = null
    var ettel1: EditText? = null
    var etpwd: EditText? = null
    var tvForget: TextView? = null
    var tvRegister: TextView? = null
    var tvChose: TextView? = null
    var btnLogin: Button? = null
    var viewQuick: View? = null
    var viewAcount: View? = null

    var quickLogin: Boolean = true
    var telnum: String = ""
    override fun onLayoutLoad(): Int {
        return R.layout.activity_login
    }

    override fun findView() {
        findBaseView()
        etvalidataCode = findViewById(R.id.etCode)
        ettel1 = findViewById(R.id.etTel1)
        etpwd = findViewById(R.id.etPwd)
        tvForget = findViewById(R.id.tvForget)
        tvRegister = findViewById(R.id.tvRegister)
        etpwd?.transformationMethod = PasswordTransformationMethod.getInstance()
        tvChose = findViewById(R.id.tvChang)
        btnLogin = findViewById(R.id.btnLogin)
        viewAcount = findViewById(R.id.viewCount)
        viewQuick = findViewById(R.id.viewQuick)
        ScreenUtil.initScreen(this)
      //  ObjectAnimator.ofFloat(viewAcount, "translationX", 0f, -ScreenUtil.getScreenW().toFloat()).setDuration(200).start()
        layoutChange()
        rendView()
    }

    override fun rendView() {
        tvForget?.setOnClickListener {
            etvalidataCode?.setText("")
            etpwd?.setText("")
            startActivity(Intent(this@LoginActivity, ForgetPwdActivity::class.java))
        }
        tvRegister?.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
        btnCode?.setOnClickListener {
            getValidateCod("quicklogin")
        }
//        tvChose?.setOnClickListener {
//            if (quickLogin) {
//                tvChose?.text = getString(R.string.tiplogincount)
//                ObjectAnimator.ofFloat(viewQuick, "translationX", 0f, -ScreenUtil.getScreenW().toFloat()).setDuration(200).start()
//                ObjectAnimator.ofFloat(viewAcount, "translationX", ScreenUtil.getScreenW().toFloat(), 0f).setDuration(200).start()
//            } else {
//                tvChose?.text = getString(R.string.tiplogin)
//                ObjectAnimator.ofFloat(viewQuick, "translationX", ScreenUtil.getScreenW().toFloat(), 0f).setDuration(200).start()
//                ObjectAnimator.ofFloat(viewAcount, "translationX", 0f, -ScreenUtil.getScreenW().toFloat()).setDuration(200).start()
//
//            }
//            quickLogin = !quickLogin
//        }
        btnLogin?.setOnClickListener {
            var url: String
            var map: HashMap<String, String>? = null
            if (quickLogin) {
                url = ApiInterface.VERIFICATLOGIN
                map = HashMap()
                telnum = etTel?.text.toString()
                if (telnum.isNullOrEmpty() || telnum.length != 11) {
                    ToastUtil.showAlter(getString(R.string.input_moble))
                    return@setOnClickListener
                }
                map.put("phone", telnum)
                map["type"] = "quicklogin"
                var code = etvalidataCode?.text.toString()
                if (code.isNullOrEmpty()) {
                    ToastUtil.showAlter(getString(R.string.input_validatacode))
                    return@setOnClickListener
                }
                map["code"] = code
            } else {
                url = ApiInterface.LOGIN
                map = HashMap()
                telnum = ettel1?.text.toString()
                if (telnum.isNullOrEmpty() || telnum.length != 11) {
                    ToastUtil.showAlter(getString(R.string.input_moble))
                    return@setOnClickListener
                }
                var pas = etpwd?.text.toString()
                if (pas.isNullOrEmpty() || pas.length < 6) {
                    ToastUtil.showAlter(getString(R.string.pwdless6))
                    return@setOnClickListener
                }
                map["login_name"] = telnum
                map["password"] = pas
            }
            login(url, map)
        }
    }


    fun login(url: String, map: HashMap<String, String>) {
        if (url.isNullOrEmpty() || null == map) {
            return
        }
        ApiManager.getInstance.loadDataByParmars(url, map, object : HttpRespnse {
            override fun _onComplete() {

            }

            override fun _onNext(t: ResponseBody) {
                var domain = GsonUtil.GsonToBean(t.string(), UserDomain::class.java)
                if (null != domain && null != domain.data) {
                    BaseKasaoApplication.setUser(domain.data)
                    SharedPreferencesHelper.getInstance(applicationContext).putObject(domain.data)
                  //  startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
                ToastUtil.showAlter(domain?.msg)

            }

            override fun _onError(e: Throwable) {
            }
        })
    }
}
