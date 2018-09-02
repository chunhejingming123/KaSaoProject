package com.kasao.qintai.activity.kayou

import android.view.View
import com.kasao.qintai.R
import com.kasao.qintai.api.ApiInterface
import com.kasao.qintai.api.ApiManager
import com.kasao.qintai.base.BaseKasaoApplication
import com.kasao.qintai.model.RtnSuss
import com.kasao.qintaiframework.base.BaseActivity
import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.until.GsonUtil
import com.kasao.qintaiframework.until.ToastUtil
import kotlinx.android.synthetic.main.activity_report_friend.*
import kotlinx.android.synthetic.main.include_view_titlebar.*
import okhttp3.ResponseBody

class ReportFriendActivity : BaseActivity(), View.OnClickListener {
    var rootViewItem = arrayOfNulls<View>(5)
    var dotView = arrayOfNulls<View>(5)
    var id: String = ""
    var reportstate: Int? = 0
    override fun onLayoutLoad(): Int {
        return R.layout.activity_report_friend
    }

    override fun findView() {
        var bundle = this.intent.extras
        id = bundle.getString("sid")
        rootViewItem[0] = viewInfo
        rootViewItem[1] = viewContent
        rootViewItem[2] = viewTel
        rootViewItem[3] = viewForce
        rootViewItem[4] = viewOther
        dotView[0] = dotInfo
        dotView[1] = dotContent
        dotView[2] = dotTel
        dotView[3] = dotForce
        dotView[4] = dotOther
        tvTitle.setText(getString(R.string.report))
        viewBack.setOnClickListener(this)
        tvSummbit.setOnClickListener(this)
        for (index in rootViewItem.indices) {
            rootViewItem[index]?.setOnClickListener(this)
        }
        rendDot(2)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.ll_back -> finish()
            R.id.viewInfo -> rendDot(0)
            R.id.viewContent -> rendDot(1)
            R.id.viewTel -> rendDot(2)
            R.id.viewForce -> rendDot(3)
            R.id.viewOther -> rendDot(4)
            R.id.tvSummbit -> sumbit()
        }
    }

    private fun rendDot(n: Int?) {
        reportstate = n;
        for (index in dotView.indices) {
            if (index == n) {
                dotView[index]?.visibility = View.VISIBLE

            } else {
                dotView[index]?.visibility = View.GONE
            }
        }
    }

    private fun sumbit() {
        var map = HashMap<String, String>()
        var uid = BaseKasaoApplication.getUserId()
        if (!uid.isNotEmpty() || !id.isNotEmpty())
            return
        map?.put("u_id", uid)
        map.put("reportstate", reportstate.toString())
        map.put("af_id", id)
        map.put("applies", "android")
        ApiManager.getInstance.loadDataByParmars(ApiInterface.REPORTMSG, map, object : HttpRespnse {
            override fun _onComplete() {
            }

            override fun _onNext(t: ResponseBody) {
                var doomain=GsonUtil.GsonToBean(t.string(),RtnSuss::class.java)
                finish()
            }

            override fun _onError(e: Throwable) {
            }
        })

    }

}
