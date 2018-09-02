package com.kasao.qintai.activity.user

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.kasao.qintai.R
import com.kasao.qintai.api.ApiInterface
import com.kasao.qintai.api.ApiManager
import com.kasao.qintai.model.domain.User
import com.kasao.qintai.model.domain.UserDomain
import com.kasao.qintai.util.GlideUtil
import com.kasao.qintai.util.ParmarsValue
import com.kasao.qintai.widget.CircleImageView
import com.kasao.qintaiframework.base.BaseActivity
import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.until.GsonUtil
import okhttp3.ResponseBody

class UserDetailActivity : BaseActivity(), View.OnClickListener {
    var viewBack: View? = null
    var tvTitle: TextView? = null
    var tvNickName: TextView? = null
    var avater: CircleImageView? = null
    var viewcall: View? = null
    var tvName: TextView? = null
    var tvSex: TextView? = null
    var tvAdress: TextView? = null
    var tvTel: TextView? = null
    private var userId: String = ""
    private var mUser: User? = null
    override fun onLayoutLoad(): Int {
        return R.layout.activity_user_detail
    }

    override fun findView() {
        viewBack = findViewById(R.id.ll_back)
        tvTitle = findViewById(R.id.tv_title)
        tvNickName = findViewById(R.id.tv_nickName)
        avater = findViewById(R.id.cirlceAvater)
        viewcall = findViewById(R.id.viewCall)
        tvName = findViewById(R.id.tvUserName)
        tvSex = findViewById(R.id.tvUseSex)
        tvAdress = findViewById(R.id.tvUserAdress)
        tvTel = findViewById(R.id.tvUserTel)


        userId = intent.getStringExtra(ParmarsValue.KEY_STR)
        if (TextUtils.isEmpty(userId)) {
            finish()
            return
        }
        tvTitle?.text = (getString(R.string.user_info))
        viewBack?.setOnClickListener(this)
        viewcall?.setOnClickListener(this)
    }

    override fun rendView() {
        if (null != mUser) {
            if (TextUtils.isEmpty(mUser?.user_mobile)) {
                finish()
                return
            }
            tvNickName?.text = (if (TextUtils.isEmpty(mUser?.nickname)) "无名" else mUser?.nickname)
            tvName?.text = (if (TextUtils.isEmpty(mUser?.user_name)) "" else mUser?.user_name)
            tvSex?.text = (if (TextUtils.isEmpty(mUser?.user_sex)) "男" else mUser?.user_sex)
            tvAdress?.text = (if (TextUtils.isEmpty(mUser?.user_company)) "保密" else mUser?.user_company)
            tvTel?.text = (if (TextUtils.isEmpty(mUser?.user_mobile)) "" else mUser?.user_mobile)
            if (TextUtils.isEmpty(mUser?.user_img)) {
                avater?.setImageResource(R.drawable.bg_default)
            } else {
                GlideUtil.into(this, mUser?.user_img, avater, R.drawable.default_avater)
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.getId()) {
            R.id.ll_back -> finish()
            R.id.viewCall -> {
                callPerssion(this, tvTel?.text.toString())
            }
        }
    }

    override fun onloadData() {
        var map = HashMap<String, String>()
        map["user_id"] = userId
        ApiManager.getInstance.loadDataByParmars(ApiInterface.USERINFO, map, object : HttpRespnse {
            override fun _onComplete() {

            }

            override fun _onNext(t: ResponseBody) {
                var usedomain = GsonUtil.GsonToBean(t.string(), UserDomain::class.java)
                if (null != usedomain && null != usedomain.data) {
                    mUser = usedomain.data
                    rendView()
                }
            }

            override fun _onError(e: Throwable) {

            }
        })
    }
}
