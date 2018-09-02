package com.kasao.qintai.fragment.social

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kasao.qintai.R
import com.kasao.qintai.activity.user.UserDetailActivity
import com.kasao.qintai.adapter.HistoryBrowseAdapter
import com.kasao.qintai.api.ApiInterface
import com.kasao.qintai.api.ApiManager
import com.kasao.qintai.model.domain.Browsedomain
import com.kasao.qintai.util.ParmarsValue
import com.kasao.qintaiframework.base.BaseFragment
import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.until.GsonUtil
import okhttp3.ResponseBody
import java.util.HashMap

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/29 0029:11
 */

class HistoryLookFragment : BaseFragment() {
    private var mRecycleView: RecyclerView? = null
    private var mBrowseAdapter: HistoryBrowseAdapter? = null
    private var articleId: String? = ""
    private var isLoading: Boolean = false
    private var PageIndex = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (null != arguments) {
            articleId = arguments?.getString("id")
        }
    }

    companion object {
        fun newInstance(articleId: String): HistoryLookFragment {
            val fragment = HistoryLookFragment()
            val mbundle = Bundle()
            mbundle.putString("id", articleId)
            fragment.arguments = mbundle
            return fragment
        }

    }

    override fun onInflater(inflater: LayoutInflater, container: ViewGroup?): View {
        rootView = inflater.inflate(R.layout.fragment_history_browse, container, false);
        return rootView!!
    }

    override fun findView() {
        mRecycleView = rootView?.findViewById(R.id.recycleView)
        initRecycle(mRecycleView!!, object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItem = llm!!.findLastVisibleItemPosition()
                val totalItemCount = llm!!.itemCount
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 2 && dy > 0 && !isLoading) {
                    isLoading = true
                    onloadData()
                }
            }
        })
        mBrowseAdapter = HistoryBrowseAdapter()
        mRecycleView?.adapter = mBrowseAdapter
        mBrowseAdapter?.setUserDetail(object : HistoryBrowseAdapter.IUserDetail {
            override fun toUserDetail(uid: String) {
                val intent = Intent(activity, UserDetailActivity::class.java)
                intent.putExtra(ParmarsValue.KEY_STR, uid)
                activity!!.startActivity(intent)
            }
        })
    }

    override fun onloadData() {
        var map = HashMap<String, String>()
        map["article_id"] = articleId!!
        map["page_num"] = PageIndex.toString()

        ApiManager.getInstance.loadDataByParmars(ApiInterface.CAR_LOOKCOUNT, map, object : HttpRespnse {
            override fun _onComplete() {
            }

            override fun _onNext(t: ResponseBody) {
                var domain = GsonUtil.GsonToBean(t.string(), Browsedomain::class.java)
                val lists = domain!!.data
                if (null != lists) {
                    if (lists!!.size > 0) {
                        PageIndex++
                        isLoading = false
                        mBrowseAdapter?.setDatabrowse(lists)
                    } else {
                        isLoading = true
                    }

                }
            }

            override fun _onError(e: Throwable) {
            }
        })

    }

}