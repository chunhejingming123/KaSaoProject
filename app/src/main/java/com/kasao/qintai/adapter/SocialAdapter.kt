package com.kasao.qintai.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.kasao.qintai.R
import com.kasao.qintai.base.BaseKSadapter
import com.kasao.qintai.base.BaseViewHolder
import com.kasao.qintai.model.SNSEntity
import com.kasao.qintai.util.DateUtil
import com.kasao.qintai.util.GlideUtil
import com.kasao.qintaiframework.base.MyApplication
import java.util.*

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/27 0027:08
 */

class SocialAdapter : BaseKSadapter<SNSEntity>() {
    var mList: MutableList<SNSEntity>? = null
    var TEXT_IMG = 1// 有图片item
    var TEXT = 2// 没有图片
    var tag = ""
    override fun getHeaderItemCount(): Int {
        isEmptyState = mList==null||mList?.size==0
        return super.getHeaderItemCount()
    }
    override fun getContentItemCount(): Int {
        if (null == mList || mList!!.isEmpty()) {
            return 0
        } else {
            return mList!!.size
        }
    }

    override fun getContentItemViewType(position: Int): Int {
        if (null != mList && TextUtils.isEmpty(mList!!.get(position).img)) {
            return TEXT
        } else {
            return TEXT_IMG
        }
    }

    override fun onBindContentItemViewHolder(contentViewHolder: BaseViewHolder<SNSEntity>?, position: Int) {
        if (null != mList) {
            contentViewHolder?.rendView(mList!![position], position)
        }
    }

    override fun onCreateContentItemViewHolder(parent: ViewGroup?, contentViewType: Int): BaseViewHolder<SNSEntity>? {
        when (contentViewType) {
            TEXT -> return ContentViewHolderText(LayoutInflater.from(parent?.context).inflate(R.layout.item_mycomment_text, parent, false))
            TEXT_IMG -> return ContentViewHolderImg(LayoutInflater.from(parent?.context).inflate(R.layout.item_mycomment_conter, parent, false))
        }
        return null

    }

    inner class ContentViewHolderText(itemView: View) : BaseViewHolder<SNSEntity>(itemView) {
        var tvYear: TextView? = null
        var tvMonth: TextView? = null
        var tvContent: TextView? = null
        var mEntity: SNSEntity? = null
        var index: Int = 0

        init {
            tvYear = itemView.findViewById(R.id.tv_year)
            tvMonth = itemView.findViewById(R.id.tv_month)
            tvContent = itemView.findViewById(R.id.tvContent)
            itemView?.setOnClickListener(this)

        }

        override fun rendView(entity: SNSEntity, position: Int) {
            if (null != entity) {
                mEntity = entity
                index = position

                if (tag == DateUtil.getDataNoPoint(java.lang.Long.parseLong(entity.new_create_time) * 1000)) {
                    tvMonth?.text = ""
                    tvYear?.text = ""
                } else {
                    if (!TextUtils.isEmpty(entity.new_create_time) && DateUtil.isInToday(java.lang.Long.parseLong(entity.new_create_time) * 1000)) {
                        tvMonth?.text = "今天"
                        tvYear?.text = ""
                    } else {
                        if (!TextUtils.isEmpty(entity.new_create_time)) {
                            tvYear?.text = (DateUtil.getDataNoPoint(java.lang.Long.parseLong(entity.new_create_time) * 1000).substring(0, 4))
                            tvMonth?.text = (DateUtil.getLongPointDate(java.lang.Long.parseLong(entity.new_create_time) * 1000).substring(5))
                        }
                    }
                }
                tag = DateUtil.getDataNoPoint(java.lang.Long.parseLong(entity.new_create_time) * 1000)
                if (!TextUtils.isEmpty(entity.title)) {
                    tvContent?.text = (entity.title.trim())
                }
            }
        }

        override fun onClick(p0: View?) {
            mAction?.toDetail(mEntity!!, index)
        }
    }

