package com.kasao.qintaiframework.base

import android.Manifest
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import com.kasao.qintaiframework.until.ActivityManager
import com.qintai.framework.base.ImplementUi
import android.Manifest.permission
import android.Manifest.permission.CALL_PHONE
import android.app.Activity
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.os.Build
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import com.kasao.qintaiframework.until.ToastUtil
import com.kasao.qintaiframework.view.MyLinearLayoutManager


abstract class BaseActivity : FragmentActivity(), ImplementUi {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setContentView(onLayoutLoad())
        findView()
        ActivityManager.registerActivity(this)
        onloadData()

    }

    /**
     * 加载布局文件
     */
    abstract fun onLayoutLoad(): Int

    override fun init() {
    }

    override fun findView() {
    }

    override fun rendView() {

    }

    override fun onloadData() {

    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityManager.destoryActivity(this)
    }

    val REQUEST_CODE_ASK_CALL_PHONE = 123
    val perms = arrayOf(Manifest.permission.CALL_PHONE)
    fun callPerssion(mActivity: Activity, phone: String) {
        if (Build.VERSION.SDK_INT >= 23) {
            val checkCallPhonePermission = ContextCompat.checkSelfPermission(mActivity,
                    Manifest.permission.CALL_PHONE)
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mActivity, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CODE_ASK_CALL_PHONE)
                return
            } else {
                call(mActivity, phone)
            }
        } else {
            call(mActivity, phone)
        }
    }

    fun call(mActivity: Activity, phone: String) {
        if (TextUtils.isEmpty(phone) || phone.length < 7) {
            ToastUtil.showAlter("服务忙,请稍后联系")
            return
        }
        val intent = Intent()
        intent.action = Intent.ACTION_DIAL
        intent.data = Uri.parse("tel:$phone")
        mActivity.startActivity(intent)
    }

    // 设置RecycleView
     public var llm: LinearLayoutManager? = null
    // 线性
    fun initRecycle(recycleView: RecyclerView, listener: RecyclerView.OnScrollListener?) {
        llm = MyLinearLayoutManager(MyApplication.applicaton)
        recycleView.layoutManager = llm
        // 设置ItemAnimator
        recycleView.itemAnimator = DefaultItemAnimator()
        // 设置固定大小
        recycleView.setHasFixedSize(true)
        recycleView.isNestedScrollingEnabled = false
        if (null != listener) {
            recycleView.addOnScrollListener(listener)
        }
    }


    protected fun startActivity(descActivity: Class<*>, bundle: Bundle?) {
        val intent = Intent()
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        intent.setClass(this, descActivity)
        this.startActivity(intent)
    }

}