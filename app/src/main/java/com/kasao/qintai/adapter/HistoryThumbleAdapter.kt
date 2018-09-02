package com.kasao.qintai.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kasao.qintai.base.BaseKSadapter
import com.kasao.qintai.base.BaseViewHolder
import com.kasao.qintai.model.ThumbModel
import java.util.ArrayList

import com.kasao.qintai.R
import com.kasao.qintai.util.DateUtil
import com.kasao.qintai.util.GlideUtil
import com.kasao.qintai.widget.CircleImageView

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/29 0029:11
 */

class HistoryThumbleAdapter : BaseKSadapter<ThumbModel>() {

    private var thumble: MutableList<ThumbModel>? = null

    override fun getContentItemCount(): Int {
        if (null == thumble || thumble!!.isEmpty()) {
            return 0
        }
        return thumble!!.size
    }

    override fun onBindContentItemViewHolder(contentViewHolder: BaseViewHolder<ThumbModel>?, position: Int) {
        if (null != thumble) {
            contentViewHolder?.rendView(thumble!![position], position)
        }
    }

    override fun onCreateContentItemViewHolder(parent: ViewGroup?, contentViewType: Int): BaseViewHolder<ThumbModel>? {
        return ContenterViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_browse_view, parent, false))
    }

    inner class ContenterViewHolder(itemView: View) : BaseViewHolder<ThumbModel>(itemView) {

        val circle_avater: CircleImageView
        val tvName: TextView
        val tvTime: TextView

        init {
            circle_avater = itemView.findViewById(R.id.circle_avater)
            tvName = itemView.findViewById(R.id.tv_name) as TextView
            tvTime = itemView.findViewById(R.id.tv_time) as TextView

        }

        override fun rendView(entity: ThumbModel, position: Int) {
            if (null != entity) {

                GlideUtil.into(itemView.context, entity.user_img, circle_avater, R.drawable.default_avater)
                tvName.text = if (TextUtils.isEmpty(entity.nickname)) "这个人很懒 啥都没留下" else entity.nickname
                tvTime?.text = (DateUtil.formatTime("MM月dd日 HH:mm", java.lang.Long.valueOf(entity.create_time) * 1000) + " 赞过")
                itemView.setOnClickListener {
                    mUserDetail!!.toUserDetail(entity.id)
                }
            }
        }
    }

    fun setDatathumble(lists: List<ThumbModel>?) {

        if (null != lists) {
            isOnlyLoadingOne=false
            if (null == thumble) {
                thumble = ArrayList<ThumbModel>()
            }
            val start = thumble!!.size
            val len = lists.size
            thumble!!.addAll(lists)
            notifyItemRangeChanged(start, len)
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