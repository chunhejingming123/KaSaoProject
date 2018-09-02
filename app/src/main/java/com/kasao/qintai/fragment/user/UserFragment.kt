package com.kasao.qintai.fragment.user

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kasao.qintai.R
import com.kasao.qintai.activity.user.*
import com.kasao.qintai.api.ApiInterface
import com.kasao.qintai.base.BaseKasaoApplication
import com.kasao.qintai.util.GlideUtil
import com.kasao.qintai.widget.CircleImageView
import com.kasao.qintaiframework.activity.WebActivity
import com.kasao.qintaiframework.base.BaseFragment

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/20 0020:09
 */

class UserFragment : BaseFragment(), View.OnClickListener {
    var viewStore: View? = null
    var viewCasre: View? = null
    var viewUs: View? = null
    var viewPrediect: View? = null
    var viewOption: View? = null
    var viewMsg: View? = null
    var dotline: View? = null
    var social: View? = null
    var avater: CircleImageView? = null
    var name: TextView? = null


    override fun onInflater(inflater: LayoutInflater, container: ViewGroup?): View {
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_user, container, false)
        }
        return rootView!!
    }

    override fun findView() {
        viewStore = rootView?.findViewById(R.id.viewStore)
        viewCasre = rootView?.findViewById(R.id.viewCare)
        viewUs = rootView?.findViewById(R.id.viewUs)
        viewPrediect = rootView?.findViewById(R.id.viewPredict)
        viewOption = rootView?.findViewById(R.id.viewOption)
        viewMsg = rootView?.findViewById(R.id.viewMsg)
        dotline = rootView?.findViewById(R.id.dotline)
        social = rootView?.findViewById(R.id.carSocial)
        name = rootView?.findViewById(R.id.name)
        avater = rootView?.findViewById(R.id.avater)

        avater?.setOnClickListener(this)
        viewStore?.setOnClickListener(this)
        viewCasre?.setOnClickListener(this)
        viewUs?.setOnClickListener(this)
        viewPrediect?.setOnClickListener(this)
        viewOption?.setOnClickListener(this)
        viewMsg?.setOnClickListener(this)
        social?.setOnClickListener(this)


    }

    override fun onResume() {
        super.onResume()
        rendView()
    }

    override fun rendView() {
        GlideUtil.into(activity, BaseKasaoApplication.getUser().user_img, avater, R.drawable.default_avater)
        name?.text = BaseKasaoApplication.getUser().nickname

    }

    override fun onClick(p0: View?) {
        var intent: Intent? = null
        when (p0?.id) {
            R.id.avater -> intent = Intent(activity, ModifyUserActivity::class.java)
            R.id.carSocial -> intent = Intent(activity, SocialActivity::class.java)
//            R.id.viewStore -> intent = Intent()
//            R.id.viewCare -> intent = Intent()
            R.id.viewUs -> {
                intent = Intent(activity, WebActivity::class.java)
                intent?.putExtra("title", "关于煤客")
                intent?.putExtra("url", ApiInterface.ABOUTMEIKE)

            }
            R.id.viewPredict -> intent = Intent(activity, MyBookActivity::class.java)
            R.id.viewOption -> intent = Intent(activity, OpinionActivity::class.java)
            R.id.viewMsg -> intent = Intent(activity, NoticeActivity::class.java)
        }
        if (null != intent) {
            startActivity(intent)
        }

    }
}