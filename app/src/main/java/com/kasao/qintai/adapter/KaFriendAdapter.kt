package com.kasao.qintai.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.kasao.qintai.base.BaseKSadapter
import com.kasao.qintai.base.BaseViewHolder
import com.kasao.qintai.model.SNSEntity
import com.kasao.qintai.R
import com.kasao.qintai.model.domain.Zandomain
import com.kasao.qintai.util.ContextComp
import com.kasao.qintai.util.GlideUtil
import com.kasao.qintai.widget.CircleImageView
import com.kasao.qintaiframework.base.MyApplication
import com.kasao.qintaiframework.until.LogUtil
import java.util.ArrayList

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/28 0028:10
 */

class KaFriendAdapter(content: Context) : BaseKSadapter<SNSEntity>() {
    private val mContext: Context=content
    var mlist: MutableList<SNSEntity>? = null
    override fun getHeaderItemCount(): Int {
        return 0
    }

    override fun getContentItemCount(): Int {
        if (null == mlist || mlist!!.isEmpty()) {
            return 0
        }
        return mlist!!.size
    }

    override fun onBindContentItemViewHolder(contentViewHolder: BaseViewHolder<SNSEntity>?, position: Int) {
        if (null != mlist) {
            contentViewHolder?.rendView(mlist!![position], position)
        }
    }

