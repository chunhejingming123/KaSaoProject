package com.kasao.qintai.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.kasao.qintai.R
import com.kasao.qintai.base.BaseKSadapter
import com.kasao.qintai.base.BaseKasaoApplication
import com.kasao.qintai.base.BaseViewHolder
import com.kasao.qintai.model.CarBrand
import com.kasao.qintai.util.GlideUtil
import com.kasao.qintaiframework.base.MyApplication
import com.kasao.qintaiframework.until.ScreenUtil

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/24 0024:15
 */

class CarBrandGridAdapter : BaseKSadapter<CarBrand>() {
    init {
        isOnlyLoadingOne=false
    }
    var listBrand: MutableList<CarBrand>? = null

    fun setListBrands(listBrand: MutableList<CarBrand>) {
        this.listBrand = listBrand
        notifyDataSetChanged()
    }

    override fun getContentItemCount(): Int {
        if (null == listBrand || listBrand!!.isEmpty()) {
            return 0
        } else {
            return listBrand?.size!!
        }
    }

    override fun onBindContentItemViewHolder(contentViewHolder: BaseViewHolder<CarBrand>?, position: Int) {
        if (null != listBrand) {
            contentViewHolder?.rendView(listBrand!!.get(position), position)
        }
    }

    override fun onCreateContentItemViewHolder(parent: ViewGroup?, contentViewType: Int): BaseViewHolder<CarBrand>? {
        return ContentViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_carsell_hotbrand, parent, false), mChoseBrand!!)
    }

    class ContentViewHolder(item: View, choseBrand: IChoseBrand) : BaseViewHolder<CarBrand>(item), View.OnClickListener {
        var ivBrand: ImageView
        var tvBrandName: TextView
        var mCarBrand: CarBrand? = null
        var listener: IChoseBrand

        init {
            val item = itemView.layoutParams as RecyclerView.LayoutParams
            item.width = ScreenUtil.getScreenW() / 4
            itemView.layoutParams = item
            ivBrand = itemView.findViewById(R.id.iv_car_brand) as ImageView
            tvBrandName = itemView.findViewById(R.id.tv_car_name) as TextView

            ivBrand.layoutParams
            val params = ivBrand.layoutParams
            params.width = (0.9 * ScreenUtil.getScreenW() / 4).toInt()
            params.height = (0.55 * ScreenUtil.getScreenW() / 4).toInt()
            ivBrand.layoutParams = params
            ivBrand.requestLayout()
            ivBrand.scaleType = ImageView.ScaleType.FIT_CENTER

            itemView.setOnClickListener(this)
            listener = choseBrand
        }

        override fun rendView(t: CarBrand, position: Int) {
            this.mCarBrand = t
            GlideUtil.into(MyApplication.applicaton, t.brand_img, ivBrand, R.drawable.bg_default)
            tvBrandName.text = t.name
        }

        override fun onClick(p0: View?) {
            listener.choseBrand(mCarBrand!!)
        }
    }

    private var mChoseBrand: IChoseBrand? = null
    fun setChoseBrand(mchoseBrand: IChoseBrand) {
        this.mChoseBrand = mchoseBrand
    }

    interface IChoseBrand {
        fun choseBrand(brand: CarBrand)
    }
}