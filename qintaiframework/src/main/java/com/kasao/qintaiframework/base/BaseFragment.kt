package com.kasao.qintaiframework.base

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kasao.qintaiframework.view.MyLinearLayoutManager
import com.qintai.framework.base.ImplementUi
import com.trello.rxlifecycle2.components.support.RxFragment

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/20 0020:08
 */

abstract class BaseFragment : Fragment(), ImplementUi {
    var rootView: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        init()
        if (null == rootView) {
            rootView = onInflater(inflater, container)
            findView()
            onloadData()
        }
        return rootView
    }

    abstract fun onInflater(inflater: LayoutInflater, container: ViewGroup?): View

    override fun init() {

    }

    override fun findView() {

    }

    override fun rendView() {
    }

    override fun onloadData() {
    }

    var llm: MyLinearLayoutManager? = null

    fun initRecycle(recycleView: RecyclerView, listener: RecyclerView.OnScrollListener?) {
        llm = MyLinearLayoutManager(MyApplication.applicaton)
        llm?.setOrientation(LinearLayoutManager.VERTICAL)
        recycleView.layoutManager = llm
        // 设置ItemAnimator
        recycleView.itemAnimator = DefaultItemAnimator()
        // 设置固定大小
        recycleView.setHasFixedSize(true)
        if (null != listener) {
            recycleView.addOnScrollListener(listener)
        }
    }

    fun initRecycle(recycleView: RecyclerView, listener: RecyclerView.OnScrollListener?, ll: LinearLayoutManager) {
        recycleView.layoutManager = ll
        // 设置ItemAnimator
        recycleView.itemAnimator = DefaultItemAnimator()
        // 设置固定大小
        recycleView.setHasFixedSize(true)
        if (null != listener) {
            recycleView.addOnScrollListener(listener)
        }
    }

    protected fun startActivity(descActivity: Class<*>, bundle: Bundle?) {
        val intent = Intent()
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        intent.setClass(this.activity, descActivity)
        this.startActivity(intent)
    }
}