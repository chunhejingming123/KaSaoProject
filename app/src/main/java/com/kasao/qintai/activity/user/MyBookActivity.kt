package com.kasao.qintai.activity.user

import android.support.v7.widget.RecyclerView
import android.view.View
import com.kasao.qintai.R
import com.kasao.qintai.adapter.MyBookAdapter
import com.kasao.qintai.api.ApiInterface
import com.kasao.qintai.api.ApiManager
import com.kasao.qintai.base.BaseKasaoApplication
import com.kasao.qintai.model.domain.OrderCodedomain
import com.kasao.qintaiframework.base.BaseActivity
import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.until.GsonUtil
import okhttp3.ResponseBody
import java.util.HashMap

class MyBookActivity : BaseActivity() {
    private var recycleView: RecyclerView? = null
    private var adapter: MyBookAdapter? = null
    override fun onLayoutLoad(): Int {
        return R.layout.activity_mybook
    }

    override fun findView() {
        findViewById<View>(R.id.viewBack).setOnClickListener { finish() }
        recycleView = findViewById(R.id.recycleView)
        initRecycle(recycleView!!, object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
        adapter = MyBookAdapter()
        recycleView?.adapter = adapter
    }

    override fun onloadData() {
        val map = HashMap<String, String>()
        map["u_id"] = BaseKasaoApplication.getUser().user_id
        ApiManager.getInstance.loadDataByParmars(ApiInterface.ORDER_LIST, map, object : HttpRespnse {
            override fun _onComplete() {
            }

            override fun _onNext(t: ResponseBody) {
                var domain = GsonUtil.GsonToBean(t.string(), OrderCodedomain::class.java)
                if (null != domain && null != domain.data) {
                    adapter?.lists = domain.data
                }else{
                    adapter?.setNullData()
                }
            }

            override fun _onError(e: Throwable) {
            }
        })
    }
}
