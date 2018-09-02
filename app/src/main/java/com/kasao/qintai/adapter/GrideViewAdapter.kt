package com.kasao.qintai.adapter

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.kasao.qintai.R
import com.kasao.qintai.activity.ImageActivity
import com.kasao.qintai.base.BaseKSadapter
import com.kasao.qintai.base.BaseViewHolder
import com.kasao.qintai.util.GlideUtil
import com.kasao.qintaiframework.until.ToastUtil
import java.util.*

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/28 0028:11
 */

class GrideViewAdapter(mcontext: Context, imgs: String) : BaseKSadapter<String>() {

    var widthPixels: Float
    var heightPixels: Int
    private var mList: MutableList<String>? = null
    var context: Context
    var origin: String

    init {
        isOnlyLoadingOne = false
        context = mcontext
        origin = imgs

        if (!TextUtils.isEmpty(imgs)) {
            origin = imgs
            val pics = imgs.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            mList = ArrayList<String>(pics.size)
            for (uid in pics) {
                mList?.add(uid)
            }
            mList = Arrays.asList(*pics)
        }
        val displayMetrics = context.getResources().getDisplayMetrics()
        widthPixels = (displayMetrics.widthPixels - context.getResources().getDimension(R.dimen.dimen_36)) / 3
        heightPixels = displayMetrics.widthPixels / 3 + context.getResources().getDimensionPixelOffset(R.dimen.dimen_10)
    }

    override fun getHeaderItemCount(): Int {
        if (null == mList || mList!!.isEmpty()) {
            return 0
        } else {
            return mList!!.size
        }
    }

    override fun onBindHeaderItemViewHolder(headerViewHolder: BaseViewHolder<String>?, position: Int) {
        if (null != mList) {
            headerViewHolder?.rendView(mList!!.get(position), position)
        }
    }

    override fun onCreateHeaderItemViewHolder(parent: ViewGroup?, headerViewType: Int): BaseViewHolder<String>? {
        return HeadViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_girde_view, parent, false))
    }

    inner class HeadViewHolder(itemView: View) : BaseViewHolder<String>(itemView) {
        var iv: ImageView

        init {
            iv = itemView.findViewById(R.id.iv_gride_view)
            iv.layoutParams = LinearLayout.LayoutParams(widthPixels.toInt(), heightPixels)
        }

        override fun rendView(t: String, position: Int) {
            GlideUtil.into(itemView.context, t, iv, R.drawable.bg_default)
            iv.setOnClickListener {
                var intent = Intent(context, ImageActivity::class.java)
                intent.putExtra("picurl", origin)
                intent.putExtra("position", position)
                context.startActivity(intent)
            }
        }
    }
}