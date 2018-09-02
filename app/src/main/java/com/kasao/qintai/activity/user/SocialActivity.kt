package com.kasao.qintai.activity.user

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.kasao.qintai.R
import com.kasao.qintai.activity.kayou.SocialDetailsActivity
import com.kasao.qintai.adapter.SocialAdapter
import com.kasao.qintai.api.ApiInterface
import com.kasao.qintai.api.ApiManager
import com.kasao.qintai.base.BaseKasaoApplication
import com.kasao.qintai.model.SNSEntity
import com.kasao.qintai.model.domain.SnsEntitydomain
import com.kasao.qintai.util.ParmarsValue
import com.kasao.qintaiframework.base.BaseActivity
import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.until.GsonUtil
import com.kasao.qintaiframework.until.ToastUtil
import okhttp3.ResponseBody
import java.util.*

/**
 * 发布的朋友圈
 */

class SocialActivity : BaseActivity() {
    var recyclerView: RecyclerView? = null
    var adapter: SocialAdapter? = null
    var isLoading: Boolean = false
    private var currtypage = 1
    private var currentType = "4"
    val REQUEST_CODE_DELET = 101
    var index: Int = 0
    override fun onLayoutLoad(): Int {
        return R.layout.activity_social
    }

    override fun findView() {
        findViewById<View>(R.id.viewBack).setOnClickListener { finish() }
        findViewById<TextView>(R.id.tvTitle).text = getString(R.string.mypublish)
        findViewById<ImageView>(R.id.ivBack).setImageResource(R.drawable.icon_return_black)
        recyclerView = findViewById(R.id.recycleView)
        initRecycle(recyclerView!!, object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var lastVisibleItem = llm!!.findLastVisibleItemPosition()
                var totalItemCount = llm!!.getItemCount()
                if (lastVisibleItem >= totalItemCount - 4 && dy > 0 && !isLoading) {
                    isLoading = true
                    currtypage++
                    onloadData()
                }
            }
        })
        adapter = SocialAdapter();
        recyclerView?.adapter = adapter
        adapter?.setScoicalAction(object : SocialAdapter.ISocialAction {
            override fun toDetail(entity: SNSEntity, position: Int) {
                index = position

                val intent = Intent(this@SocialActivity, SocialDetailsActivity::class.java)
                intent.putExtra("id", entity.id)
                intent.putExtra("data", entity)
                intent.putExtra(ParmarsValue.KEY_CAN_DEL, true)
                startActivityForResult(intent, REQUEST_CODE_DELET)
            }
        })
    }

    override fun onloadData() {
        var map: Map<String, String> = getMap() ?: return
        ApiManager.getInstance.loadDataByParmars(ApiInterface.CAR__FRIENDSQUAN, map, object : HttpRespnse {
            override fun _onComplete() {

            }

            override fun _onNext(t: ResponseBody) {
                var domian = GsonUtil.GsonToBean(t.string(), SnsEntitydomain::class.java)
                if (null != domian && null != domian.data) {
                    var newslist = domian.data
                    if (null != newslist) {
                        isLoading = newslist.size <= 0
                        currtypage++
                        adapter?.setCommentList(newslist)
                    } else {

                    }
                }
            }

            override fun _onError(e: Throwable) {
            }
        })
    }

    private fun getMap(): Map<String, String> {
        val param = TreeMap<String, String>()
        param["attr"] = currentType
        param["u_id"] = BaseKasaoApplication.getUserId()
        param["user_id"] = BaseKasaoApplication.getUserId()
        param["page_num"] = currtypage.toString()
        return param

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DELET) {
            if (null != data) {
                var del = data.getBooleanExtra(ParmarsValue.KEY_WEATHER_DEL, false)
                if (del) {
                    adapter?.setRemove(index)
                }
            }
        }
    }
}
