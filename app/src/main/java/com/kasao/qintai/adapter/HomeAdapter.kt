package com.kasao.qintai.adapter

import android.content.Intent
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kasao.qintai.R
import com.kasao.qintai.base.BaseKSadapter
import com.kasao.qintai.base.BaseViewHolder
import com.kasao.qintai.model.BannerEntity
import com.kasao.qintai.model.CarBrand
import com.kasao.qintai.model.CarDetailEntity
import com.kasao.qintai.model.domain.Bannderdomain
import com.kasao.qintai.model.domain.MainCar
import com.kasao.qintai.widget.CustomViewPager
import com.kasao.qintai.widget.FullyGridLayoutManager
import com.kasao.qintai.widget.ItemCarDisplayA
import com.kasao.qintai.widget.banner.BannerView
import com.kasao.qintaiframework.activity.WebActivity
import com.kasao.qintaiframework.base.MyApplication
import com.kasao.qintaiframework.until.FrameParmars
import com.kasao.qintaiframework.until.LogUtil

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/21 0021:09
 */

class HomeAdapter : BaseKSadapter<MainCar>() {
    private val ITEM_SPLIT = 100
    private val ITEM_STYLE = 101
    private val ITEM_NO = 103
    private val ITEM_HOT_BRAND = 104
    private val ITEM_HOT_RANK = 105
    var homeDomain: MainCar? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getHeaderItemCount(): Int {
        if (homeDomain != null && homeDomain?.carbanner != null) {
            return 1
        }
        return 0
    }

    override fun onCreateHeaderItemViewHolder(parent: ViewGroup?, headerViewType: Int): BaseViewHolder<MainCar>? {
        var view = LayoutInflater.from(parent?.context).inflate(R.layout.item_carsell_header, parent, false)
        return HeadBannerViewHolde(view)
    }

    override fun onBindHeaderItemViewHolder(headerViewHolder: BaseViewHolder<MainCar>?, position: Int) {
        headerViewHolder?.rendView(homeDomain!!, 0)
    }

    override fun getContentItemCount(): Int {
        if (null != homeDomain) {
            return 2
        }
        return 0
    }

    override fun getContentItemViewType(position: Int): Int {
        if (position == 0) {
            return ITEM_HOT_BRAND
        } else {
            return ITEM_HOT_RANK
        }
    }

    override fun onBindContentItemViewHolder(contentViewHolder: BaseViewHolder<MainCar>?, position: Int) {
        contentViewHolder?.rendView(homeDomain!!, position)
    }

