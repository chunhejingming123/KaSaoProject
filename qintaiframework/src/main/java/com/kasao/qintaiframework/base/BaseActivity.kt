package com.kasao.qintaiframework.base

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import com.kasao.qintaiframework.until.ActivityManager
import com.qintai.framework.base.ImplementUi

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
}