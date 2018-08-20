package com.kasao.qintai.activity.login

import android.content.Intent
import android.widget.TextView
import com.kasao.qintai.MainActivity
import com.kasao.qintai.R
import com.kasao.qintaiframework.base.BaseActivity

class SplashActivity : BaseActivity() {
    var tx: TextView? = null
    override fun onLayoutLoad(): Int {
        return R.layout.activity_splash
    }

    override fun findView() {
        tx = findViewById(R.id.tv)
        tx?.setOnClickListener {
            var intent = Intent()
            intent.setClass(SplashActivity@ this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}