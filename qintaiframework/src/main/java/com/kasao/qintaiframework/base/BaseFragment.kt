package com.kasao.qintaiframework.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
}