package com.kasao.qintai.base

import android.view.ViewGroup
import com.kasao.qintai.widget.LodingStateView
import com.kasao.qintaiframework.base.HeaderFooterRecyclerViewAdapter

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/21 0021:09
 */

open class BaseKSadapter<T> : HeaderFooterRecyclerViewAdapter<BaseViewHolder<T>>() {
    var isOnlyLoadingOne: Boolean = true
    val LOADING_STATE = 101
    val NODATA_STATE = 102
    override fun getHeaderItemCount(): Int {
        return 0
    }

    override fun getFooterItemCount(): Int {
        return if (isOnlyLoadingOne) 1 else 0
    }

    override fun getFooterItemViewType(position: Int): Int {
        return if (isOnlyLoadingOne) {
            LOADING_STATE
        } else {
            NODATA_STATE
        }
    }

    override fun getContentItemCount(): Int {
        return 0
    }

    override fun onCreateHeaderItemViewHolder(parent: ViewGroup?, headerViewType: Int): BaseViewHolder<T>? {
        return null
    }

    override fun onCreateFooterItemViewHolder(parent: ViewGroup?, footerViewType: Int): BaseViewHolder<T>? {
        when (footerViewType) {
            LOADING_STATE -> return BaseViewHolder(LodingStateView(parent?.context))
            NODATA_STATE -> return null
        }
        return null
    }

    override fun onCreateContentItemViewHolder(parent: ViewGroup?, contentViewType: Int): BaseViewHolder<T>? {
        return null
    }

    override fun onBindHeaderItemViewHolder(headerViewHolder: BaseViewHolder<T>?, position: Int) {

    }

    override fun onBindFooterItemViewHolder(footerViewHolder: BaseViewHolder<T>?, position: Int) {

    }

    override fun onBindContentItemViewHolder(contentViewHolder: BaseViewHolder<T>?, position: Int) {

    }


}