package com.kasao.qintai.fragment.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kasao.qintaiframework.base.BaseFragment
import com.kasao.qintai.R

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/20 0020:09
 */

class UserFragment:BaseFragment() {
    override fun onInflater(inflater: LayoutInflater, container: ViewGroup?): View {
     return inflater.inflate(R.layout.fragment_user,container,false)
    }
}