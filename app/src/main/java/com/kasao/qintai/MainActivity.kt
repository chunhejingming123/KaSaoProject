package com.kasao.qintai

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentTransaction
import android.view.KeyEvent
import com.kasao.qintai.activity.login.LoginActivity
import com.kasao.qintai.api.ApiInterface
import com.kasao.qintai.api.ApiManager
import com.kasao.qintai.base.BaseKasaoApplication
import com.kasao.qintai.dialoge.DialogeCarActivite
import com.kasao.qintai.dialoge.ReportAlertDialoge
import com.kasao.qintai.dialoge.ShareDialog
import com.kasao.qintai.fragment.main.HomeFrament
import com.kasao.qintai.fragment.social.SocialFragment
import com.kasao.qintai.fragment.user.UserFragment
import com.kasao.qintai.model.RtnSuss
import com.kasao.qintai.model.domain.Reportdomain
import com.kasao.qintai.util.PermissionsUtils
import com.kasao.qintai.util.UtilsTool
import com.kasao.qintai.version.VersionUpMananger
import com.kasao.qintaiframework.base.BaseActivity
import com.kasao.qintaiframework.bottomnavigation.BottomNavigationBar
import com.kasao.qintaiframework.bottomnavigation.BottomNavigationItem
import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.until.ActivityManager
import com.kasao.qintaiframework.until.GsonUtil
import com.kasao.qintaiframework.until.ToastUtil
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import okhttp3.ResponseBody

class MainActivity : BaseActivity(), BottomNavigationBar.OnTabSelectedListener {
    var homeFrament: HomeFrament? = null
    var socialFragment: SocialFragment? = null
    var userFragment: UserFragment? = null
    var bottomBar: BottomNavigationBar? = null

    var locationManager: LocationManager? = null
    var provider: String? = ""
    var location: Location? = null
    var locationListener: MyListener? = null
    var curentIndex = 0
    var isFromNotice = false//有无举报
    var afid: String = ""
    var reportid: String = ""
    override fun onLayoutLoad(): Int {
        return R.layout.activity_main
    }

    override fun findView() {
        //实例化布局对象
        val uri = intent.data
        if (uri != null) {
            afid = uri.getQueryParameter("id")
            reportid = uri.getQueryParameter("reportid")
            isFromNotice = true
        }
        bottomBar = findViewById(R.id.bottomBar)
        rendView()

    }

    override fun rendView() {
        bottomBar?.setMode(BottomNavigationBar.MODE_DEFAULT)
        bottomBar?.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
        bottomBar?.addItem(BottomNavigationItem(R.drawable.tab_main_selector, getString(R.string.tabmain)).setActiveColorResource(R.color.color_ee2e3b).setInActiveColorResource(R.color.color_cc5f5f5f))
                ?.addItem(BottomNavigationItem(R.drawable.tab_social_selector, getString(R.string.tabSocal)).setActiveColorResource(R.color.color_ee2e3b).setInActiveColorResource(R.color.color_cc5f5f5f))
                ?.addItem(BottomNavigationItem(R.drawable.tab_user_selector, getString(R.string.tabUser)).setActiveColorResource(R.color.color_ee2e3b).setInActiveColorResource(R.color.color_cc5f5f5f))
                ?.setFirstSelectedPosition(0)
                ?.initialise()
        bottomBar?.setTabSelectedListener(this)
        initFragment(0)
        location()
        if (isFromNotice) {
            //loadReport()
        } else {
            handler?.postDelayed(object : Runnable {
                override fun run() {
                    checkVersion()
                }
            }, 1500)

            showCarActivite()
        }
    }

    private fun loadReport() {
        var map = HashMap<String, String>()
        ApiManager.getInstance.getDataByParmars(ApiInterface.GETREPORTMSG, map, object : HttpRespnse {
            override fun _onComplete() {
            }

            override fun _onNext(t: ResponseBody) {
                var domain = GsonUtil.GsonToBean(t.string(), Reportdomain::class.java)
                var dialoe = ReportAlertDialoge(this@MainActivity)
                dialoe.showDialoge(domain)
            }

            override fun _onError(e: Throwable) {

            }
        })
    }


