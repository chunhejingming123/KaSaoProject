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
import com.kasao.qintai.model.CarDetailEntity
import com.kasao.qintai.util.GlideUtil

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/22 0022:14
 */

class CarSystemAdapter : BaseKSadapter<CarDetailEntity>() {
    private var list: List<CarDetailEntity>? = null
    val FOOTDIS = 1
    val FOOTNULL = 2
    override fun getContentItemCount(): Int {
        return if (list == null || list?.isEmpty()!!) 0 else list?.size!!
    }

    override fun onBindContentItemViewHolder(contentViewHolder: BaseViewHolder<CarDetailEntity>?, position: Int) {
        contentViewHolder?.rendView(list!!.get(position), position)
    }

    override fun onCreateContentItemViewHolder(parent: ViewGroup?, contentViewType: Int): BaseViewHolder<CarDetailEntity>? {
        return ContentViewHolde(LayoutInflater.from(parent?.context).inflate(R.layout.item_car_system, parent, false)!!, mICarSeries!!)
    }

    override fun getFooterItemCount(): Int {
        return 1
    }

    override fun getFooterItemViewType(position: Int): Int {
        if (null == list || list!!.isEmpty()) {
            return FOOTNULL
        }
        return FOOTDIS
    }

    override fun onCreateFooterItemViewHolder(parent: ViewGroup?, footerViewType: Int): BaseViewHolder<CarDetailEntity>? {
        when (footerViewType) {
            FOOTDIS -> return BaseViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_distance_view, parent, false)!!)
            FOOTNULL -> return BaseViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_tip_nodata_view, parent, false)!!)
        }
        return null
    }

    class ContentViewHolde(item: View, mlistener: ICarSeries) : BaseViewHolder<CarDetailEntity>(item) {
        var img: ImageView? = null
        var tvName: TextView? = null
        var tvPrice: TextView? = null
        var viewRoot: View? = null
        var listener: ICarSeries? = null
        var ids: String? = null

        init {
            tvName = item.findViewById(R.id.tvDes)
            img = item.findViewById(R.id.ivImg)
            tvPrice = item.findViewById(R.id.tvprice)
            viewRoot = item.findViewById(R.id.itemRoot)
            listener = mlistener
        }

        override fun rendView(t: CarDetailEntity, position: Int) {
            ids = t.goods_id
            tvName?.text = t.name
            if (TextUtils.isEmpty(t.priceblock) ||
                    t.priceblock.equals("0") || t.priceblock.equals("0.0") || t.priceblock.equals("0.00")) {
                tvPrice?.text = "暂无报价"
            } else {
                tvPrice?.text = t?.priceblock + "万"
            }
            GlideUtil.into(viewRoot?.context, t?.goods_img, img, R.drawable.bg_default)

            viewRoot?.setOnClickListener {
                listener?.onCarSerise(ids!!)
            }
        }

    }

    fun setList(list: List<CarDetailEntity>) {
        isOnlyLoadingOne=false
        this.list = list
        notifyDataSetChanged()
    }

    private var mICarSeries: ICarSeries? = null

    fun setmICarSeries(mICarSeries: ICarSeries) {
        this.mICarSeries = mICarSeries
    }

    interface ICarSeries {
        fun onCarSerise(seriesId: String)
    }
}