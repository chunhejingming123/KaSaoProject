package com.kasao.qintai.activity.login

import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.kasao.qintai.R
import com.kasao.qintai.api.ApiInterface
import com.kasao.qintai.api.ApiManager
import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.until.ToastUtil
import okhttp3.ResponseBody

class RegisterActivity : BaseLoginActivity() {
    var etPwd: EditText? = null
    var etCode: EditText? = null
    var btnRegister: Button? = null

    override fun onLayoutLoad(): Int {
        return R.layout.activity_register
    }

    override fun findView() {
        findBaseView()
        etPwd = findViewById(R.id.etPwd)
        etCode = findViewById(R.id.etCode)
        etPwd?.transformationMethod = PasswordTransformationMethod.getInstance()
        btnRegister = findViewById(R.id.btnRegister)
        layoutChange()
        rendView()
    }

    override fun rendView() {
        findViewById<View>(R.id.tvlogin).setOnClickListener { finish() }
        btnCode?.setOnClickListener {
            getValidateCod("")
        }

        btnRegister?.setOnClickListener {
            var telNum = etTel?.text.toString()
            if (telNum.isNullOrEmpty() || telNum.length != 11) {
                ToastUtil.showAlter(getString(R.string.input_moble))
                return@setOnClickListener
            }
            var pass = etPwd?.text.toString()
            if (pass.isNullOrEmpty() || pass.length < 6) {
                ToastUtil.showAlter(getString(R.string.pwdless6))
                return@setOnClickListener
            }
            var code = etCode?.text.toString()
            if (code.isNullOrEmpty()) {
                ToastUtil.showAlter(getString(R.string.input_validatacode))
            }
            var map = HashMap<String, String>()
            ApiManager.getInstance.getDataByParmars(ApiInterface.AGAIST, map, object : HttpRespnse {
                override fun _onComplete() {
                    finish()
                }

                override fun _onNext(t: ResponseBody) {

                }

                override fun _onError(e: Throwable) {
                }
            })
        }
    }

}