    fun initFragment(index: Int) {
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        hidtFragment(fragmentTransaction)
        when (index) {
            0 -> if (null == homeFrament) {
                homeFrament = HomeFrament()
                fragmentTransaction.add(R.id.container, homeFrament!!)
            } else {
                fragmentTransaction.show(homeFrament!!)
            }
            1 -> {
                if (null == socialFragment) {
                    socialFragment = SocialFragment()
                    fragmentTransaction.add(R.id.container, socialFragment!!)
                } else {
                    fragmentTransaction.show(socialFragment!!)
                }
            }
            2 -> if (null == userFragment) {
                userFragment = UserFragment()
                fragmentTransaction.add(R.id.container, userFragment!!)
            } else {
                fragmentTransaction.show(userFragment!!)
            }
        }
        fragmentTransaction.commitAllowingStateLoss()

    }

    fun hidtFragment(fragmentTransaction: FragmentTransaction) {
        if (null != homeFrament) {
            fragmentTransaction.hide(homeFrament!!)
        }
        if (null != socialFragment) {
            fragmentTransaction.hide(socialFragment!!)
        }
        if (null != userFragment) {
            fragmentTransaction.hide(userFragment!!)
        }
    }

    override fun onTabSelected(position: Int) {
        if (null == BaseKasaoApplication.getUser() && position == 1) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            bottomBar?.setFirstSelectedPosition(curentIndex)
                    ?.initialise()
            return
        }
        curentIndex = position
        initFragment(position)
    }

    override fun onTabUnselected(position: Int) {
    }

    override fun onTabReselected(position: Int) {

    }

    fun location() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = MyListener()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var criteria = Criteria();
            criteria.accuracy = Criteria.ACCURACY_COARSE
            criteria.isAltitudeRequired = false
            criteria.isBearingRequired = false
            criteria.isCostAllowed = true
            criteria.powerRequirement = Criteria.POWER_LOW
            provider = locationManager?.getBestProvider(criteria, true)
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PermissionsUtils.REQUEST_LOCATION)
                }
                return
            }
            location = locationManager?.getLastKnownLocation(provider)
            if (null != location) {
                BaseKasaoApplication.mLocation = location
            }
            location = locationManager?.getLastKnownLocation(provider)

            locationManager?.requestLocationUpdates(provider, 10000, 0f, locationListener)

        }
    }

    class MyListener : LocationListener {
        override fun onLocationChanged(p0: Location?) {
            BaseKasaoApplication.mLocation = p0

        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

        }

        override fun onProviderEnabled(p0: String?) {
        }

        override fun onProviderDisabled(p0: String?) {
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PermissionsUtils.REQUEST_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    location()
                } else {
                    ToastUtil.showAlter("开启定位权限")
                }
            }
            PermissionsUtils.REQUEST_EXTERNAL_STORAGE -> {
                if (null!=grantResults&&grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val mamange = VersionUpMananger(this)
                    mamange.checkUp(false)
                } else {
                }
            }
        }
    }

    fun checkVersion() {
        var versionManager = VersionUpMananger(this)
        versionManager.checkUp(false)
    }

    /**
     * 上一次按退出的时间
     */
    private var exitTime: Long = 0
    /**
     * 两次退出键间隔时间
     */
    internal var handler: Handler? = Handler()
    private val gap = 2000
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > gap) {
                ToastUtil.showAlter("再按一次退出程序")
                exitTime = System.currentTimeMillis()
            } else {
                handler?.postDelayed(Runnable {
                    ActivityManager.finishAllActivity()
                    finish()
                }, 200)

            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    // 检测有无活动
    // 卡车活动
    private fun showCarActivite() {
        ApiManager.getInstance.getDataByUrl(ApiInterface.CAR_ACTIVITE, object : HttpRespnse {
            override fun _onComplete() {
            }

            override fun _onNext(t: ResponseBody) {
                var rtn = GsonUtil.GsonToBean(t.string(), RtnSuss::class.java)
                if (rtn?.code == "200") {
                    showCarDialoge()
                }
            }

            override fun _onError(e: Throwable) {

            }
        })
    }

    private fun showCarDialoge() {
        val activite = DialogeCarActivite(this)
        activite.show()
        activite.setOnActivite(object : DialogeCarActivite.IJoinActivite {
            override fun onJoinActivite() {
                WXlanchMiniProgram()
            }
        })
    }

    // 跳转小程序
    internal fun WXlanchMiniProgram() {
        val appid = ShareDialog.APP_ID
        val isApk = UtilsTool.isApkInstall(this, appid)
        if (!isApk) {
            return
        }

        val api = WXAPIFactory.createWXAPI(this, appid)
        val req = WXLaunchMiniProgram.Req()
        req.userName = "gh_8636d6032d2d"
        req.path = "pages/index/carart/carart"
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE// 可选打开 开发版，体验版和正式版
        api.sendReq(req)
    }
}


