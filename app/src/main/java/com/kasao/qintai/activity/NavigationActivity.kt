package com.kasao.qintai.activity

import android.content.Intent
import android.net.Uri
import com.kasao.qintai.MainActivity
import com.kasao.qintai.activity.kayou.SocialDetailsActivity
import com.kasao.qintai.activity.login.SplashActivity
import com.kasao.qintai.activity.main.CarDetailActivity
import com.kasao.qintai.activity.user.NoticeActivity
import com.kasao.qintai.base.BaseKasaoApplication
import com.kasao.qintaiframework.activity.WebActivity
import com.kasao.qintaiframework.base.BaseActivity

/**
 * 作者 :created  by suochunming
 * 日期：2018/9/9 0009:11
 */

class NavigationActivity : BaseActivity() {
    override fun onLayoutLoad(): Int {
        return 0
    }

    override fun findView() {
        val uri = intent.data
        var user = BaseKasaoApplication.getUser()
        if (null == user) {
            startActivity(Intent(this@NavigationActivity, MainActivity::class.java))
        } else {
            var intent = Intent(this, getStartActivity())
            intent.data = uri
            startActivity(intent)
        }

        finish()
    }

    private fun getStartActivity(): Class<*> {
        var start: Class<*>? = null
        val uri = intent.data
        if (uri != null) {
            start = getStartActivity(uri)
        }
        if (start == null) {
            start = SplashActivity::class.java
        }
        return start
    }

    fun getStartActivity(uri: Uri): Class<*>? {
        var start: Class<*>? = null
        val host = uri.host
        //是否登录测试
        //url=feel://notification_center?tab=x
        //            if ("notification_center".equals(host)) {//通知中心
        //                String tab = uri.getQueryParameter("tab");
        //
        //            }
        if ("cardetail" == host) {//汽车详情
            start = CarDetailActivity::class.java
        } else if ("newsdetail" == host) { //资讯详情
            start = WebActivity::class.java
        } else if ("frienddetail" == host) {//煤客圈详情
            start = SocialDetailsActivity::class.java
        } else if ("reportmsg" == host) {
            start = MainActivity::class.java// 主页面
        }
        return start
    }
}