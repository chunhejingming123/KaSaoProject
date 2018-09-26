package com.kasao.qintai.activity.user

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.kasao.qintai.R
import com.kasao.qintai.activity.main.CarDetailActivity
import com.kasao.qintai.adapter.CarSearchAdapter
import com.kasao.qintai.api.ApiInterface
import com.kasao.qintai.api.ApiManager
import com.kasao.qintai.base.BaseKasaoApplication
import com.kasao.qintai.model.CarDetailEntity
import com.kasao.qintai.model.domain.CarListdomain
import com.kasao.qintai.util.ParmarsValue
import com.kasao.qintaiframework.base.BaseActivity
import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.until.GsonUtil
import okhttp3.ResponseBody

class MyCollectionActivity : BaseActivity() {
    var mRecycleView: RecyclerView? = null
    var mAdapter: CarSearchAdapter? = null
    override fun onLayoutLoad(): Int {
        return R.layout.activity_my_collection
    }

    override fun findView() {
        findViewById<View>(R.id.viewBack).setOnClickListener {
            finish()
        }
        findViewById<ImageView>(R.id.ivBack).setImageResource(R.drawable.icon_return_black)
        findViewById<TextView>(R.id.tvTitle).text = getString(R.string.titleMyClooection)
        mRecycleView = findViewById(R.id.recycle)
        rendView()
    }

    override fun rendView() {
        initRecycle(mRecycleView!!, object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
        mAdapter = CarSearchAdapter()
        mAdapter?.isDelete = true
        mRecycleView?.adapter = mAdapter
        mAdapter?.setCarSellAction(object : CarSearchAdapter.ICarSellAction {
            override fun onCarDetaile(entity: CarDetailEntity) {
                val bundle = Bundle()
                bundle.putString(ParmarsValue.KEY_GOODID, entity.goods_id)
                startActivity(CarDetailActivity::class.java, bundle)
            }

            override fun onCarDel(entity: CarDetailEntity, index: Int) {
                del(entity, index)
            }
        })


    }

    private fun del(entity: CarDetailEntity, index: Int) {
        val map = HashMap<String, String>()
        map[ParmarsValue.KEY_GOODID] = entity.goods_id
        map[ParmarsValue.KEY_UID] = BaseKasaoApplication.getUser().user_id
        ApiManager.getInstance.loadDataByParmars(ApiInterface.CAR_STORE, map, object : HttpRespnse {
            override fun _onComplete() {
            }

            override fun _onNext(t: ResponseBody) {
                mAdapter?.removeItem(index)
            }

            override fun _onError(e: Throwable) {

            }
        })
    }

    override fun onloadData() {
        var map = HashMap<String, String>()
        map["u_id"] = BaseKasaoApplication.getUser().user_id
        ApiManager.getInstance.loadDataByParmars(ApiInterface.CAR_SHORE_LIST, map, object : HttpRespnse {
            override fun _onComplete() {

            }

            override fun _onNext(t: ResponseBody) {
                var domain = GsonUtil.GsonToBean(t.string(), CarListdomain::class.java)
                if (null != domain && null != domain.data) {
                    mAdapter?.setDatas(domain.data)
                }else{
                    mAdapter?.setNullData()
                }
            }

            override fun _onError(e: Throwable) {

            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if(null!=mAdapter){
            mAdapter?.onDestroy()
        }
    }
}
