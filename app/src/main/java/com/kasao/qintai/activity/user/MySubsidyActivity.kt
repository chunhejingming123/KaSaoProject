package com.kasao.qintai.activity.user

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.kasao.qintai.R
import com.kasao.qintai.api.ApiInterface
import com.kasao.qintai.api.ApiManager
import com.kasao.qintai.base.BaseKasaoApplication
import com.kasao.qintai.model.Subsidie
import com.kasao.qintai.model.domain.Subsidiedomain
import com.kasao.qintaiframework.base.BaseActivity
import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.until.GsonUtil
import kotlinx.android.synthetic.main.activity_my_subsidy.*
import okhttp3.ResponseBody

class MySubsidyActivity : BaseActivity() {
    var mSubsidie:Subsidie?=null
    override fun onLayoutLoad(): Int {
        return R.layout.activity_my_subsidy
    }

    override fun findView() {
        findViewById<View>(R.id.viewBack).setOnClickListener { finish() }
        findViewById<ImageView>(R.id.ivBack).setImageResource(R.drawable.icon_return_black)
        findViewById<TextView>(R.id.tvTitle).text = getString(R.string.titleSubsidy)


    }

    override fun onloadData() {
        var map = HashMap<String, String>()
        map["u_id"] = BaseKasaoApplication.getUser().user_id
        ApiManager.getInstance.getDataByParmars(ApiInterface.My_bullter, map, object : HttpRespnse {
            override fun _onComplete() {

            }

            override fun _onNext(t: ResponseBody) {

                var domain = GsonUtil.GsonToBean(t.string(), Subsidiedomain::class.java)
                if (null!=domain&&null!=domain.data){
                    mSubsidie=domain.data
                    rendView()
                }


            }

            override fun _onError(e: Throwable) {

            }
        })
    }

    override fun rendView() {
      if(null!=mSubsidie){
          tvDate.text="领取日期"+mSubsidie?.create_time
          tvTip.visibility=View.VISIBLE
          tvEmpty.visibility=View.GONE
          if(mSubsidie!!.over){
           iv?.setImageResource(R.drawable.activity_bg5)
          }else{
              iv?.setImageResource(R.drawable.active_bg3)
          }
      }
    }

}
