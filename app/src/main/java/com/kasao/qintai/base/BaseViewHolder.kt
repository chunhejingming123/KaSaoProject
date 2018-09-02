package com.kasao.qintai.base

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/21 0021:08
 */

open class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    open fun rendView() {}
    open fun rendView(t: T, position: Int) {}
    override fun onClick(p0: View?) {

    }
}