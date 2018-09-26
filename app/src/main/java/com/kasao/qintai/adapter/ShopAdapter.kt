package com.kasao.qintai.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng
import com.kasao.qintai.R
import com.kasao.qintai.base.BaseKSadapter
import com.kasao.qintai.base.BaseKasaoApplication
import com.kasao.qintai.base.BaseViewHolder
import com.kasao.qintai.model.ShopInfo
import com.kasao.qintai.util.ContextComp
import com.kasao.qintai.util.GlideUtil
import com.kasao.qintai.util.UtilsTool

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/24 0024:08
 */

class ShopAdapter : BaseKSadapter<ShopInfo>() {
    var mList: List<ShopInfo>? = null
        set(value) {
            isOnlyLoadingOne=false
            field = value
            notifyDataSetChanged()
        }

    override fun getHeaderItemCount(): Int {
        isEmptyState=(mList==null||mList?.size==0)
        return super.getHeaderItemCount()
    }
    override fun getContentItemCount(): Int {
        if (null == mList || mList!!.isEmpty()) {
            return 0
        } else {
            return mList!!.size
        }

    }

    override fun onBindContentItemViewHolder(contentViewHolder: BaseViewHolder<ShopInfo>?, position: Int) {
        if (null != mList) {
            contentViewHolder?.rendView(mList!!.get(position), position)
        }
    }

    override fun onCreateContentItemViewHolder(parent: ViewGroup?, contentViewType: Int): BaseViewHolder<ShopInfo>? {
        return ContentViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_shop_view, parent, false), mIShopAction!!)
    }

    class ContentViewHolder(itemView: View, mShop: IShopAction) : BaseViewHolder<ShopInfo>(itemView), View.OnClickListener {
        var ivShoplogo: ImageView
        var tvName: TextView
        var tvDes: TextView
        var tvGoThere: TextView
        var tvDistance: TextView
        private var `mShop`: ShopInfo? = null
        var listener: IShopAction? = null

        init {
            ivShoplogo = itemView.findViewById(R.id.ivShoplogo)
            tvName = itemView.findViewById(R.id.tvShopName)
            tvDes = itemView.findViewById(R.id.tvShopDes)
            tvDistance = itemView.findViewById(R.id.tvShopDistance)
            tvGoThere = itemView.findViewById(R.id.tvGothere)
            listener = mShop
            val parmars = ivShoplogo.layoutParams as FrameLayout.LayoutParams
            parmars.width = ContextComp.getDimensionPixelOffset(R.dimen.dimen_110)
            parmars.height = 13 * ContextComp.getDimensionPixelOffset(R.dimen.dimen_110) / 19
            ivShoplogo.layoutParams = parmars
        }


        override fun rendView(o: ShopInfo, position: Int) {
            if (null != o) {
                mShop = o
                itemView.setOnClickListener(this)
                tvGoThere.setOnClickListener(this)
                tvName.setText(o.name)
                tvDes.setText(o.address)
                val longtitud = java.lang.Double.parseDouble(o.store_y)
                val latitud = java.lang.Double.parseDouble(o.store_x)
                if (null!=BaseKasaoApplication.mLocation){
                    val calculateLineDistance = AMapUtils.calculateLineDistance(LatLng(longtitud, latitud), LatLng(BaseKasaoApplication.mLocation.getLatitude(), BaseKasaoApplication.mLocation.getLongitude()))
                    val b = UtilsTool.SetLengthToKm(calculateLineDistance.toDouble())
                    tvDistance.text = b
                }else{
                   tvDistance?.visibility=View.GONE
                }
                    if (null != o.store_img && !o.store_img.isEmpty()) {
                        if (!TextUtils.isEmpty(o.store_img.get(0).toString())) {
                            GlideUtil.into(itemView.context, o.store_img[0].toString(), ivShoplogo, R.drawable.bg_default)

                        }
                    }


            }
        }

        override fun onClick(p0: View?) {
            when (p0?.id) {
                R.id.tvGothere -> listener!!.goToThere(mShop!!)

                else -> listener!!.goToDetail(mShop!!)
            }
        }

    }

    var mIShopAction: IShopAction? = null
    fun setShopAction(mShop: IShopAction) {
        this.mIShopAction = mShop
    }

    public interface IShopAction {
        fun goToDetail(mShop: ShopInfo)
        fun goToThere(mShop: ShopInfo)
    }
}