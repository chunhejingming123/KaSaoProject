package com.kasao.qintai.adapter

import android.content.Context
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.kasao.qintai.R
import com.kasao.qintai.base.BaseKSadapter
import com.kasao.qintai.base.BaseKasaoApplication
import com.kasao.qintai.base.BaseViewHolder
import com.kasao.qintai.model.BrowseEntity
import com.kasao.qintai.model.CommentEntity
import com.kasao.qintai.model.SNSEntity
import com.kasao.qintai.model.ThumbModel
import com.kasao.qintai.model.domain.CommentLikedomain
import com.kasao.qintai.util.ContextComp
import com.kasao.qintai.util.DateUtil
import com.kasao.qintai.util.GlideUtil
import com.kasao.qintai.widget.CircleImageView
import com.kasao.qintaiframework.until.LogUtil
import java.util.*

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/28 0028:16
 */

class SocialDetailAdapter(context: Context, snn: SNSEntity) : BaseKSadapter<CommentEntity>() {
    var snsentity: SNSEntity? = null
    val mList = LinkedList<CommentEntity>()
    var mThubmble: List<ThumbModel>? = null
    var mBrowse: List<BrowseEntity>? = null
    var mContext: Context
    private var parentNick: String? = null
    private var parentId: String? = null
    private val mHandler: Handler
    private var count = 0
    private var temptZan = 0
    private var browseCount: Int = 0
    private val uid: String

    init {
        mContext = context
        snsentity = snn;
        uid = BaseKasaoApplication.getUserId()
        mHandler = Handler()
    }

    override fun getHeaderItemCount(): Int {
        return 1
    }

    override fun onBindHeaderItemViewHolder(headerViewHolder: BaseViewHolder<CommentEntity>?, position: Int) {
        headerViewHolder?.rendView()
    }