    override fun onCreateContentItemViewHolder(parent: ViewGroup?, contentViewType: Int): BaseViewHolder<MainCar>? {
        when (contentViewType) {
            ITEM_HOT_BRAND ->
                return ContentBrandViewHolde(LayoutInflater.from(parent?.context).inflate(R.layout.item_home_carbrand_view, parent, false)!!, mICarSellAction!!)
            ITEM_HOT_RANK ->
                return ContentRankViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_home_carrank_view, parent, false)!!, mICarSellAction!!)
        }
        return null
    }

    override fun getFooterItemCount(): Int {
        if (homeDomain?.cargoods != null && !homeDomain?.cargoods?.isEmpty()!!) {
            return homeDomain?.cargoods?.size!!
        } else {
            return 2
        }

    }

    override fun getFooterItemViewType(position: Int): Int {
        if (position == 0) {
            return ITEM_SPLIT
        } else {
            if (null != homeDomain?.cargoods && homeDomain?.cargoods?.size!! > 0) {
                return ITEM_STYLE
            } else {
                return ITEM_NO
            }
            return ITEM_NO
        }
    }

    override fun onCreateFooterItemViewHolder(parent: ViewGroup?, footerViewType: Int): BaseViewHolder<MainCar>? {
        when (footerViewType) {
            ITEM_SPLIT -> return BaseViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_home_footer_splitview, parent, false)!!)
            ITEM_STYLE -> return FooterViewHolde(ItemCarDisplayA(parent!!.context), mICarSellAction!!)
            ITEM_NO ->
                return BaseViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_empty_view, parent, false))
        }
        return null
    }

    override fun onBindFooterItemViewHolder(footerViewHolder: BaseViewHolder<MainCar>?, position: Int) {
        if (null != homeDomain?.cargoods) {
            footerViewHolder?.rendView(homeDomain!!, position)
        }

    }

    class HeadBannerViewHolde(item: View) : BaseViewHolder<MainCar>(item) {
        var bannerView: BannerView? = null

        init {
            bannerView = item.findViewById(R.id.bannerView)
        }

        override fun rendView(t: MainCar, position: Int) {
            var banners = t?.carbanner;
            bannerView?.isEnabled = true
            var bannerDomain = Bannderdomain()
            bannerDomain.data = banners
            bannerView?.renderView(bannerDomain, 0.38f)
            bannerView?.setOnBannerViewActionListener(object : BannerView.OnBannerViewActionListener {
                override fun onHorizontalImageListScrollStart() {

                }

                override fun onHorizontalImageListScrollStop() {

                }

                override fun onClickBanner(node: BannerEntity?) {
                    if (node != null && !TextUtils.isEmpty(node.url)) {
                        val intent = Intent(itemView.context!!, WebActivity::class.java)
                        intent.putExtra(FrameParmars.KEY_TITLE, "详情")
                        intent.putExtra(FrameParmars.KEY_URL, node.url)
                        itemView.context.startActivity(intent)
                    }
                }
            })
        }
    }

    class ContentBrandViewHolde(item: View, mCAction: ICareSellAction) : BaseViewHolder<MainCar>(item), View.OnClickListener {
        var tvMore: View? = null
        var recyclview: RecyclerView? = null
        var adapter: CarViewpageGridAdapter? = null
        var listener: ICareSellAction? = null

        init {
            adapter = CarViewpageGridAdapter(item.context)
            tvMore = item.findViewById(R.id.tvMore)
            recyclview = item.findViewById(R.id.recycle_gride)
            listener = mCAction
            var grid = FullyGridLayoutManager(MyApplication.applicaton, 4)
            recyclview?.layoutManager = grid
            recyclview?.setItemAnimator(DefaultItemAnimator())
            // 设置固定大小
            recyclview?.setHasFixedSize(true)
            recyclview?.adapter = adapter
            tvMore?.setOnClickListener(this)
        }

        override fun rendView(t: MainCar, position: Int) {
            if (null != t && t.car_class != null) {
                if (t.car_class.get(0) != null && t.car_class.get(0).brand != null) {
                    adapter?.setData(t.car_class[0].brand)
                } else {
                    adapter?.setNodata()
                }

                adapter?.setCategory(object : CarViewpageGridAdapter.CarCategory {
                    override fun choseBrand(brand: CarBrand?) {
                        listener!!.onChoseCar(brand!!.name, brand!!.id)
                    }
                })
            }

        }

        override fun onClick(p0: View?) {
            listener?.onSearchMore()
        }
    }

    class ContentRankViewHolder(item: View, mCAction: ICareSellAction) : BaseViewHolder<MainCar>(item), View.OnClickListener {
        var viewPage: CustomViewPager? = null
        private var pager: ViewPageCarRankAdapter? = null
        private var tvCall1: TextView? = null
        private var tvCall2: TextView? = null
        private var viewlinePhone: View? = null
        private var phoneNum: String? = null
        private var listNum: List<String>? = null
        var listener: ICareSellAction? = null

        init {
            pager = ViewPageCarRankAdapter(item.context)
            viewPage = item.findViewById(R.id.viewpager)
            tvCall1 = item.findViewById(R.id.tvCall1)
            tvCall2 = item.findViewById(R.id.tvCall2)
            viewlinePhone = item.findViewById(R.id.viewlinePhone)
            listener = mCAction
            viewPage?.adapter = pager
            viewPage?.pageMargin = 8
            tvCall2?.setOnClickListener(this)
            tvCall1?.setOnClickListener(this)
            pager?.setmIClickPager(object : ViewPageCarRankAdapter.IClickPager {
                override fun onClickPager(exponent: CarDetailEntity?) {
                    listener?.onCarDetail(exponent!!)
                }
            })
        }

        override fun rendView(t: MainCar, position: Int) {
            pager?.setData(t?.rank)
            listNum = t?.carphone
            if (null != listNum && listNum?.size!! > 0) {
                viewlinePhone?.visibility = View.VISIBLE
                var siez = listNum?.size
                if (siez == 1) {
                    tvCall1?.visibility = View.VISIBLE
                    tvCall1?.text = listNum?.get(0)
                    tvCall2?.visibility = View.GONE
                }
                if (siez == 2) {
                    tvCall1?.setVisibility(View.VISIBLE)
                    tvCall2?.setVisibility(View.VISIBLE)
                    tvCall1?.setText(listNum?.get(0))
                    tvCall2?.setText(listNum?.get(1))
                }
            } else {
                viewlinePhone?.visibility = View.GONE
            }
        }

        override fun onClick(p0: View?) {
            when (p0?.id) {
                R.id.tvCall1 -> phoneNum = listNum?.get(0)
                R.id.tvCall2 -> phoneNum = listNum?.get(1)
            }
            listener?.callConsult(phoneNum!!)
        }
    }

    class FooterViewHolde(item: View, mCAction: ICareSellAction) : BaseViewHolder<MainCar>(item), View.OnClickListener {
        var view: ItemCarDisplayA? = null
        var goods: CarDetailEntity? = null
        var listener: ICareSellAction? = null

        init {
            view = item as ItemCarDisplayA
            view?.setOnClickListener(this)
            listener = mCAction
        }

        override fun rendView(t: MainCar, position: Int) {
            view?.rendView(t.cargoods[position], false, false)
            goods = t.cargoods[position]

        }

        override fun onClick(p0: View?) {
            if (null != goods) {
                listener?.onCarDetail(goods!!)
            }
        }
    }

    var mICarSellAction: ICareSellAction? = null
    fun setIcarsAction(listener: ICareSellAction) {
        this.mICarSellAction = listener
    }

    interface ICareSellAction {
        fun onCarDetail(carDetail: CarDetailEntity)
        fun onSearchMore()
        fun callConsult(strTel: String)
        fun onChoseCar(name: String, id: String)
    }

}