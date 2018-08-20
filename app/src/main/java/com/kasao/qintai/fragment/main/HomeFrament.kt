package com.kasao.qintai.fragment.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kasao.qintai.R
import com.kasao.qintaiframework.base.BaseFragment

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/20 0020:09
 */

class HomeFrament : BaseFragment() {
    override fun onInflater(inflater: LayoutInflater, container: ViewGroup?): View {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)
        return rootView!!
    }

}