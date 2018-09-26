package com.kasao.qintai.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kasao.qintai.base.BaseKSadapter
import com.kasao.qintai.base.BaseViewHolder
import com.kasao.qintai.model.BrowseEntity

import com.kasao.qintai.R
import com.kasao.qintai.util.DateUtil
import com.kasao.qintai.util.GlideUtil
import com.kasao.qintai.widget.CircleImageView
import java.util.ArrayList

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/29 0029:11
 */

class HistoryBrowseAdapter : BaseKSadapter<BrowseEntity>() {

    private var alllistbrowse: MutableList<BrowseEntity>? = null
    override fun getHeaderItemCount(): Int {
        isEmptyState = (null == alllistbrowse || alllistbrowse!!.isEmpty())
        return super.getHeaderItemCount()
    }

    override fun getContentItemCount(): Int {
        if (null == alllistbrowse || alllistbrowse!!.isEmpty()) {
            return 0
        }
        return alllistbrowse!!.size
    }

    override fun onBindContentItemViewHolder(contentViewHolder: BaseViewHolder<BrowseEntity>?, position: Int) {
        if (null != alllistbrowse) {
            contentViewHolder?.rendView(alllistbrowse!![position], position)
        }
    }

    override fun onCreateContentItemViewHolder(parent: ViewGroup?, contentViewType: Int): BaseViewHolder<BrowseEntity>? {
        return Contener(LayoutInflater.from(parent?.context).inflate(R.layout.item_browse_view, parent, false))
    }

    inner class Contener(itemView: View) : BaseViewHolder<BrowseEntity>(itemView) {
        var circle_avater: CircleImageView
        var tvName: TextView
        var tvTime: TextView

        init {
            circle_avater = itemView.findViewById(R.id.circle_avater)
            tvName = itemView.findViewById(R.id.tv_name)
            tvTime = itemView.findViewById(R.id.tv_time)
        }

        override fun rendView(entity: BrowseEntity, position: Int) {
            if (null != entity) {
                GlideUtil.into(itemView.context, entity.user_img, circle_avater, R.drawable.default_avater)
                tvName.text = if (TextUtils.isEmpty(entity.nickname)) "这个人很懒 啥都没留下" else entity.nickname
                tvTime?.text = (DateUtil.formatTime("MM月dd日 HH:mm", java.lang.Long.valueOf(entity.browsetime) * 1000) + " 来过")
                itemView.setOnClickListener {
                    mUserDetail?.toUserDetail(entity.u_id)
                }
            }
        }
    }

    // 设置数据
    fun setDatabrowse(lists: List<BrowseEntity>?) {
        isOnlyLoadingOne = false
        if (null != lists) {
            if (null == alllistbrowse) {
                alllistbrowse = ArrayList()
            }
            val start = alllistbrowse!!.size
            val len = lists.size
            alllistbrowse!!.addAll(lists)
            notifyItemRangeChanged(start, len)
        }
        if (null == alllistbrowse || alllistbrowse!!.isEmpty()) {
            setNullData()
        }
    }

    private var mUserDetail: IUserDetail? = null

    fun setUserDetail(detail: IUserDetail) {
        this.mUserDetail = detail
    }

    interface IUserDetail {
        fun toUserDetail(uid: String)
    }
}