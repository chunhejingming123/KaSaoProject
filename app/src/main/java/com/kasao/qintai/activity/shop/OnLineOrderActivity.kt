package com.kasao.qintai.activity.shop

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.kasao.qintai.R
import com.kasao.qintai.api.ApiInterface
import com.kasao.qintai.api.ApiManager
import com.kasao.qintai.base.BaseKasaoApplication
import com.kasao.qintai.model.CarDetailEntity
import com.kasao.qintai.model.domain.Promodomain
import com.kasao.qintai.util.ParmarsValue
import com.kasao.qintaiframework.base.BaseActivity
import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.until.GsonUtil
import com.kasao.qintaiframework.until.ToastUtil
import okhttp3.ResponseBody
import java.util.*

class OnLineOrderActivity : BaseActivity(), View.OnClickListener {
    var etName: EditText? = null
    var etPhone: EditText? = null
    var tvLoae: TextView? = null//贷款
    var tvPay: TextView? = null// 全款
    var tvSevenDay: TextView? = null
    var tvOneMonth: TextView? = null
    var tvThreeMonth: TextView? = null
    var tvNum: TextView? = null
    var tvDes: TextView? = null
    var tvOrderNum: TextView? = null
    private var timeArray: Array<TextView>? = null
    //
    var state: String = "1"//1新车贷款 2 全额付款
    var goodsid: String? = ""
    /**
     * adb
     * 1  7天内
     * 2 一个月
     * 3 3个月内 时间
     */
    private var trade_state = "1"
    var uid: String? = ""
    var car: CarDetailEntity? = null
    override fun onLayoutLoad(): Int {
        return R.layout.activity_online_order
    }

    override fun findView() {
        etName = findViewById(R.id.etName)
        etPhone = findViewById(R.id.etPhone)
        tvLoae = findViewById(R.id.tvLoae)
        tvPay = findViewById(R.id.tvPay)
        tvSevenDay = findViewById(R.id.tvSeventDay)
        tvOneMonth = findViewById(R.id.tvOneMonth)
        tvThreeMonth = findViewById(R.id.tvThreeMonth)
        tvNum = findViewById(R.id.tvNum)
        tvDes = findViewById(R.id.tvDes)
        tvOrderNum = findViewById(R.id.tvOrderNum)

        tvLoae?.setOnClickListener(this)
        tvPay?.setOnClickListener(this)
        tvSevenDay?.setOnClickListener(this)
        tvOneMonth?.setOnClickListener(this)
        tvThreeMonth?.setOnClickListener(this)
        findViewById<View>(R.id.tvMinus).setOnClickListener(this)
        findViewById<View>(R.id.tvPlug).setOnClickListener(this)
        findViewById<View>(R.id.viewBack).setOnClickListener(this)
        findViewById<View>(R.id.viewCall).setOnClickListener(this)
        findViewById<View>(R.id.viewBuy).setOnClickListener(this)

        rendView()
    }

    override fun rendView() {
        car = intent.extras.getSerializable(ParmarsValue.KEY_OBJ) as CarDetailEntity
        if (null != car) {
            tvDes?.setText(car!!.name)
            tvOrderNum?.setText("已有" + car!!.subscribecount + "人预订")
            goodsid = car!!.goods_id

        }
        timeArray = arrayOf<TextView>(tvSevenDay!!, tvOneMonth!!, tvThreeMonth!!)
        setBuyTime(0)
    }


    private fun setBuyTime(index: Int) {
        if (null != timeArray) {
            var length = timeArray!!.size - 1
            for (i in 0..length) {
                if (index == i) {
                    timeArray!![i].setBackgroundResource(R.drawable.bg_border2_fbe0e2)
                    trade_state = (i + 1).toString() + ""
                } else {
                    timeArray!![i].setBackgroundResource(R.drawable.bg_border6_dedede)
                }
            }
        }
    }

    private fun getMap(): Map<String, String>? {
        var map = HashMap<String, String>()
        uid = BaseKasaoApplication.getUser().user_id
        map["u_id"] = uid!!
        map["goods_id"] = goodsid!!
        if (TextUtils.isEmpty(tvNum?.text.toString())) {
            ToastUtil.showAlter(getString(R.string.alert_carbuynum))
            return null
        } else {
            map["goods_num"] = tvNum?.text.toString()
        }
        if (TextUtils.isEmpty(etName?.text.toString())) {
            ToastUtil.showAlter(getString(R.string.input_name))
            return null
        } else {
            map["name"] = etName?.text.toString()
        }
        if (TextUtils.isEmpty(etPhone?.text.toString()) || 11 != etPhone?.text.toString().length) {
            ToastUtil.showAlter(getString(R.string.input_moble))
            return null
        } else {
            map["phone"] = etPhone?.text.toString()
        }
        map["trade_state"] = trade_state
        map["state"] = state
        return map
    }

    fun submit() {
        var map = getMap()
        if (null == map) {
            return
        }
        ApiManager.getInstance.loadDataByParmars(ApiInterface.SUBMIT_ONLINE_BUY, map!!, object : HttpRespnse {
            override fun _onComplete() {

            }

            override fun _onNext(t: ResponseBody) {
                var domain = GsonUtil.GsonToBean(t.string(), Promodomain::class.java)
                if (domain?.flag.equals("success")) {
                    val bundle = Bundle()
                    bundle.putString(ParmarsValue.KEY_STR, domain?.promo)
                    bundle.putString(ParmarsValue.KEY_CID, car?.inphone)
                    startActivity(BookSuccessActivity::class.java, bundle)
                    finish()
                } else {
                    ToastUtil.showAlter(domain?.msg)
                }
            }

            override fun _onError(e: Throwable) {

            }
        })
    }

    private fun upDateNum(isadd: Boolean) {
        val str = tvNum?.getText().toString()
        if (TextUtils.isEmpty(str)) {
            return
        }
        var num = Integer.parseInt(str)
        if (isadd) {
            num++
        } else {
            num--
            if (num < 1) {
                return
            }
        }
        tvNum?.setText(num.toString())
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.viewBack -> finish()
            R.id.tvLoae -> {
                tvLoae?.setBackgroundResource(R.drawable.bg_border2_fbe0e2)
                tvPay?.setBackgroundResource(R.drawable.bg_border6_dedede)
                state = "1"
            }
            R.id.tvPay -> {
                tvPay?.setBackgroundResource(R.drawable.bg_border2_fbe0e2)
                tvLoae?.setBackgroundResource(R.drawable.bg_border6_dedede)
                state = "2"
            }
            R.id.tvPlug -> upDateNum(true)
            R.id.tvMinus -> upDateNum(false)
            R.id.tvSeventDay -> setBuyTime(0)
            R.id.tvOneMonth -> setBuyTime(1)
            R.id.tvThreeMonth -> setBuyTime(2)
            R.id.viewBuy -> submit()
            R.id.viewCall -> {
                if (null != car) {
                    callPerssion(this, car!!.inphone)
                }
            }

        }
    }

}