package com.kasao.qintai.fragment.social

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kasao.qintai.R
import com.kasao.qintai.activity.user.UserDetailActivity
import com.kasao.qintai.adapter.HistoryThumbleAdapter
import com.kasao.qintai.api.ApiInterface
import com.kasao.qintai.api.ApiManager
import com.kasao.qintai.model.domain.Thumbldomain
import com.kasao.qintai.util.ParmarsValue
import com.kasao.qintaiframework.base.BaseFragment
import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.until.GsonUtil
import okhttp3.ResponseBody
import java.util.*

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/29 0029:11
 */

class HistoryThumbleFragment : BaseFragment() {
    private var mRecycleView: RecyclerView? = null
    private var mBrowseAdapter: HistoryThumbleAdapter? = null
    private var id: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (null != arguments) {
            id = arguments?.getString("id")
        }

    }

    companion object {
        fun newInstance(id: String): HistoryThumbleFragment {
            val fragment = HistoryThumbleFragment()
            val bundle = Bundle()
            bundle.putString("id", id)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onInflater(inflater: LayoutInflater, container: ViewGroup?): View {
        rootView = inflater.inflate(R.layout.fragment_vistor_thumble, container, false)
        return rootView!!
    }

    override fun findView() {
        mRecycleView = rootView?.findViewById(R.id.recycleView)
        initRecycle(mRecycleView!!, object : RecyclerView.OnScrollListener() {
        })
        mBrowseAdapter = HistoryThumbleAdapter()
        mRecycleView!!.adapter = mBrowseAdapter
        mBrowseAdapter!!.setUserDetail(object : HistoryThumbleAdapter.IUserDetail {
            override fun toUserDetail(uid: String) {
                val intent = Intent(activity, UserDetailActivity::class.java)
                intent.putExtra(ParmarsValue.KEY_STR, uid)
                activity?.startActivity(intent)
            }
        })
    }

    override fun onloadData() {
        val map = HashMap<String, String>()
        map.put("id", id!!)
        ApiManager.getInstance.loadDataByParmars(ApiInterface.CAR_ZANS, map, object : HttpRespnse {
            override fun _onComplete() {

            }

            override fun _onNext(t: ResponseBody) {
                var thumdomain = GsonUtil.GsonToBean(t.string(), Thumbldomain::class.java)
                if (null != thumdomain) {
                    val lists = thumdomain.data
                    if (null != lists) {
                        mBrowseAdapter?.setDatathumble(lists)
                    } else {
                        mBrowseAdapter?.setNullData()
                    }
                } else {
                    mBrowseAdapter?.setNullData()
                }

            }

            override fun _onError(e: Throwable) {

            }
        })
    }
}