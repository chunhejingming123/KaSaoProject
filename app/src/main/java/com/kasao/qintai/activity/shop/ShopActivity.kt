package com.kasao.qintai.activity.shop

import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng
import com.kasao.qintai.R
import com.kasao.qintai.api.ApiInterface
import com.kasao.qintai.api.ApiManager
import com.kasao.qintai.base.BaseKasaoApplication
import com.kasao.qintai.model.BannerEntity
import com.kasao.qintai.model.ShopInfo
import com.kasao.qintai.model.domain.Bannderdomain
import com.kasao.qintai.model.domain.Shopinfodomain
import com.kasao.qintai.util.ParmarsValue
import com.kasao.qintai.util.UtilsTool
import com.kasao.qintai.widget.banner.BannerView
import com.kasao.qintaiframework.base.BaseActivity
import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.until.GsonUtil
import com.kasao.qintaiframework.until.ScreenUtil
import okhttp3.ResponseBody
import java.util.ArrayList
import kotlin.collections.HashMap

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/24 0024:10
 */

class ShopActivity : BaseActivity() {
    var tvName: TextView? = null
    var tvAdress: TextView? = null
    var tvDistance: TextView? = null
    var tvIntro: TextView? = null
    var bannerView: BannerView? = null
    var storeId: String? = null
    var mShopinfo: ShopInfo? = null

    override fun onLayoutLoad(): Int {
        return R.layout.activity_shop
    }

    override fun findView() {
        tvName = findViewById(R.id.tvName)
        tvAdress = findViewById(R.id.tvAdress)
        tvDistance = findViewById(R.id.tvDistance)
        tvIntro = findViewById(R.id.tvIntro)
        bannerView = findViewById(R.id.bannerView)
        val parmars = bannerView?.getLayoutParams() as FrameLayout.LayoutParams
        parmars.width = ScreenUtil.getScreenW() - resources.getDimensionPixelOffset(R.dimen.dimen_80)
        parmars.height = 39 * (ScreenUtil.getScreenW() - resources.getDimensionPixelOffset(R.dimen.dimen_80)) / 57
        bannerView?.setLayoutParams(parmars)
        findViewById<View>(R.id.viewBack).setOnClickListener { finish() }


    }

    override fun rendView() {
        if (null != mShopinfo) {
            tvName?.setText(mShopinfo?.name)
            tvAdress?.setText(mShopinfo?.address)
            tvIntro?.text = mShopinfo?.store_body
            findViewById<View>(R.id.viewcall).setOnClickListener { callPerssion(this, mShopinfo!!.phone) }
            val domain = Bannderdomain()
            val list = ArrayList<BannerEntity>()
            if (null != mShopinfo?.store_img) {
                var str = mShopinfo?.store_img
                var size = str!!.size - 1
                for (i in 0..size!!) {
                    if (!TextUtils.isEmpty(mShopinfo!!.store_img[i])) {
                        val entity = BannerEntity()
                        entity.img = mShopinfo!!.store_img[i]
                        list.add(entity)
                    }
                }
                domain.data = list
                bannerView?.renderView(domain, (39 / 57).toFloat())
            }
            try {
//                val longtitud = java.lang.Double.parseDouble(mShopinfo?.store_y)
//                val latitud = java.lang.Double.parseDouble(mShopinfo?.store_x)
//                val calculateLineDistance = AMapUtils.calculateLineDistance(LatLng(longtitud, latitud), LatLng(BaseKasaoApplication.mLocation.getLatitude(), BaseKasaoApplication.mLocation.getLongitude())).toInt()
//                val b = UtilsTool.SetLengthToKm(calculateLineDistance.toDouble())
//                tvDistance?.text = b
            } catch (e: Throwable) {
                tvDistance?.visibility = View.GONE
            }

        }
    }

    override fun onloadData() {
        storeId = intent.getStringExtra(ParmarsValue.KEY_CID)
        var map = HashMap<String, String>()
        map.put("store_id", storeId!!)
        ApiManager.getInstance.getDataByParmars(ApiInterface.CAR_STORE_DETAIL, map, object : HttpRespnse {
            override fun _onComplete() {
            }

            override fun _onNext(t: ResponseBody) {
                var domain = GsonUtil.GsonToBean(t.string(), Shopinfodomain::class.java)
                if (null != domain && null != domain.data) {
                    mShopinfo = domain.data
                    rendView()
                }
            }

            override fun _onError(e: Throwable) {

            }
        })
    }
}