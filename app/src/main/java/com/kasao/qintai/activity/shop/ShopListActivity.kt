package com.kasao.qintai.activity.shop

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.kasao.qintai.R
import com.kasao.qintai.adapter.ShopAdapter
import com.kasao.qintai.api.ApiInterface
import com.kasao.qintai.api.ApiManager
import com.kasao.qintai.model.ShopInfo
import com.kasao.qintai.model.domain.ShopListdomain
import com.kasao.qintai.util.MapNativeUtil
import com.kasao.qintai.util.ParmarsValue
import com.kasao.qintaiframework.base.BaseActivity
import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.until.GsonUtil
import okhttp3.ResponseBody

class ShopListActivity : BaseActivity() {
    var mRecycleView: RecyclerView? = null
    var mAdapter: ShopAdapter? = null
    var mlist: List<ShopInfo>? = null
    override fun onLayoutLoad(): Int {
        return R.layout.activity_shoplist
    }

    override fun findView() {
        mRecycleView = findViewById(R.id.recycleView)
        findViewById<View>(R.id.viewBack).setOnClickListener { finish() }
        mAdapter = ShopAdapter()
        initRecycle(mRecycleView!!, null)
        mRecycleView?.adapter = mAdapter
    }

    override fun rendView() {
        if (null != mlist) {
            mAdapter?.mList = mlist
            mAdapter?.setShopAction(object : ShopAdapter.IShopAction {
                override fun goToDetail(mShop: ShopInfo) {
                    val bundle = Bundle()
                    bundle.putString(ParmarsValue.KEY_CID, mShop.store_id)
                    startActivity(ShopActivity::class.java, bundle)
                }

                override fun goToThere(mShop: ShopInfo) {
                    val longtitud = java.lang.Double.parseDouble(mShop.store_y)
                    val latitud = java.lang.Double.parseDouble(mShop.store_x)
                    MapNativeUtil.nativegation(this@ShopListActivity, longtitud, latitud)
                }
            })
        }
    }

    override fun onloadData() {
        ApiManager.getInstance.getDataByUrl(ApiInterface.CAR_SHOP, object : HttpRespnse {
            override fun _onComplete() {
            }

            override fun _onNext(t: ResponseBody) {
                var domain = GsonUtil.GsonToBean(t.string(), ShopListdomain::class.java)
                if (null != domain && null != domain.data) {
                    mlist = domain.data
                    rendView()
                }
            }

            override fun _onError(e: Throwable) {

            }
        })
    }

}
