package com.kasao.qintai.activity.shop

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.kasao.qintai.R
import com.kasao.qintai.util.ParmarsValue
import com.kasao.qintaiframework.base.BaseActivity

class BookSuccessActivity : BaseActivity() {
    private var code: String? = null
    private var tvCode: TextView? = null
    var phoneNum: String = ""
    override fun onLayoutLoad(): Int {
        return R.layout.activity_book_success

    }

    override fun findView() {
        code = intent.extras.getString(ParmarsValue.KEY_STR)
        phoneNum = intent.extras.getString(ParmarsValue.KEY_CID)

        tvCode = findViewById(R.id.tvCode)
        findViewById<View>(R.id.viewBack).setOnClickListener { finish() }
        findViewById<View>(R.id.viewCall).setOnClickListener { callPerssion(this, phoneNum) }
        findViewById<View>(R.id.viewShop).setOnClickListener { startActivity(ShopListActivity::class.java, null) }
        rendView()
    }

    override fun rendView() {

        if (!TextUtils.isEmpty(code)) {
            tvCode?.text = code
        }
    }

}
