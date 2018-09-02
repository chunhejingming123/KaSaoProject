package com.kasao.qintai.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kasao.qintai.base.BaseKSadapter
import com.kasao.qintai.base.BaseViewHolder
import com.kasao.qintai.model.CarDetailEntity
import com.kasao.qintai.R
import com.kasao.qintai.util.DataTypeChange

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/22 0022:17
 */

class CarSeriesAdapter : BaseKSadapter<CarDetailEntity>() {
     var mlist: List<CarDetailEntity>? = null
        set(value) {
            isOnlyLoadingOne=false
            field = value
            notifyDataSetChanged()
        }

    override fun getContentItemCount(): Int {
        if (null == mlist) {
            return 0
        }
        return mlist!!.size
    }

    override fun onBindContentItemViewHolder(contentViewHolder: BaseViewHolder<CarDetailEntity>?, position: Int) {
        contentViewHolder?.rendView(mlist!!.get(position), position)
    }

    override fun onCreateContentItemViewHolder(parent: ViewGroup?, contentViewType: Int): BaseViewHolder<CarDetailEntity>? {
        return ContentViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_carseries_view, parent, false)!!, mIonclick!!)
    }

    class ContentViewHolder(item: View, mIonclick: IOnClik) : BaseViewHolder<CarDetailEntity>(item), View.OnClickListener {
        var tvIntro: TextView
        var tvprice: TextView
        var viewConsult: View
        var viewItem: View
        var car: CarDetailEntity? = null
        var listener: IOnClik? = null

        init {
            tvprice = itemView.findViewById(R.id.tvPrice)
            tvIntro = itemView.findViewById(R.id.tvIntro)
            viewItem = itemView.findViewById(R.id.viewItem)
            viewConsult = itemView.findViewById(R.id.viewConsult)
            listener = mIonclick
            viewItem.setOnClickListener(this)
            viewConsult.setOnClickListener(this)
        }

        override fun rendView(t: CarDetailEntity, position: Int) {
            car = t
            tvIntro.setText(t.name)
            if (TextUtils.isEmpty(t.goods_price) || t.goods_price.equals("0") || t.goods_price.equals("0.0") || t.goods_price.equals("0.00")) {
                tvprice.text = "厂家指导价：暂无报价"
            } else {
                val money = DataTypeChange.getF10000(DataTypeChange.stringToDounble(t.goods_price))//entity.goods_price;
                tvprice.text = "厂家指导价：$money"
            }
        }

        override fun onClick(p0: View?) {
            when (p0?.id) {
                R.id.viewItem -> listener?.goToDetail(car!!.goods_id)
                R.id.viewConsult -> listener?.goToConsult(car!!)
            }
        }
    }

    private var mIonclick: IOnClik? = null
    fun setmIonclick(mIonclick: IOnClik) {
        this.mIonclick = mIonclick
    }

    interface IOnClik {
        fun goToDetail(goodsId: String)

        fun goToConsult(cardetail: CarDetailEntity)
    }
}