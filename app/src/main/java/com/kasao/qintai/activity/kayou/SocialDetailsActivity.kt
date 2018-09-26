package com.kasao.qintai.activity.kayou

import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import com.kasao.qintai.R
import com.kasao.qintai.activity.user.UserDetailActivity
import com.kasao.qintai.adapter.SocialDetailAdapter
import com.kasao.qintai.api.ApiInterface
import com.kasao.qintai.api.ApiManager
import com.kasao.qintai.base.BaseKasaoApplication
import com.kasao.qintai.dialoge.DialogeDelComment
import com.kasao.qintai.dialoge.DialogeDelDynamic
import com.kasao.qintai.dialoge.DialogeSendMsg
import com.kasao.qintai.model.CommentEntity
import com.kasao.qintai.model.RtnSuss
import com.kasao.qintai.model.SNSEntity
import com.kasao.qintai.model.domain.CommentLikedomain
import com.kasao.qintai.model.domain.CommentRetundomain
import com.kasao.qintai.model.domain.Zandomain
import com.kasao.qintai.util.ContextComp
import com.kasao.qintai.util.ParmarsValue
import com.kasao.qintaiframework.base.BaseActivity
import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.until.GsonUtil
import com.kasao.qintaiframework.until.OnTimeClickDuring
import com.kasao.qintaiframework.until.ToastUtil
import okhttp3.ResponseBody
import java.util.*

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/28 0028:15
 */

class SocialDetailsActivity : BaseActivity() {
    var viewBack: View? = null
    var viewDel: View? = null
    var tvPrise: TextView? = null
    var tvBrowse: TextView? = null
    var viewComment: View? = null
    var root: View? = null
    var recycleView: RecyclerView? = null
    var snsentity: SNSEntity? = null
    var position = 0
    var id: String? = ""

