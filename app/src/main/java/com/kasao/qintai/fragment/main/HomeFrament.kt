package com.kasao.qintai.fragment.main

import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kasao.qintai.MainActivity
import com.kasao.qintai.R
import com.kasao.qintai.activity.main.CarSearcherActivity
import com.kasao.qintai.activity.main.CarSeriesActivity
import com.kasao.qintai.adapter.HomeAdapter
import com.kasao.qintai.api.ApiInterface
import com.kasao.qintai.api.ApiManager
import com.kasao.qintai.dialoge.DialogeCarChose
import com.kasao.qintai.model.CarDetailEntity
import com.kasao.qintai.model.domain.MainCardomain
import com.kasao.qintai.util.ParmarsValue
import com.kasao.qintai.widget.FullyLinearLayoutManager
import com.kasao.qintaiframework.base.BaseFragment
import com.kasao.qintaiframework.base.MyApplication
import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.until.GsonUtil
import okhttp3.ResponseBody

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/20 0020:09
 */

class HomeFrament : BaseFragment() {
    var searchEditex: TextView? = null
    var mSwipterfresh: SwipeRefreshLayout? = null
    var mRecycleView: RecyclerView? = null
    var homeAdapter: HomeAdapter? = null
    // var skeletonScreen: SkeletonScreen? = null
    override fun onInflater(inflater: LayoutInflater, container: ViewGroup?): View {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)
        return rootView!!
    }

    override fun findView() {
        searchEditex = rootView?.findViewById(R.id.include_editex)
        mSwipterfresh = rootView?.findViewById(R.id.swipeRefresh)
        mRecycleView = rootView?.findViewById(R.id.recycleview)

        homeAdapter = HomeAdapter()
        val manage = FullyLinearLayoutManager(MyApplication.applicaton)
        manage.setOrientation(LinearLayoutManager.VERTICAL)
        initRecycle(mRecycleView!!, object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //                int top = manage.getScrollY();
                //                firstHeight = recyclerView.getLayoutManager().getChildAt(0).getHeight();
                //                if (top <= 0) {
                //                    mEditext.setHintTextColor(getResources().getColor(R.color.color_666666));
                //                    mEditext.setBackgroundResource(R.drawable.shape_round_white70);
                //                } else if (top > 0 && top <= firstHeight) {
                ////                    float scale = (float) top / firstHeight;
                ////                    float alpha = (255 * scale);
                ////                    mEditext.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                //                } else {
                //                    mEditext.setBackgroundResource(R.drawable.shape_round_gray5);
                //                    mEditext.setHintTextColor(getResources().getColor(R.color.white));
                //                }
            }
        }, manage)
        searchEditex?.setOnClickListener {
            var intent = Intent(activity, CarSearcherActivity::class.java)
            startActivity(intent)
        }
        mRecycleView?.adapter = homeAdapter
        homeAdapter?.setIcarsAction(object : HomeAdapter.ICareSellAction {
            override fun onCarDetail(carDetail: CarDetailEntity) {
                val intent = Intent(activity, CarSeriesActivity::class.java)
                intent.putExtra(ParmarsValue.KEY_GOODID, carDetail.goods_id)
                activity?.startActivity(intent)
            }

            override fun onSearchMore() {
                var intent = Intent(activity, CarSearcherActivity::class.java)
                startActivity(intent)
            }

            override fun callConsult(strTel: String) {
                var ac = activity as MainActivity
                ac.callPerssion(ac, strTel)
            }

            override fun onChoseCar(name: String, id: String) {
                val chose = DialogeCarChose(activity!!, name, id)
                chose.showDialoge(object : DialogeCarChose.IChoseSeries {
                    override fun onChoseSerise(id: String?) {
                        val intent = Intent(activity, CarSeriesActivity::class.java)
                        intent.putExtra(ParmarsValue.KEY_GOODID, id)
                        activity?.startActivity(intent)
                        chose.hide()

                    }
                })
            }
        })

        mSwipterfresh?.isRefreshing = false
        mSwipterfresh?.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_light)
        mSwipterfresh?.setOnRefreshListener {
            onloadData()
            mSwipterfresh?.isRefreshing = false
        }

//
//        var root = rootView?.findViewById<View>(R.id.rootView)
//        skeletonScreen = Skeleton.bind(root)
//                .load(R.layout.activity_home_skeletonscreen)
//                .duration(10000)
//                .color(R.color.shimmer_color)
//                .angle(0)
//                .show()
    }

    override fun onloadData() {
        ApiManager.getInstance.getDataByUrl(ApiInterface.homeMaindata, object : HttpRespnse {
            override fun _onComplete() {
            }

            override fun _onNext(t: ResponseBody) {
                var homeDomain: MainCardomain? = GsonUtil.GsonToBean(t.string(), MainCardomain::class.java)
                homeAdapter?.homeDomain = homeDomain?.data
            }

            override fun _onError(e: Throwable) {

            }
        })
    }
}