    override fun onCreateContentItemViewHolder(parent: ViewGroup?, contentViewType: Int): BaseViewHolder<SNSEntity>? {
        return ContenerViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_kafriend_view, parent, false))
    }

    override fun getFooterItemCount(): Int {
        if (mlist == null) {
            return 1
        } else {
            return 0
        }
    }

    inner class ContenerViewHolder(itemView: View) : BaseViewHolder<SNSEntity>(itemView) {
        var picture: CircleImageView
        var name: TextView
        var content: TextView
        var time: TextView
        var comment: TextView
        var praise: TextView
        var liulangcishu: TextView
        var mGridView: RecyclerView
        var viewReport: View

        init {
            picture = itemView.findViewById(R.id.sns_item_picture)
            name = itemView.findViewById(R.id.sns_item_name)
            content = itemView.findViewById(R.id.sns_item_content)
            time = itemView.findViewById(R.id.sns_item_time)
            comment = itemView.findViewById(R.id.sns_item_comment)
            praise = itemView.findViewById(R.id.sns_item_praise)
            mGridView = itemView.findViewById(R.id.sns_item_gridView)
            viewReport = itemView.findViewById(R.id.ivReport)
            val gide = GridLayoutManager(MyApplication.applicaton, 3)
            mGridView.layoutManager = gide
            mGridView.setHasFixedSize(true)
            liulangcishu = itemView.findViewById(R.id.liulangcishu)
        }

        override fun rendView(entity: SNSEntity, positions: Int) {
            if (entity.u_img != null && entity.u_img.length > 5) {
                GlideUtil.into(itemView.context, entity.u_img, picture, R.drawable.default_avater)
            } else {
                picture.setImageResource(R.drawable.default_avater)
            }
            picture.setOnClickListener {
                if (null != onFriendAction) {
                    onFriendAction?.userDetail(entity)
                }
            }
            try {
                val idCardReplaceWithStar = idCardReplaceWithStar(entity.nackname)
                if(idCardReplaceWithStar.isNullOrEmpty()){
                    name.text="匿名"
                }
                else{
                    name.text=(idCardReplaceWithStar)
                }

                liulangcishu?.text=(entity.browse_count + "")
                if (TextUtils.isEmpty(entity.title)) {
                    content.visibility = View.GONE
                } else {
                    content.visibility = View.VISIBLE
                    content?.text=(entity.title)
                }
                time?.text=(entity.create_time)
                if (entity.review_count=="0") {
                    comment.text = "评论"
                } else {
                    comment?.text=(entity.review_count)
                }
                val drawable: Drawable
                if (entity.type==("2")) {
                    drawable = ContextComp.getDrawable(R.drawable.icon_thumb_normal)
                    praise.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                } else {
                    drawable = ContextComp.getDrawable(R.drawable.icon_thumb_press)
                    praise.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                }
                if (entity.likes_count=="0" || TextUtils.isEmpty(entity.likes_count)) {
                    praise.text = "赞"
                } else {
                    praise?.text=entity.likes_count
                }
                praise.setOnClickListener {
                    if (null != onFriendAction) {
                        onFriendAction?.setThumble(entity, positions)
                    }
                    val count = entity.likes_count
                    var num = Integer.parseInt(count)
                    if ("1" == entity.type) {
                        entity.type = "2"
                        praise.setCompoundDrawablesWithIntrinsicBounds(ContextComp.getDrawable(R.drawable.icon_thumb_normal), null, null, null)
                        if (num > 0) {
                            num = num - 1
                        }
                    } else {
                        entity.type = "1"
                        praise.setCompoundDrawablesWithIntrinsicBounds(ContextComp.getDrawable(R.drawable.icon_thumb_press), null, null, null)
                        num = num + 1
                    }
                    if (num > 0) {
                        praise.text = num.toString() + ""
                    } else {
                        praise.text = "赞"
                    }
                }
                //九宫格图片处
                if (TextUtils.isEmpty(entity.img)) {
                    mGridView.visibility = View.GONE
                } else {
                    mGridView.visibility = View.VISIBLE
                    if (null != entity && null != entity.img) {
                        val adapter = GrideViewAdapter(mContext, entity.img)
                        mGridView.adapter = adapter
                    }
                }

                itemView.setOnClickListener {
                    if (null != onFriendAction) {
                        onFriendAction?.ontoFriendDetail(entity, positions)
                    }
                }
                mGridView.setOnClickListener {
                    if (null != onFriendAction) {
                        onFriendAction?.ontoFriendDetail(entity, positions)
                    }
                }
                viewReport.setOnClickListener {
                    if (null != onFriendAction) {
                        onFriendAction?.rePort(entity, positions)
                    }
                }
            } catch (e: Exception) {
           LogUtil.e("------="+e.message)
            }


        }
    }

    fun idCardReplaceWithStar(idCard: String?): String? {
        return if (idCard == null||idCard!!.isEmpty() ) {
            null
        } else {
            replaceAction(idCard, "(?<=\\d{3})\\d(?=\\d{4})")
        }
    }

    private fun replaceAction(username: String, regular: String): String {
        return username.replace(regular.toRegex(), "*")
    }

    fun setData(data: List<SNSEntity>?) {
        if (null == data || data.isEmpty()) {
            return
        }
        isOnlyLoadingOne = false
        if (mlist == null) {
            mlist = ArrayList()
        }
        val start = mlist!!.size
        val len = data.size
        mlist!!.addAll(data)
        notifyContentItemRangeInserted(start, len)
    }

    private var onFriendAction: IOnFriendAction? = null

    fun setOnFriendAction(onFriendAction: IOnFriendAction) {
        this.onFriendAction = onFriendAction
    }

    fun setFresh(fresh: List<SNSEntity>?, mHandler: Handler) {
        if (null != mHandler && fresh != null && !fresh.isEmpty()) {
            mHandler.postDelayed(Runnable {
                if (mlist != null) {
                    mlist!!.clear()
                    mlist!!.addAll(fresh)
                    notifyDataSetChanged()
                }
            }, 150)
        } else {

        }
    }

    fun notifyChange(domain: Zandomain?, index: Int) {
        if (null == domain) {
            return
        }
        mlist?.get(index)?.likes_count = domain!!.count
        mlist?.get(index)?.type = (domain!!.type)
    }

    fun notifyRemove(index: Int) {
        if (index > -1) {
            notifyContentItemRemoved(index)
            mlist?.removeAt(index)
        }
    }

    interface IOnFriendAction {
        fun ontoFriendDetail(entity: SNSEntity, positon: Int)

        fun setThumble(snsentity: SNSEntity, position: Int)

        fun setComment(snsentity: SNSEntity, position: Int)

        fun userDetail(snsentity: SNSEntity)

        fun rePort(snsentity: SNSEntity, positon: Int)
    }
}