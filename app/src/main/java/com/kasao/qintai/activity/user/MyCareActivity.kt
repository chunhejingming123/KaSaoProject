package com.kasao.qintai.activity.user

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.kasao.qintai.R
import com.kasao.qintaiframework.base.BaseActivity

class MyCareActivity : BaseActivity() {
    override fun onLayoutLoad(): Int {
        return R.layout.activity_my_care
    }

    override fun findView() {
        findViewById<View>(R.id.viewBack).setOnClickListener { finish() }
        findViewById<ImageView>(R.id.ivBack).setImageResource(R.drawable.icon_return_black)
        findViewById<TextView>(R.id.tvTitle).text=getString(R.string.title_mycar)
    }
}
