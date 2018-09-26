package com.kasao.qintai.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kasao.qintai.R
import com.kasao.qintai.base.BaseKSadapter
import com.kasao.qintai.base.BaseViewHolder
import com.kasao.qintai.model.NoticeEntity

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/26 0026:11
 */

class NoticeAdapter : BaseKSadapter<NoticeEntity>() {
    var mutableList: MutableList<NoticeEntity>? = null
        set(value) {
            isOnlyLoadingOne=false
            field = value
            notifyDataSetChanged()
        }
    override fun getHeaderItemCount(): Int {
        isEmptyState = mutableList==null||mutableList?.size==0
        return super.getHeaderItemCount()
    }
    override fun getContentItemCount(): Int {
        if (null == mutableList || mutableList!!.isEmpty())
            return 0
        else
            return mutableList!!.size
    }

    override fun onBindContentItemViewHolder(contentViewHolder: BaseViewHolder<NoticeEntity>?, position: Int) {
        if (null != mutableList) {
            contentViewHolder?.rendView(mutableList!![position], position)
        }
    }

    override fun onCreateContentItemViewHolder(parent: ViewGroup?, contentViewType: Int): BaseViewHolder<NoticeEntity>? {
        return ContentViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_notice_view, parent, false), mAction!!)
    }

    class ContentViewHolder(itemView: View, mAction: INoticeAction) : BaseViewHolder<NoticeEntity>(itemView) {
        var tv_nickname: TextView
        var listener: INoticeAction
        var entity: NoticeEntity? = null

        init {
            tv_nickname = itemView.findViewById(R.id.tv_nickname) as TextView
            listener = mAction
            itemView.setOnClickListener(this)
        }

        override fun rendView(t: NoticeEntity, position: Int) {
            entity = t
            tv_nickname.text = t?.title
        }

        override fun onClick(p0: View?) {
            listener?.goToDetal(entity!!)
        }
    }

    var mAction: INoticeAction? = null
    fun setNoticrAction(listener: INoticeAction) {
        this.mAction = listener
    }

    interface INoticeAction {
        fun goToDetal(entity: NoticeEntity)
    }
}
