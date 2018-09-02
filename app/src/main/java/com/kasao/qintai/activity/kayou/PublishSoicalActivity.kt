package com.kasao.qintai.activity.kayou

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.TextAppearanceSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.kasao.qintai.R
import com.kasao.qintai.activity.ImageGridActivity
import com.kasao.qintai.adapter.SendAdapter
import com.kasao.qintai.api.ApiInterface
import com.kasao.qintai.api.ApiManager
import com.kasao.qintai.base.BaseKasaoApplication
import com.kasao.qintai.util.ContextComp
import com.kasao.qintai.util.ImageTools
import com.kasao.qintai.widget.seekBar.BubbleSeekBar
import com.kasao.qintaiframework.base.BaseActivity
import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.until.ToastUtil
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File

class PublishSoicalActivity : BaseActivity(), View.OnClickListener {
    private var mGridView: RecyclerView? = null
    private var mInput: EditText? = null
    private var mList: MutableList<String>? = null
    private var adapter: SendAdapter? = null
    private var seekbare01: BubbleSeekBar? = null
    private var seekbare02: BubbleSeekBar? = null
    private var viewConterBar01: View? = null
    private var viewContenBar02: View? = null
    private var tvForth: TextView? = null
    private var tvSix: TextView? = null
    var select: Drawable? = null
    var normal: Drawable? = null
    private var time = "1"// 发布定时时间1，2，3
    private var intval = "2"// 间隔 发布此时
    private var publishType = "4"  // 2 煤炭,3 物流,4 汽贸:
    private var map: java.util.HashMap<String, String>? = null
    override fun onLayoutLoad(): Int {
        return R.layout.activity_publish_soical
    }