    override fun onCreateHeaderItemViewHolder(parent: ViewGroup?, headerViewType: Int): BaseViewHolder<CommentEntity>? {
        return HeadViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_head_socialdetail, parent, false))
    }

    override fun getContentItemCount(): Int {
        if (null == mList || mList.isEmpty()) {
            return 0
        } else {
            return mList.size
        }
    }

    override fun onBindContentItemViewHolder(contentViewHolder: BaseViewHolder<CommentEntity>?, position: Int) {
        contentViewHolder?.rendView(mList[position], position)
    }

    override fun onCreateContentItemViewHolder(parent: ViewGroup?, contentViewType: Int): BaseViewHolder<CommentEntity>? {
        return ContentVieHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_conter_socialdetail, parent, false))
    }

    inner class HeadViewHolder(itemView: View) : BaseViewHolder<CommentEntity>(itemView) {
        var avater: CircleImageView? = null
        internal var tvName: TextView? = null
        internal var tvTime: TextView? = null
        internal var tvConter: TextView? = null
        internal var tvCount: TextView? = null
        internal var tv_pinglunnumber: TextView? = null
        internal var tv_zannumber: TextView? = null
        internal var llavater: FrameLayout? = null
        internal var grideview: RecyclerView? = null
        internal var newCircle: CircleImageView? = null
        internal var viewHistory: View? = null

        init {
            avater = itemView.findViewById(R.id.iv_touxiang)
            tvName = itemView.findViewById(R.id.tv_name)
            tvTime = itemView.findViewById(R.id.tv_time)
            tvConter = itemView.findViewById(R.id.tv_titletalk)
            tvCount = itemView.findViewById(R.id.lookcount)
            tv_pinglunnumber = itemView.findViewById(R.id.tv_pinglunnumber)
            tv_zannumber = itemView.findViewById(R.id.tv_zannumber)
            llavater = itemView.findViewById(R.id.viewConter)
            grideview = itemView.findViewById(R.id.recycle_gride)
            newCircle = itemView.findViewById(R.id.avater)
            viewHistory = itemView.findViewById(R.id.ll_liulan)
        }

        override fun rendView() {
            if (null != snsentity) {
                GlideUtil.into(mContext, snsentity!!.u_img, avater, R.drawable.default_avater)
                tvName?.text = (snsentity!!.nackname)
                tvTime?.text = (snsentity!!.create_time)
                if (!TextUtils.isEmpty(snsentity!!.title)) {
                    tvConter?.visibility = (View.VISIBLE)
                    tvConter?.text = (snsentity!!.title)
                } else {
                    tvConter?.visibility = (View.GONE)
                }
                try {
                    avater?.setOnClickListener(this)
                    if (Integer.parseInt(snsentity!!.browse_count) > 0) {
                        browseCount = Integer.parseInt(snsentity!!.browse_count)
                        tvCount?.text = (snsentity?.browse_count)
                        if (llavater?.childCount!! > 0) {
                            llavater?.removeAllViews()
                        }
                        if (null != mBrowse) {
                            var size = mBrowse!!.size
                            if (size < 5) {
                            } else {
                                size = 4
                            }
                            for (i in 0..size) {
                                if (i == size) {
                                    val param = FrameLayout.LayoutParams(ContextComp.getDimensionPixelOffset(R.dimen.dimen_33), ContextComp.getDimensionPixelOffset(R.dimen.dimen_33))
                                    val headGroupView = LayoutInflater.from(mContext).inflate(R.layout.view_border_addtext, null)
                                    val view = headGroupView.findViewById<FrameLayout>(R.id.view1)
                                    view.setBackgroundResource(R.drawable.bg_circle_white)
                                    val tvss = headGroupView.findViewById<TextView>(R.id.tvCount)
                                    param.setMargins(i * ContextComp.getDimensionPixelOffset(R.dimen.dimen_15), 0, 0, 0)
                                    view.setLayoutParams(param)
                                    llavater?.addView(headGroupView)
                                    if (mBrowse!!.size > 99) {
                                        tvss.setText("+\n" + mBrowse!!.size)
                                    } else {
                                        tvss.setText("+" + mBrowse!!.size)
                                    }

                                } else {
                                    var param = FrameLayout.LayoutParams(ContextComp.getDimensionPixelOffset(R.dimen.dimen_33), ContextComp.getDimensionPixelOffset(R.dimen.dimen_33))
                                    val headGroupView = LayoutInflater.from(mContext).inflate(R.layout.view_border_avater, null)
                                    val view = headGroupView.findViewById<FrameLayout>(R.id.view1)
                                    view.setBackgroundResource(R.drawable.bg_circle_white)
                                    val avaters = headGroupView.findViewById<CircleImageView>(R.id.avater)
                                    param.setMargins(i * ContextComp.getDimensionPixelOffset(R.dimen.dimen_15), 0, 0, 0)
                                    view.setLayoutParams(param)
                                    llavater?.addView(headGroupView)
                                    GlideUtil.into(mContext, mBrowse!![i].user_img, avaters, R.drawable.default_avater)
                                }
                            }
                        }
                    } else {
                        browseCount = 1
                        GlideUtil.into(mContext, BaseKasaoApplication.getUserImg(), newCircle, R.drawable.default_avater)
                        tvCount?.text = ("+1")
                    }
                } catch (throwable: Throwable) {
                    LogUtil.e(throwable.message)
                }

                if (mThubmble != null) {
                    count = mThubmble!!.size
                }
                if (temptZan > 0) {
                    tv_zannumber?.text = (count + temptZan).toString()
                } else {
                    if (count >= 1) {
                        tv_zannumber?.text = (count + temptZan).toString()
                    } else {
                        tv_zannumber?.text = "0"
                    }
                }

                if (null != mList) {
                    tv_pinglunnumber?.text = mList.size.toString()
                }


                viewHistory?.setOnClickListener(View.OnClickListener {
                    if (null != mINewFriendOp) {
                        mINewFriendOp?.onHistory(snsentity!!.id, browseCount, count)
                    }
                })
            }
            val gide = GridLayoutManager(mContext, 3)
            grideview?.setLayoutManager(gide)
            grideview?.setHasFixedSize(true)
            if (null != snsentity && null != snsentity!!.img) {
                val madapter = GrideViewAdapter(mContext, snsentity!!.img)
                grideview?.setAdapter(madapter)
            }


        }

        override fun onClick(p0: View?) {
            mINewFriendOp?.onToUserDetail(snsentity!!.id)
        }
    }

    inner class ContentVieHolder(itemView: View) : BaseViewHolder<CommentEntity>(itemView) {
        var circleImageView: CircleImageView? = null
        var llcontetn: View? = null
        var tvName: TextView? = null
        var tvSay: TextView? = null
        var tvTime: TextView? = null
        var viewDelete: View? = null
        var coment: CommentEntity? = null
        var index = -1

        init {
            circleImageView = itemView.findViewById(R.id.iv_useravater)
            llcontetn = itemView.findViewById(R.id.view_contetn)
            tvName = itemView.findViewById(R.id.tv_name)
            tvSay = itemView.findViewById(R.id.tv_say)
            tvTime = itemView.findViewById(R.id.tv_time)
            viewDelete = itemView.findViewById(R.id.viewDelete)
        }

        override fun rendView(entity: CommentEntity, positon: Int) {
            this.coment = entity
            index = positon
            GlideUtil.into(mContext, entity.user_img, circleImageView, R.drawable.default_avater)
            circleImageView?.setOnClickListener(this)
            llcontetn?.setOnClickListener(this)
            viewDelete?.setOnClickListener(this)
            tvName?.text = (if (TextUtils.isEmpty(entity.nickname)) if (TextUtils.isEmpty(entity.user_mobile)) "匿名" else entity.user_mobile else entity.nickname)
            if (!TextUtils.isEmpty(entity.create_time)) {
                tvTime?.text = (DateUtil.formatTime("MM月dd日 HH:mm", java.lang.Long.valueOf(entity.create_time) * 1000))
            }
            if (entity.isComment()) {
                val rmname = if (TextUtils.isEmpty(entity.rm_id)) parentNick else entity.rm_id
                tvSay?.text = (Html.fromHtml("回复<font color=#0142a5>" + rmname + "</font>:" + entity.title))
            } else {
                parentNick = entity.nickname
                parentId = entity.id
                tvSay?.setText(entity.title)
            }
            if (uid == entity.u_id) {
                viewDelete?.visibility = (View.VISIBLE)
            } else {
                viewDelete?.visibility = (View.GONE)
            }
        }

        override fun onClick(view: View?) {
            when (view?.id) {
                R.id.view_contetn ->
                    mINewFriendOp?.setReply(coment!!, index)
                R.id.iv_useravater ->
                    mINewFriendOp?.onToUserDetail(coment!!.getU_id())
                R.id.viewDelete ->
                    if (null != mList && !mList.isEmpty()) {
                        mINewFriendOp?.onDelete(coment!!.id, index)

                    }
            }
        }
    }


    fun setCommentLikes(data: CommentLikedomain?) {
        if (data == null) {
            return
        }
     isOnlyLoadingOne=false
             if (null != data.likes) {
            mThubmble = data.likes
        }
        if (null != data.browse) {
            mBrowse = data.browse
        }
        if (null != data.data) {
            for (enty in data.data) {
                enty.type = 1
                mList.add(enty)
                if (!enty.child.isEmpty()) {
                    for (entity1 in enty.child) {
                        entity1.type = 2
                        mList.add(entity1)
                    }
                }
            }
            if (null == snsentity && null != data.detail) {
                snsentity = data.detail
                snsentity?.browse_count = data.browsecount
                snsentity?.likes_count = data.likescount
                snsentity?.review_count = mList.size.toString()
            }
        }
        notifyDataSetChanged()
    }

    //修改点赞数据
    fun resetZanNum(b: Int) {
        temptZan = b
        notifyItemChanged(0, snsentity)
    }

    // 发布评论
    fun setOneComment(oneComment: CommentEntity) {
        this.mList.add(oneComment)
        mHandler.postDelayed({ notifyItemChanged(0, snsentity) }, 100)
    }

    fun resetRepley(origin: List<CommentEntity>?) {
        if (null != origin) {
            mList.clear()
            for (enty in origin) {
                enty.type = 1
                mList.add(enty)
                if (null != enty.child && !enty.child.isEmpty()) {
                    for (entity1 in enty.child) {
                        entity1.type = 2
                        mList.add(entity1)
                    }
                }
            }
        }
        notifyDataSetChanged()
    }

    // 移除评论的内容
    fun removeContentItem(position: Int) {
        if (null != mList && !mList.isEmpty() && position < mList.size) {
            mList.removeAt(position)
            snsentity?.review_count = (mList.size.toString() + "")
            notifyContentItemRemoved(position)
            notifyItemChanged(0, snsentity)
            notifyContentItemRangeChanged(0, contentItemCount)
        }
    }

    private var mINewFriendOp: ISocialDeAction? = null

    fun setAction(mINewFriendOp: ISocialDeAction) {
        this.mINewFriendOp = mINewFriendOp
    }

    interface ISocialDeAction {
        fun setReply(comment: CommentEntity, position: Int)

        fun onToUserDetail(uid: String)

        fun onHistory(id: String, browseCount: Int, zanCount: Int)

        fun onDelete(id: String, position: Int)
    }
}