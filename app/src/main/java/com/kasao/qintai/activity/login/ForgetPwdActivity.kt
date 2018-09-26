package com.kasao.qintai.activity.login

import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import com.kasao.qintai.R
import com.kasao.qintai.api.ApiInterface
import com.kasao.qintai.api.ApiManager
import com.kasao.qintai.model.RtnSuss
import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.until.GsonUtil
import com.kasao.qintaiframework.until.ToastUtil
import okhttp3.ResponseBody

class ForgetPwdActivity : BaseLoginActivity() {
    var etPwd: EditText? = null
    var etPwd2: EditText? = null
    var etCode: EditText? = null
    var btnConfirm: Button? = null

    override fun onLayoutLoad(): Int {
        return R.layout.activity_forget_pwd
    }

    override fun findView() {
        findBaseView()
        etPwd = findViewById(R.id.etPwd)
        etPwd2 = findViewById(R.id.etPwd2)
        etPwd?.transformationMethod = PasswordTransformationMethod.getInstance()
        etPwd2?.transformationMethod = PasswordTransformationMethod.getInstance()

        etCode = findViewById(R.id.etCode)
        btnConfirm = findViewById(R.id.btnConfirm)
        layoutChange()
        rendView()
    }

    override fun rendView() {
        btnCode?.setOnClickListener {
            getValidateCod("")
        }
        btnConfirm?.setOnClickListener {
            var telNum = etTel?.text.toString()
            if (telNum.isNullOrEmpty() || telNum.length != 11) {
                ToastUtil.showAlter(getString(R.string.input_moble))
                return@setOnClickListener
            }
            var pwd1 = etPwd?.text.toString()
            if (pwd1.isNullOrEmpty() || pwd1.length < 6) {
                ToastUtil.showAlter(getString(R.string.pwdless6))
                return@setOnClickListener
            }
            var pwd2 = etPwd2?.text.toString()
            if (pwd2.isNullOrEmpty()) {
                ToastUtil.showAlter(getString(R.string.input_againpwd))
                return@setOnClickListener
            }
            if (pwd1 == pwd2) {
            } else {
                ToastUtil.showAlter(getString(R.string.tip_pwdnosame))
                return@setOnClickListener
            }
            var codes = etCode?.text.toString()
            if (codes.isNullOrEmpty()) {
                ToastUtil.showAlter(getString(R.string.input_validatacode))
                return@setOnClickListener
            }
            val params = HashMap<String, String>()
            params["mobile"] = telNum
            params["type"] = "2"
            params["password"] = codes

            ApiManager.getInstance.getDataByParmars(ApiInterface.FORGETPASSWORD, params, object : HttpRespnse {
                override fun _onComplete() {
                    finish()
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


}
