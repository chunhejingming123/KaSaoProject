package com.kasao.qintai.widget

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.kasao.qintai.R
import com.kasao.qintai.util.ContextComp

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/20 0020:10
顶部标题栏
 */

class TopBar(resDrawabaleLeft: Int, titleText: String, resDrawabaleRight: Int,strRight:String, activity: View) : View.OnClickListener {
    var resDrawabaleLeft: Int = 0
    var titleText: String = ""
    var resDrawabaleRight: Int = 0

    var titleView: TextView? = null
    var leftView: View? = null
    var leftIcon: ImageView? = null
    var viewRight: FrameLayout? = null

    init {
        this.resDrawabaleLeft = resDrawabaleLeft
        this.titleText = titleText
        this.resDrawabaleRight = resDrawabaleRight
        initView(activity)
    }

    fun initView(activity: View) {
        //R.layout.view_title
        titleView = activity.findViewById(R.id.tvTitle)
        leftView = activity.findViewById(R.id.viewBack)
        leftIcon = activity.findViewById(R.id.ivBack)
        viewRight = activity.findViewById(R.id.viewRight)

        titleView?.text = titleText

        if (resDrawabaleLeft != 0) {
            leftIcon?.setBackgroundResource(resDrawabaleLeft)
            leftView?.setOnClickListener(this)
        }
        if (resDrawabaleRight != 0) {
            var drawable = ContextComp.getDrawable(resDrawabaleRight)
            var img = ImageView(activity.context)
            img.scaleType = ImageView.ScaleType.CENTER_CROP
            img.setImageDrawable(drawable)
            viewRight?.addView(img)
            viewRight?.setOnClickListener(this)
        }

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.viewRight -> mAction?.right()
            R.id.viewBack -> mAction?.left()
        }
    }

    var mAction: IActionBar? = null
    fun setIAction(iAction: IActionBar) {
        mAction = iAction
    }

    public interface IActionBar {
        fun left()
        fun right()
    }
}