    private var adapter: SocialDetailAdapter? = null
    private var commentEntity: CommentEntity? = null
    private var origin: MutableList<CommentEntity>? = null
    private var isRepley = false
    // 能否删除
    private var isCanDel: Boolean? = false
    // 发布评论成功后 返回 id;
    private var reParentId: String? = null
    private var isBrowse: Boolean = false
    private var borowseCount: Int = 0
    private var zanCount: Int = 0
    private var nickNmae: String? = null
    private var mBackEnable = false
    private val mIsBtnBack = false
    private var rootBottom = Integer.MIN_VALUE
    private val mOnGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val r = Rect()
        root!!.getGlobalVisibleRect(r)
        // 进入Activity时会布局，第一次调用onGlobalLayout，先记录开始软键盘没有弹出时底部的位置
        if (rootBottom == Integer.MIN_VALUE) {
            rootBottom = r.bottom
            return@OnGlobalLayoutListener
        }
        // adjustResize，软键盘弹出后高度会变小
        if (r.bottom < rootBottom) {//打开
            mBackEnable = false
        } else {
            mBackEnable = true//关闭
            if (mIsBtnBack) {
                finish()
            }
        }
    }

    override fun onLayoutLoad(): Int {
        return R.layout.activity_social_detail
    }

    override fun findView() {
        viewBack = findViewById(R.id.ll_back)
        viewDel = findViewById(R.id.ll_del)
        tvPrise = findViewById(R.id.sns_item_praise)
        tvBrowse = findViewById(R.id.liulangcishu)
        viewComment = findViewById(R.id.viewComment)
        root = findViewById(R.id.root)
        findViewById<View>(R.id.ll_del).setOnClickListener {
            val dynami = DialogeDelDynamic(this@SocialDetailsActivity)
            dynami.showDialoge(object : DialogeDelDynamic.ODialogeDel {
                override fun del() {
                    delComment()
                }
            })
        }
        viewBack?.setOnClickListener { finish() }
        tvPrise?.setOnClickListener { onThumble() }
        viewComment?.setOnClickListener {
            isRepley = false
            sendMsg()
        }
        rendView()
    }

    private fun delComment() {
        val map = HashMap<String, String>()
        map["af_id"] = id!!
        ApiManager.getInstance.loadDataByParmars(ApiInterface.DELETE_CAR_CICLE, map, object : HttpRespnse {
            override fun _onComplete() {
            }

            override fun _onNext(t: ResponseBody) {
                var rtn = GsonUtil.GsonToBean(t.string(), RtnSuss::class.java)
                if (null != rtn && rtn.code=="200") {
                    val intent = Intent()
                    intent.putExtra(ParmarsValue.KEY_WEATHER_DEL, true)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }

            override fun _onError(e: Throwable) {
            }
        })
    }

    internal var msdgDialoge: DialogeSendMsg? = null
    private fun sendMsg() {
        if (!OnTimeClickDuring.getInstance().onTickTimeChange(System.currentTimeMillis(), 500)) {
            return
        }
        msdgDialoge = DialogeSendMsg(this)
        msdgDialoge?.showDialoge(nickNmae, isRepley)

        msdgDialoge?.setSendPrice(object : DialogeSendMsg.ReplyOrSend {
            override fun send(msg: String) {
                title = msg
                if (isRepley) {
                    onRepley()
                } else {
                    setComment()
                }
                msdgDialoge?.hide()
                isRepley = false
            }
        })

    }

    override fun rendView() {
        root?.getViewTreeObserver()?.addOnGlobalLayoutListener(mOnGlobalLayoutListener)
        val uri = intent.data
        if (uri != null) {
            id = uri.getQueryParameter("id")
        }
        if (TextUtils.isEmpty(id)) {
            var sns = intent.getSerializableExtra("data") as SNSEntity
            if (null != sns) {
                snsentity = sns
            }
            id = intent.getStringExtra("id")
        }
        if (TextUtils.isEmpty(id)) {
            finish()
            return
        }
        recycleView = findViewById(R.id.recycleView)
        isCanDel = intent.getBooleanExtra(ParmarsValue.KEY_CAN_DEL, false)
        if (null != snsentity) {
            isBrowse = snsentity!!.browse_type
            adapter = SocialDetailAdapter(this, snsentity!!)
        } else {
            adapter = SocialDetailAdapter(this)
        }
        initRecycle(recycleView!!, null)
        viewDel?.visibility = (if (isCanDel!!) View.VISIBLE else View.GONE)

        recycleView?.adapter = adapter
        adapter?.setAction(object : SocialDetailAdapter.ISocialDeAction {
            override fun setReply(comment: CommentEntity, positions: Int) {
                isRepley = true
                if (TextUtils.isEmpty(comment.nickname)) {
                    nickNmae = comment.user_mobile
                } else {
                    nickNmae = comment.nickname
                }
                commentEntity = comment
                position = positions
                sendMsg()

            }

            override fun onToUserDetail(uid: String) {
                val intent = Intent(this@SocialDetailsActivity, UserDetailActivity::class.java)
                intent.putExtra(ParmarsValue.KEY_STR, uid)
                startActivity(intent)
            }

            override fun onHistory(id: String, browseCount: Int, zanCount: Int) {
                val intent = Intent(this@SocialDetailsActivity, VisitorActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("history", browseCount)
                intent.putExtra("zan", zanCount)
                startActivity(intent)
            }

            override fun onDelete(id: String, positions: Int) {
                if (positions > -1) {
                    position = positions
                    val dynami = DialogeDelComment(this@SocialDetailsActivity)
                    dynami.showDialoge(object : DialogeDelComment.ODialogeDel {
                        override fun del() {
                            delOneComment(id)
                        }
                    })
                }
            }
        })

        // 是否记录过浏览次数
        if (!isBrowse) {
            sendBrowseCount()
        }
    }

    // 删除自己的评论
    private fun delOneComment(id: String) {
        val map = HashMap<String, String>()
        map["id"] = id
        ApiManager.getInstance.loadDataByParmars(ApiInterface.DELETE_CAR_ONECOMMENT, map, object : HttpRespnse {
            override fun _onComplete() {
            }

            override fun _onNext(t: ResponseBody) {
                var rtnSuss = GsonUtil.GsonToBean(t.string(), RtnSuss::class.java)
                if (null != rtnSuss && !TextUtils.isEmpty(rtnSuss.msg)) {
                    adapter?.removeContentItem(position)
                    ToastUtil.showAlter(rtnSuss.msg)
                }
            }

            override fun _onError(e: Throwable) {
            }
        })
    }

    override fun onloadData() {
        var map = HashMap<String, String>()
        map["id"] = id!!
        ApiManager.getInstance.loadDataByParmars(ApiInterface.CAR_COMMENT_LIKE, map, object : HttpRespnse {
            override fun _onComplete() {

            }

            override fun _onNext(t: ResponseBody) {
                var commentLikedomain = GsonUtil.GsonToBean(t.string(), CommentLikedomain::class.java)
                adapter?.setCommentLikes(commentLikedomain)
                reRendView(commentLikedomain)
                if (null != commentLikedomain?.data) {
                    origin = commentLikedomain?.data
                }
            }

            override fun _onError(e: Throwable) {
            }
        })
    }

    private fun reRendView(commentLikedomain: CommentLikedomain?) {
        if (null == commentLikedomain) {
            return
        }
        if (null != commentLikedomain.detail) {
            snsentity = commentLikedomain.detail
        }
        if (null != commentLikedomain.browse) {
            tvBrowse?.text = commentLikedomain.browse.size.toString()
            borowseCount = commentLikedomain.browse.size
        } else {
            tvBrowse?.text = "1"
            borowseCount = 1
        }
        if (null != commentLikedomain.likes) {
            zanCount = commentLikedomain.likes.size
        } else {
            zanCount = 0
        }
        val drawable: Drawable
        if (snsentity?.type.equals("2")) {
            drawable = ContextComp.getDrawable(R.drawable.icon_thumb_normal)
            tvPrise?.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
            tvPrise?.text="赞"
            tvPrise?.setTextColor(resources.getColor(R.color.color_666666))
        } else {
            drawable = ContextComp.getDrawable(R.drawable.icon_thumb_press)
            tvPrise?.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
            tvPrise?.text="已赞"
            tvPrise?.setTextColor(resources.getColor(R.color.color_ee303c))
        }
    }

    // 发送浏览记录
    private fun sendBrowseCount() {
        val map = HashMap<String, String>()
        map["article_id"] = id!!
        map["u_id"] = BaseKasaoApplication.getUser().user_id
        ApiManager.getInstance.loadDataByParmars(ApiInterface.CAR_BROWAWCOUNT, map, object : HttpRespnse {
            override fun _onComplete() {
            }

            override fun _onNext(t: ResponseBody) {
            }

            override fun _onError(e: Throwable) {
            }
        })
    }

    // 点赞接口  处理更新相关的点赞数据
    fun onThumble() {
        if (null == snsentity) {
            return
        }
        val paramzan = HashMap<String, String>()
        paramzan["u_id"] = BaseKasaoApplication.getUser().user_id
        paramzan["af_id"] = snsentity!!.id
        paramzan["applies"] = "android"
        ApiManager.getInstance.loadDataByParmars(ApiInterface.CAR_ZAN, paramzan, object : HttpRespnse {
            override fun _onComplete() {
            }

            override fun _onNext(t: ResponseBody) {
                var zandomain = GsonUtil.GsonToBean(t.string(), Zandomain::class.java)
                if (null != zandomain) {
                    val type = zandomain.type
                    if (null != snsentity) {
                        snsentity?.type = type
                        snsentity?.likes_count = (zandomain.count)
                    }
                    if (type == "2") {
                        val drawable = ContextComp.getDrawable(R.drawable.icon_thumb_normal)
                        tvPrise?.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                        tvPrise?.text = ("赞")
                        tvPrise?.setTextColor(resources.getColor(R.color.color_666666))
                        adapter?.resetZanNum(-1)
                    } else {
                        val drawable = ContextComp.getDrawable(R.drawable.icon_thumb_press)
                        tvPrise?.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                        tvPrise?.text = ("已赞")
                        tvPrise?.setTextColor(resources.getColor(R.color.color_ee303c))
                        adapter?.resetZanNum(1)
                    }
                }
            }

            override fun _onError(e: Throwable) {

            }
        })

    }

    // 回复接口
    internal var title: String = ""
    internal var buildparentid: String = ""

    // 发布评论
    fun setComment() {
        if (TextUtils.isEmpty(title)) {
            ToastUtil.showAlter("说点什么吧")
        } else {
            var params = HashMap<String, String>()
            params["u_id"] = BaseKasaoApplication.getUser().user_id//"3942
            params["af_id"] = id!!
            params["applies"] = "android"
            params["title"] = title
            var entity = CommentEntity()
            entity.u_id = BaseKasaoApplication.getUser().user_id//"3942
            entity.title = title
            entity.parent_id = "0"
            entity.isMine = true

            entity.setNickname(BaseKasaoApplication.getUser().user_name)

            ApiManager.getInstance.loadDataByParmars(ApiInterface.CAR_SETFRIENDSPINGLUN, params, object : HttpRespnse {
                override fun _onComplete() {
                    isRepley = false
                }

                override fun _onNext(t: ResponseBody) {
                    var comnreturndomain = GsonUtil.GsonToBean(t.string(), CommentRetundomain::class.java)
                    if (null != comnreturndomain) {
                        adapter?.setOneComment(entity)
                        reParentId = comnreturndomain.parent_id
                        if (null == origin) {
                            origin = ArrayList()
                        }
                        entity.id = reParentId
                        origin?.add(entity)
                    } else {
                        ToastUtil.showAlter(getString(R.string.send_error))
                    }
                    isRepley = false

                }

                override fun _onError(e: Throwable) {
                    isRepley = false
                }
            })
        }
    }

    //回复
    fun onRepley() {
        if (TextUtils.isEmpty(title)) {
            ToastUtil.showAlter("说点什么吧")
            return
        }
        val repley = HashMap<String, String>()
        repley["article_id"] = id!!
        repley["u_id"] = BaseKasaoApplication.getUser().user_id
        var rename = if (TextUtils.isEmpty(commentEntity?.getNickname())) commentEntity?.user_mobile else commentEntity?.getNickname()
        repley["re_name"] = rename!!
        if (commentEntity!!.isMine) {
            buildparentid = reParentId!!
        } else {
            if ("0" == commentEntity?.parent_id) {
                buildparentid = commentEntity!!.id
            } else {
                buildparentid = commentEntity!!.parent_id
            }
        }
        repley["parent_id"] = buildparentid
        repley["title"] = title
        repley["applies"] = "android"
        repley["remove_id"] = commentEntity!!.u_id
        ApiManager.getInstance.loadDataByParmars(ApiInterface.CAR_FRIEMDREPLEY, repley, object : HttpRespnse {
            override fun _onComplete() {
                isRepley = false
            }

            override fun _onNext(t: ResponseBody) {
                var rtn = GsonUtil.GsonToBean(t.string(), RtnSuss::class.java)
                if (rtn != null && rtn.code == "200") {
                    rebuildComment()
                }
                isRepley = false
            }

            override fun _onError(e: Throwable) {
                isRepley = false
            }
        })
    }

    private fun rebuildComment() {
        if (null != origin) {
            var index = 0
            val size = origin!!.size
            if (commentEntity!!.parent_id.equals("0")) {
                for (i in 0..size - 1) {
                    if (commentEntity!!.id.equals(origin!!.get(i).id)) {
                        index = i
                        break
                    }
                }
            } else {
                for (i in 0..size - 1) {
                    if (commentEntity!!.parent_id.equals(origin!!.get(i).id)) {
                        index = i
                        break
                    }
                }
            }
            val repleyentity = CommentEntity()
            repleyentity.parent_id = buildparentid//commentEntity.id;
            repleyentity.type = 2
            repleyentity.title = title
            repleyentity.u_id = commentEntity!!.u_id
            val nickName = if (TextUtils.isEmpty(BaseKasaoApplication.getUser().nickname)) BaseKasaoApplication.getUser().user_mobile
            else BaseKasaoApplication.getUser().nickname
            repleyentity.nickname = nickName
            repleyentity.rm_id = if (TextUtils.isEmpty(commentEntity!!.nickname)) commentEntity!!.user_mobile else commentEntity!!.nickname
            repleyentity.setUser_img(BaseKasaoApplication.getUser().user_img)
            repleyentity.create_time = System.currentTimeMillis().toString() + ""
            val reset = origin!!.get(index)
            if (null != reset.child) {
                reset.child.add(repleyentity)
            } else {
                val list = LinkedList<CommentEntity>()
                list.add(repleyentity)
                reset.child = list
            }
            adapter?.resetRepley(origin)
        }

    }
}