    override fun findView() {
        findViewById<ImageView>(R.id.ivBack).setImageResource(R.drawable.icon_return_black)
        val tv_title = findViewById(R.id.tvTitle) as TextView
        tv_title.text = "发说说"
        val viewRight = findViewById<View>(R.id.viewRight) as View
        viewRight.setOnClickListener(this)
        val viewBack = findViewById<View>(R.id.viewBack).setOnClickListener { finish() }
        viewRight.setOnClickListener(this)
        val tv_right = findViewById(R.id.tvRight) as TextView
        tv_right.text = "发送"

        mGridView = findViewById(R.id.newsay_gv)
        mInput = findViewById(R.id.newsay_input)
        rendSeekBar()

        val gide = GridLayoutManager(this, 4)
        mGridView?.setLayoutManager(gide)
        mGridView?.setHasFixedSize(true)
        mList = ArrayList<String>()
        adapter = SendAdapter(this@PublishSoicalActivity, mList!!)
        mGridView?.adapter = adapter
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.viewRight -> {
                CloseKeyBoard()
                if (mInput?.text?.length == 0 && mList?.size == 0) {
                    ToastUtil.showAlter("说点什么吧")
                } else if (mInput!!.text!!.length > 300) {
                    ToastUtil.showAlter("最多只能300字")
                } else {
                    upLoadingData()
                }
            }
            R.id.tvFourTime -> {
                time = "1"
                tvForth?.setCompoundDrawables(null, null, select, null)
                tvSix?.setCompoundDrawables(null, null, normal, null)
                viewContenBar02?.visibility = (View.GONE)
                viewConterBar01?.visibility = (View.VISIBLE)
                val str = getString(R.string.foutthTime)
                val length1 = str.length
                val styledText1 = SpannableString(str)
                styledText1.setSpan(TextAppearanceSpan(this, R.style.span_color272626), 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                styledText1.setSpan(TextAppearanceSpan(this, R.style.span_colorcc272626), 8, length1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                tvForth?.setText(styledText1, TextView.BufferType.SPANNABLE)
                tvSix?.text = (getString(R.string.sixTime))
            }
            R.id.tvSixTime -> {
                time = "2"
                tvSix?.setCompoundDrawables(null, null, select, null)
                tvForth?.setCompoundDrawables(null, null, normal, null)
                viewContenBar02?.visibility = (View.VISIBLE)
                viewConterBar01?.visibility = (View.GONE)
                val str1 = getString(R.string.sixthTime)
                val length2 = str1.length
                val styledText2 = SpannableString(str1)
                styledText2.setSpan(TextAppearanceSpan(this, R.style.span_color272626), 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                styledText2.setSpan(TextAppearanceSpan(this, R.style.span_colorcc272626), 8, length2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                tvSix?.setText(styledText2, TextView.BufferType.SPANNABLE)
                tvForth?.setText(getString(R.string.foutth))
            }
        }
    }


    fun rendSeekBar() {
        select = ContextComp.getDrawable(R.drawable.icon_radio_select)
        normal = ContextComp.getDrawable(R.drawable.icon_radio_mormal)
        select?.setBounds(0, 0, select!!.getMinimumWidth(), select!!.getMinimumHeight()) //设置边界
        normal?.setBounds(0, 0, normal!!.getMinimumWidth(), normal!!.getMinimumHeight()) //设置边界
        seekbare01 = findViewById(R.id.seekbarFourth)
        seekbare02 = findViewById(R.id.seekbarsix)
        viewConterBar01 = findViewById(R.id.viewFourth)
        viewContenBar02 = findViewById(R.id.viewSix)

        tvForth = findViewById(R.id.tvFourTime)
        tvSix = findViewById(R.id.tvSixTime)
        tvForth?.setCompoundDrawables(null, null, select, null)
        tvForth?.setOnClickListener(this)
        tvSix?.setOnClickListener(this)
        seekbare01!!.getConfigBuilder()
                .min(2F)
                .max(10F)
                .progress(2F)
                .sectionCount(8)
                .trackColor(ContextComp.getColor(R.color.color_4A4A4A))
                .secondTrackColor(ContextComp.getColor(R.color.red))
                .thumbColor(ContextComp.getColor(R.color.blue))
                .showSectionText()
                .sectionTextColor(ContextComp.getColor(R.color.color_80272626))
                .sectionTextSize(12)
                .showThumbText()
                .thumbTextColor(ContextComp.getColor(R.color.color_666666))
                .thumbTextSize(12)
                .bubbleColor(ContextComp.getColor(R.color.color_272626))
                .bubbleTextSize(12)
                // .showSectionMark()
                .seekStepSection()
                .touchToSeek()// .trackSize(dp2px(15))
                .sectionTextPosition(BubbleSeekBar.TextPosition.BELOW_SECTION_MARK)
                .build()
        seekbare02!!.getConfigBuilder()
                .min(2F)
                .max(10F)
                .progress(2F)
                .sectionCount(8)
                .trackColor(ContextComp.getColor(R.color.color_4A4A4A))
                .secondTrackColor(ContextComp.getColor(R.color.red))
                .thumbColor(ContextComp.getColor(R.color.blue))
                .showSectionText()
                .sectionTextColor(ContextComp.getColor(R.color.color_80272626))
                .sectionTextSize(12)
                .showThumbText()
                .thumbTextColor(ContextComp.getColor(R.color.color_666666))
                .thumbTextSize(12)
                .bubbleColor(ContextComp.getColor(R.color.color_272626))
                .bubbleTextSize(12)
                // .showSectionMark()
                .seekStepSection()
                .touchToSeek()// .trackSize(dp2px(15))
                .sectionTextPosition(BubbleSeekBar.TextPosition.BELOW_SECTION_MARK)
                .build()

        seekbare01!!.setOnProgressChangedListener(object : BubbleSeekBar.OnProgressChangedListener {
            override fun onProgressChanged(bubbleSeekBar: BubbleSeekBar, progress: Int, progressFloat: Float) {
                intval = progress.toString()
            }

            override fun getProgressOnActionUp(bubbleSeekBar: BubbleSeekBar, progress: Int, progressFloat: Float) {

            }

            override fun getProgressOnFinally(bubbleSeekBar: BubbleSeekBar, progress: Int, progressFloat: Float) {

            }
        })
        seekbare02!!.setOnProgressChangedListener(object : BubbleSeekBar.OnProgressChangedListener {
            override fun onProgressChanged(bubbleSeekBar: BubbleSeekBar, progress: Int, progressFloat: Float) {
                intval = progress.toString()
            }

            override fun getProgressOnActionUp(bubbleSeekBar: BubbleSeekBar, progress: Int, progressFloat: Float) {

            }

            override fun getProgressOnFinally(bubbleSeekBar: BubbleSeekBar, progress: Int, progressFloat: Float) {

            }
        })
    }

    /**
     * 关闭软键盘
     */
    fun CloseKeyBoard() {
        val view = getWindow().peekDecorView()
        if (view != null) {
            val inputmanger = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputmanger.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
        }
    }


    private fun upLoadingData() {
        val map = HashMap<String, RequestBody>()
        map["attr"] = toRequestBody(publishType)
        map["u_id"] = toRequestBody(BaseKasaoApplication.getUser().user_id)
        map["title"] = toRequestBody(mInput?.text.toString())
        map["applies"] = toRequestBody("android")
        map["timing"] = toRequestBody("true")
        map["time"] = toRequestBody(time)
        map["intval"] = toRequestBody(intval)

        for (i in mList!!.indices) {
            val bitmap = ImageTools.getimage(mList!!.get(i))
            val path = ImageTools.saveBitmapToSDcard(this, bitmap)
            if (!TextUtils.isEmpty(path)) {
                val file = File(path)
                map["img[" + i + "]\";filename=\"" + file.getName()] = RequestBody.create(MediaType.parse("image/*"), file)
            }
        }
        ApiManager.getInstance.upLoadData(ApiInterface.FABUFRIENDS, map, object : HttpRespnse {
            override fun _onComplete() {

            }

            override fun _onNext(t: ResponseBody) {
                setResult(RESULT_OK)
                finish()
            }

            override fun _onError(e: Throwable) {

            }
        })

    }

    fun toRequestBody(value: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), value)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 5) {
            map = data?.getSerializableExtra(ImageGridActivity.IMAGE_MAP) as java.util.HashMap<String, String>
            for (str in map!!.values) {
                mList?.add(str)
            }
            adapter?.setDatalist(mList!!)
        } else if (resultCode == 5 && requestCode == 5) {
            val path = data?.getStringExtra("path")
            mList?.add(path!!)
            adapter?.setDatalist(mList!!)
        }
    }
}
