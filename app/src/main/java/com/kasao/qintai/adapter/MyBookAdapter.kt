package com.kasao.qintai.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kasao.qintai.base.BaseKSadapter
import com.kasao.qintai.base.BaseViewHolder
import com.kasao.qintai.model.OderCode

import com.kasao.qintai.R
import com.kasao.qintai.util.ContextComp
import com.kasao.qintai.util.DataTypeChange

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/26 0026:15
 */

class MyBookAdapter : BaseKSadapter<OderCode>() {
    var lists: MutableList<OderCode>? = null
        set(value) {
            isOnlyLoadingOne = false
            field = value
            notifyDataSetChanged()
        }
    override fun getHeaderItemCount(): Int {
        isEmptyState = lists==null||lists?.size==0
        return super.getHeaderItemCount()
    }
    override fun getContentItemCount(): Int {
        if (null == lists || lists!!.isEmpty()) {
            return 0
        } else {
            return lists!!.size
        }
    }

    override fun onCreateContentItemViewHolder(parent: ViewGroup?, contentViewType: Int): BaseViewHolder<OderCode>? {
        return ContentViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_mybook_view, parent, false))
    }

    override fun onBindContentItemViewHolder(contentViewHolder: BaseViewHolder<OderCode>?, position: Int) {
        if (null != lists) {
            contentViewHolder!!.rendView(lists!!.get(position), position)
        }
    }

    class ContentViewHolder(itemView: View) : BaseViewHolder<OderCode>(itemView) {
        var tvPrice: TextView
        var tvNum: TextView
        var tvDes: TextView
        var ivBg: View
        var tvNo: TextView
        var unite: TextView

        init {
            tvPrice = itemView.findViewById(R.id.tvPrice)
            tvNo = itemView.findViewById(R.id.tvNo)
            tvNum = itemView.findViewById(R.id.tvNum)
            tvDes = itemView.findViewById(R.id.tvDes)
            ivBg = itemView.findViewById(R.id.viewBg)
            unite = itemView.findViewById(R.id.unite)
        }

        override fun rendView(o: OderCode, position: Int) {
            if (null != o) {
                if (o.is_user.equals("1")) {
                    ivBg.setBackgroundResource(R.drawable.car_coupon)
                    tvNo.setTextColor(ContextComp.getColor(R.color.color_ff2a49))
                } else {
                    ivBg.setBackgroundResource(R.drawable.car_coupon_used)
                    tvNo.setTextColor(ContextComp.getColor(R.color.color_909090))
                }
                var num = 1
                try {
                    if (!TextUtils.isEmpty(o.goods_num)) {
                        num = Integer.parseInt(o.goods_num)
                    }
                } catch (e: Throwable) {

                }

                val money = DataTypeChange.getF10000(DataTypeChange.stringToDounble(o.goods_price) * num)
                if (TextUtils.isEmpty(money) || "0" == money || "0.00" == money || "0.0" == money) {
                    tvPrice.text = "暂无报价"
                    tvPrice.setTextColor(ContextComp.getColor(R.color.color_909090))
                    unite.visibility = View.GONE
                } else {
                    tvPrice.text = (money)
                    tvPrice.setTextColor(ContextComp.getColor(R.color.color_ff2a49))
                    unite.visibility = View.VISIBLE
                }
                tvNum.text = (o.goods_num)
                tvDes.text = (o.name + " " + o.goods_jingle)
                tvNo.text = (o.promo_code)
            }

        }
    }
}