    inner class ContentViewHolderImg(itemView: View) : BaseViewHolder<SNSEntity>(itemView) {
        var tvYear: TextView? = null
        var tvMonth: TextView? = null
        var tvContent: TextView? = null
        var iv01: ImageView? = null
        var IVO2: ImageView? = null
        var mEntity: SNSEntity? = null
        var index: Int = 0

        init {
            tvYear = itemView.findViewById(R.id.tv_year)
            tvMonth = itemView.findViewById(R.id.tv_month)
            tvContent = itemView.findViewById(R.id.tvContent)
            iv01 = itemView.findViewById(R.id.iv01)
            IVO2 = itemView.findViewById(R.id.iv02)
            itemView.setOnClickListener(this)
        }

        override fun rendView(entity: SNSEntity, position: Int) {
            if (null != entity) {
                mEntity = entity
                index = position
                if (tag == DateUtil.getDataNoPoint(java.lang.Long.parseLong(entity.new_create_time) * 1000)) {
                    tvMonth?.text = ""
                    tvYear?.text = ""
                } else {
                    if (!TextUtils.isEmpty(entity.new_create_time) && DateUtil.isInToday(java.lang.Long.parseLong(entity.new_create_time) * 1000)) {
                        tvMonth?.text = "今天"
                        tvYear?.text = ""
                    } else {
                        if (!TextUtils.isEmpty(entity.new_create_time)) {
                            tvYear?.text = (DateUtil.getDataNoPoint(java.lang.Long.parseLong(entity.new_create_time) * 1000).substring(0, 4))
                            tvMonth?.text = (DateUtil.getLongPointDate(java.lang.Long.parseLong(entity.new_create_time) * 1000).substring(5))
                        }
                    }

                }
                tag = DateUtil.getDataNoPoint(java.lang.Long.parseLong(entity.new_create_time) * 1000)
                if (!TextUtils.isEmpty(entity.title)) {
                    tvContent?.text = entity.title
                }

                if (!TextUtils.isEmpty(entity.img)) {
                    val pics = entity.img.split(",")
                    var sList: MutableList<String> = ArrayList<String>(pics.size)
                    for (imgs in pics) {
                        sList.add(imgs)
                    }
                    if (sList.size == 1) {
                        GlideUtil.into(MyApplication.applicaton, sList[0], iv01, R.drawable.bg_default)
                        iv01?.visibility = View.VISIBLE
                        IVO2?.visibility = View.GONE
                    } else if (sList.size >= 2) {
                        GlideUtil.into(MyApplication.applicaton, sList[0], iv01, R.drawable.bg_default)
                        GlideUtil.into(MyApplication.applicaton, sList[1], IVO2, R.drawable.bg_default)
                        iv01?.visibility = View.VISIBLE
                        IVO2?.visibility = View.VISIBLE
                    }
                }
            }
        }

        override fun onClick(p0: View?) {
            mAction?.toDetail(mEntity!!, index)
        }
    }

    fun setRemove(index: Int) {
        if (null == mList) {
            return
        }
        if (index < mList!!.size) {
            mList!!.removeAt(index)
            if (mList!!.isEmpty()) {
                mList = null
                notifyDataSetChanged()
            } else {
                notifyContentItemRemoved(index)
            }
        }

    }

    fun setCommentList(data: List<SNSEntity>?) {
        isOnlyLoadingOne=false
        if (null == mList) {
            mList = ArrayList()
        }
        var start = mList?.size!!
        var len = data?.size!!
        mList?.addAll(data!!)
        notifyContentItemRangeInserted(start, len)
    }

    var mAction: ISocialAction? = null
    fun setScoicalAction(mSocialAction: ISocialAction) {
        mAction = mSocialAction
    }

    interface ISocialAction {
        fun toDetail(entity: SNSEntity, index: Int)
    }
}