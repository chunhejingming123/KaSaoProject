package com.kasao.qintai.api

import android.text.TextUtils
import com.kasao.qintaiframework.http.UriConsts

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/21 0021:14
 * 存放接口
 */

class ApiInterface {
    companion object {
        // 首页数据
        val homeMaindata = "/Api/CarshopApi/newgetIndex"
        // 按品牌获取系列
        val carSystemById = "/Api/CarshopApi/newgetCarlist"
        //系类详情页面
        val carSystemDetail = "/Api/CarshopApi/newgetCarsetinfo"
        //询底价 获取价格 人数
        val xundijia = "/Api/CarshopApi/getconsultcount";
        //询底价 留下用户信息
        val leavePhone = "/Api/CarshopApi/setCarconsult"
        // 卡车详情
        val CAR_DATAI = "/Api/CarshopApi/getcargoodsinfo"
        // 收藏 汽车
        val CAR_STORE = "/Api/CarshopApi/setCarFavorite"
        //卡车详情链接
        val LINK_CAR_DETAI = "/Api/CarshopApi/getCarDetail?goods_id="

        //浏览过车的信息 用于推荐
        val CAR_BROWSE = "/Api/CarshopApi/setGoodsbrowse"
        // 卡车参数详情
        val CAR_PARAMETER = "/Api/CarshopApi/getParameter"
        //门店列表
        val CAR_SHOP = "/Api/CarSotreApi/getCarSotrelist"
        //门店详情
        var CAR_STORE_DETAIL = "/Api/CarSotreApi/getCarSotreitem"
        // 提交预订单信息
        var SUBMIT_ONLINE_BUY = "/Api/CarsubscribeApi/setCarsubscribe"
        // 汽贸品牌列表
        val CAR_BRAND = "/Api/CarshopApi/getBrandlist"

        // 汽贸搜索列表
        val CAR_LIST = "/Api/CarshopApi/getCarshoplist"
        // 获取车价格间断
        val CAR_MONEY_DURING = "/Api/CarshopApi/getCarPrice"
        //关于煤客
        val ABOUTMEIKE = "/Api/PublicApi/aboutMeikeApi?id=1"

        //公告列表
        val NOTICELIST = "/Api/AppApi/getNoticeListApi"
        // 预订单列表
        var ORDER_LIST = "/Api/CarsubscribeApi/getCarsubscribeitem"

        //意见反馈
        val OPINION = "/Api/PublicApi/feedBackApi"
        //汽贸朋友圈
        val CAR__FRIENDSQUAN = "/Api/CarAircleApi/getAircleList"
        //修改个人信息
        val MODEIFYMY = "/Api/UserApi/updateUserApi"
        // 获取用户信息接口
        val USERINFO = "/Api/UserApi/getUserInfoApi"

        // 汽贸圈点赞
        val CAR_ZAN = "/Api/CarAircleApi/clickAircleLikes"

        //举报
        var REPORTMSG = "/Api/AircleApi/clickAircleReport"
        //获取举报信息
        var GETREPORTMSG = "/Api/AircleApi/getAricleReportinfo"
        // 删除汽贸圈
        val DELETE_CAR_CICLE = "/Api/CarAircleApi/delAircle"
        val CAR_BROWAWCOUNT = "/Api/CarAircleApi/postAirclebrower"
        // 获取汽贸圈评论点赞接口
        val CAR_COMMENT_LIKE = "/Api/CarAircleApi/getNewAritcleinfo"
        //汽贸圈发评论
        val CAR_SETFRIENDSPINGLUN = "/Api/CarAircleApi/clickAircleReview"
        //汽贸圈回复接口
        val CAR_FRIEMDREPLEY = "/Api/CarAircleApi/postarticlereply"
        //删除汽贸圈自己的评论
        var DELETE_CAR_ONECOMMENT = "/Api/CarAircleApi/delAircleReview"
        //汽贸圈浏览记录
        val CAR_LOOKCOUNT = "/Api/CarAircleApi/getbrowselist"

        //获取汽贸圈点赞列表
        val CAR_ZANS = "/Api/CarAircleApi/getLikesList"
        //获取短信验证码
        var VERIFICATIONCODE = "/Api/SmsApi/sendSms"
        // 验证码登录
        var VERIFICATLOGIN = "/Api/SmsApi/checkSms"
        //登录
        val LOGIN = "/Api/UserApi/userLoginApi"
        //注册
        val AGAIST = "/Api/UserApi/createUserApi"
        //忘记密码
        val FORGETPASSWORD = "/Api/PublicApi/findPasswordApi"
        // 获取卡车详情分享的地址
        var ShareCarDetailUrl = "/Wap/Carshop/cardetails?goods_id="
        //发布朋友圈
        val FABUFRIENDS = "/Api/AircleApi/addAircleList"
        //版本更新
        val UPVERSION = "/Api/Common/updateEdition"

        fun getRequestUrl(url: String): String {
            var buff = StringBuffer(UriConsts.BASE_URL)
            if (!TextUtils.isEmpty(url)) {
                buff.append(url)
            }
            return buff.toString()
        }
    }
}