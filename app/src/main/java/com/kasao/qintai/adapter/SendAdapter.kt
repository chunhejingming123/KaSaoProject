package com.kasao.qintai.adapter

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.kasao.qintai.R
import com.kasao.qintai.activity.ImageGridActivity
import com.kasao.qintai.base.BaseKSadapter
import com.kasao.qintai.base.BaseViewHolder
import com.kasao.qintai.util.GlideUtil
import com.kasao.qintai.util.ImageTools
import com.kasao.qintai.widget.photo.BitmapBucket
import com.kasao.qintaiframework.until.ToastUtil

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/31 0031:14
 */

class SendAdapter(context: Activity, lists: MutableList<String>) : BaseKSadapter<String>() {
    private var mContext: Activity
    var mList: MutableList<String>? = null
    init {
        isOnlyLoadingOne=false
        isEmptyState=false
        mContext = context
        mList = lists
    }

    override fun getContentItemCount(): Int {
        if (null == mList) {
            return 1
        } else {
            return if (mList!!.size == 9) mList!!.size else mList!!.size + 1
        }
    }

    override fun onBindContentItemViewHolder(contentViewHolder: BaseViewHolder<String>?, position: Int) {
        contentViewHolder?.rendView("", position)
    }

    override fun onCreateContentItemViewHolder(parent: ViewGroup?, contentViewType: Int): BaseViewHolder<String>? {
        return ContentViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_sendsocial_img, parent, false))
    }

    fun setDatalist(lists: MutableList<String>) {
        this.mList = lists
        notifyDataSetChanged()
    }

    inner class ContentViewHolder(itemView: View) : BaseViewHolder<String>(itemView) {
        var iv: ImageView
        var ivDel: ImageView
        init {
            iv = itemView.findViewById(R.id.ivImg)
            ivDel = itemView.findViewById(R.id.ivDel)
        }
        override fun rendView(t: String, position: Int) {
            if (null != mList) {
                if (mList!!.size != 9 && position == mList!!.size) {
                    iv.isClickable = true
                    ivDel.visibility = View.GONE
                    iv.setImageResource(R.drawable.btn_addimage)
                    iv.setOnClickListener {
                        val pm = mContext.packageManager
                        val permission = PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.READ_EXTERNAL_STORAGE", "com.kasao.qintai")
                        if (permission) {
                            BitmapBucket.max = 9 - mList!!.size
                            val intent = Intent(mContext, ImageGridActivity::class.java)
                            mContext.startActivityForResult(intent, 5)
                        } else {
                            ToastUtil.showAlter("请打开读取照片权限")
                        }
                    }
                } else {
                    iv.isClickable = false
                    var getimage = ImageTools.getimage(mList!![position])
                    if (null == getimage) {
                        GlideUtil.into(mContext, mList!![position], iv, R.drawable.bg_default)
                    } else {
                        iv.setImageBitmap(getimage)
                    }
                    ivDel.visibility = View.VISIBLE
                    ivDel.setOnClickListener {
                        mList!!.removeAt(position)
                        notifyContentItemRemoved(position)
                    }
                }
            }
        }
    }
}