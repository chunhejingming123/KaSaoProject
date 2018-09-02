package com.kasao.qintai.fragment.social

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kasao.qintai.R
import com.kasao.qintai.activity.kayou.PublishSoicalActivity
import com.kasao.qintai.activity.kayou.ReportFriendActivity
import com.kasao.qintai.activity.kayou.SocialDetailsActivity
import com.kasao.qintai.activity.user.UserDetailActivity
import com.kasao.qintai.adapter.KaFriendAdapter
import com.kasao.qintai.api.ApiInterface
import com.kasao.qintai.api.ApiManager
import com.kasao.qintai.base.BaseKasaoApplication
import com.kasao.qintai.model.RtnSuss
import com.kasao.qintai.model.SNSEntity
import com.kasao.qintai.model.domain.SnsEntitydomain
import com.kasao.qintai.model.domain.Zandomain
import com.kasao.qintai.popuwindow.ReportPopuWindow
import com.kasao.qintai.util.ParmarsValue
import com.kasao.qintaiframework.base.BaseFragment
import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.until.GsonUtil
import okhttp3.ResponseBody
import java.util.*

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/20 0020:09
 */

class SocialFragment : BaseFragment() {
    var mRecyclerView: RecyclerView? = null
    var swipeRefresh: SwipeRefreshLayout? = null
    var viewRight: View? = null
    var mAdapter: KaFriendAdapter? = null
    private var currentType: String = "4"
    private var currtypage = 1
    private var isLoading: Boolean = false
    private var isFresh: Boolean = false
    private val mHandler = Handler()
    val REQUEST_CODE_DETAIL = 102
    private var index = -1
    private var window: ReportPopuWindow? = null
    private var id: String? = ""
    override fun onInflater(inflater: LayoutInflater, container: ViewGroup?): View {
        rootView = inflater.inflate(R.layout.fragment_socail, container, false)
        return rootView!!
    }

    override fun findView() {
        viewRight = rootView?.findViewById(R.id.viewRight)
        mRecyclerView = rootView?.findViewById(R.id.recycleview)
        swipeRefresh = rootView?.findViewById(R.id.swipeRefresh)
        mAdapter = KaFriendAdapter(activity!!)
        initRecycle(mRecyclerView!!, object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItem = llm!!.findLastVisibleItemPosition()
                val totalItemCount = llm!!.getItemCount()
                if (lastVisibleItem >= totalItemCount - 4 && dy > 0 && !isLoading && !isFresh) {
                    isLoading = true
                    currtypage++
                    onloadData()
                }
            }
        })
        mRecyclerView?.adapter = mAdapter

        mAdapter?.setOnFriendAction(object : KaFriendAdapter.IOnFriendAction {

            override fun ontoFriendDetail(entity: SNSEntity, position: Int) {
                index = position
                val intent = Intent(activity, SocialDetailsActivity::class.java)
                intent.putExtra("id", entity.id)
                intent.putExtra("data", entity)
                intent.putExtra(ParmarsValue.KEY_CAN_DEL, false)
                startActivityForResult(intent, REQUEST_CODE_DETAIL)
            }

            override fun setThumble(snsentity: SNSEntity, position: Int) {
                onThumble(snsentity, position)
            }

            override fun setComment(snsentity: SNSEntity, position: Int) {
                index = position
                val intent = Intent(activity, SocialDetailsActivity::class.java)
                intent.putExtra("id", snsentity.id)
                intent.putExtra("data", snsentity)
                intent.putExtra(ParmarsValue.KEY_CAN_DEL, false)
                startActivityForResult(intent, REQUEST_CODE_DETAIL)
            }

            override fun userDetail(snsentity: SNSEntity) {
                val intent = Intent(activity, UserDetailActivity::class.java)
                intent.putExtra(ParmarsValue.KEY_STR, snsentity.u_id)
                startActivity(intent)
            }

            override fun rePort(snsentity: SNSEntity, positon: Int) {
                val isSelf = snsentity.u_id.equals(BaseKasaoApplication.getUserId())

                id = snsentity.id
                if (isSelf) {
                    index = positon
                } else {
                    index = -1
                }
                window?.show(isSelf)
            }
        })
        viewRight?.setOnClickListener { startActivityForResult(Intent(activity, PublishSoicalActivity::class.java), 1) }

        swipeRefresh?.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_light)
        swipeRefresh?.setOnRefreshListener {
            swipeRefresh?.setRefreshing(true)
            mHandler.postDelayed(Runnable {
                currtypage = 1
                isFresh = true
                onloadData()
            }, 150)
        }

        window = ReportPopuWindow(activity, rootView?.findViewById(R.id.mianroot))
        window?.showPopuWindow(object : ReportPopuWindow.OnReport {
            override fun takeCare() {

            }

            override fun onReport() {
                val bundle = Bundle()
                bundle.putString("sid", id)
                startActivity(ReportFriendActivity::class.java, bundle)
            }

            override fun del() {
                delComment()
            }
        })
    }

    override fun onloadData() {
        val param = HashMap<String, String>()
        param["attr"] = currentType
        param["page_num"] = currtypage.toString()
        param["u_id"] = BaseKasaoApplication.getUserId()
        ApiManager.getInstance.loadDataByParmars(ApiInterface.CAR__FRIENDSQUAN, param, object : HttpRespnse {
            override fun _onComplete() {
            }

            override fun _onNext(t: ResponseBody) {
                var domain = GsonUtil.GsonToBean(t.string(), SnsEntitydomain::class.java)
                if (null != domain && null != domain.data) {
                    var newslist = domain.data

                    isLoading = newslist.size < 0
                    if (isFresh) {
                        mAdapter?.setFresh(newslist, mHandler)
                        mRecyclerView?.scrollToPosition(0)
                    } else {
                        currtypage++
                        mAdapter?.setData(newslist)
                    }
                    isFresh = false
                }
                swipeRefresh?.setRefreshing(false)
            }

            override fun _onError(e: Throwable) {
            }
        })
    }

    // 点赞接口
    fun onThumble(snsentity: SNSEntity, index: Int) {
        val paramzan = HashMap<String, String>()
        paramzan["u_id"] = BaseKasaoApplication.getUserId()
        paramzan["af_id"] = snsentity.id
        paramzan["applies"] = "android"

        ApiManager.getInstance.loadDataByParmars(ApiInterface.CAR_ZAN, paramzan, object : HttpRespnse {
            override fun _onComplete() {
            }

            override fun _onNext(t: ResponseBody) {
                var domain = GsonUtil.GsonToBean(t.string(), Zandomain::class.java)
                if (null != domain && domain.code.equals("200")) {
                    mAdapter?.notifyChange(domain, index)
                }
            }

            override fun _onError(e: Throwable) {
            }
        })

    }


    // 删除朋友圈
    private fun delComment() {
        var map = HashMap<String, String>()
        map["af_id"] = id!!
        var url = ApiInterface.DELETE_CAR_CICLE
        ApiManager.getInstance.loadDataByParmars(ApiInterface.DELETE_CAR_CICLE, map, object : HttpRespnse {
            override fun _onComplete() {

            }

            override fun _onNext(t: ResponseBody) {
                var domain = GsonUtil.GsonToBean(t.string(), RtnSuss::class.java)
                if (null != domain && domain?.code.equals("200")) {
                    mAdapter?.notifyRemove(index)
                }

            }

            override fun _onError(e: Throwable) {

            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            mHandler.postDelayed(Runnable {
                currtypage = 1
                isFresh = true
                onloadData()
            }, 150)
        }
    }
}