package com.kasao.qintai.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kasao.qintai.R
import com.kasao.qintai.base.BaseKSadapter
import com.kasao.qintai.base.BaseViewHolder
import com.kasao.qintai.model.CarParmeterKeyValue

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/23 0023:17
 */

class SimpleAdapter : BaseKSadapter<CarParmeterKeyValue>() {
    var mlist: List<CarParmeterKeyValue>? = null
        set(value) {
            isOnlyLoadingOne=false
            field = value
            notifyDataSetChanged()
        }
    val ITEM_TYPE_TITLE = 99
    val ITEM_TYPE_CONTENT = 101
    var type = 99
    var selectedIndex = 0
    override fun getHeaderItemCount(): Int {
        isEmptyState=(mlist==null||mlist?.size==0)
        return super.getHeaderItemCount()
    }
    override fun getContentItemCount(): Int {
        return if (mlist == null) 0 else mlist!!.size
    }

    override fun getContentItemViewType(position: Int): Int {
        return if (null != mlist && mlist!!.get(position).index > 0) {
            ITEM_TYPE_TITLE
        } else {
            ITEM_TYPE_CONTENT
        }
    }

    override fun onBindContentItemViewHolder(contentViewHolder: BaseViewHolder<CarParmeterKeyValue>?, position: Int) {
        contentViewHolder?.rendView(mlist!!.get(position), position)
    }

    override fun onCreateContentItemViewHolder(parent: ViewGroup?, contentViewType: Int): BaseViewHolder<CarParmeterKeyValue>? {
        when (contentViewType) {
            ITEM_TYPE_TITLE -> return SimpleOneViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_simple_one_textview, parent, false))
            ITEM_TYPE_CONTENT -> return SimpleTwoViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_simple_textview, parent, false))
        }
        return null
    }

    class SimpleOneViewHolder(item: View) : BaseViewHolder<CarParmeterKeyValue>(item) {
        private var tvTitle: TextView

        init {
            tvTitle = item.findViewById(R.id.tvTitle)
        }

        override fun rendView(t: CarParmeterKeyValue, position: Int) {
            tvTitle.text = t.name
        }
    }

    class SimpleTwoViewHolder(item: View) : BaseViewHolder<CarParmeterKeyValue>(item) {
        private var tvTitle: TextView
        private var tvParmars: TextView

        init {
            tvTitle = item.findViewById(R.id.tvTitle) as TextView
            tvParmars = item.findViewById(R.id.tv_parmars) as TextView
        }

        override fun rendView(t: CarParmeterKeyValue, position: Int) {
            tvTitle.setText(t.name + ":")
            tvParmars.text = if (TextUtils.isEmpty(t.aliasvalue)) "--" else t.aliasvalue
        }
    }
}