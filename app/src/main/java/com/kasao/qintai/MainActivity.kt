package com.kasao.qintai

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import com.kasao.qintai.api.ApiManager
import com.kasao.qintai.base.BaseKasaoApplication
import com.kasao.qintai.fragment.main.HomeFrament
import com.kasao.qintai.fragment.social.SocialFragment
import com.kasao.qintai.fragment.user.UserFragment
import com.kasao.qintai.model.domain.UserDomain
import com.kasao.qintai.util.PermissionsUtils
import com.kasao.qintai.version.VersionUpMananger
import com.kasao.qintaiframework.base.BaseActivity
import com.kasao.qintaiframework.bottomnavigation.BottomNavigationBar
import com.kasao.qintaiframework.bottomnavigation.BottomNavigationItem
import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.until.GsonUtil
import com.kasao.qintaiframework.until.LogUtil
import com.kasao.qintaiframework.until.ToastUtil
import okhttp3.ResponseBody
import java.util.*

class MainActivity : BaseActivity(), BottomNavigationBar.OnTabSelectedListener {
    var homeFrament: HomeFrament? = null
    var socialFragment: SocialFragment? = null
    var userFragment: UserFragment? = null
    var bottomBar: BottomNavigationBar? = null

    var locationManager: LocationManager? = null
    var provider: String? = ""
    var location: Location? = null
    var locationListener: MyListener? = null

    override fun onLayoutLoad(): Int {
        return R.layout.activity_main
    }

    override fun findView() {
        bottomBar = findViewById(R.id.bottomBar)
        rendView()
        checkVersion()
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
    }

    override fun onloadData() {
        val url = "/Api/UserApi/userLoginApi"
        var params = HashMap<String, String>()
        params.put("login_name", "15120039895")
        params.put("password", "123123")
        ApiManager.getInstance.loadDataByParmars(url, params, object : HttpRespnse {
            override fun _onComplete() {
            }

            override fun _onNext(t: ResponseBody) {
                var rtn: UserDomain? = GsonUtil.GsonToBean(t.string(), UserDomain::class.java)
                LogUtil.e("login=" + rtn?.flag)
                BaseKasaoApplication.setUser(rtn?.data)
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
            1 -> if (null == socialFragment) {
                socialFragment = SocialFragment()
                fragmentTransaction.add(R.id.container, socialFragment!!)
            } else {
                fragmentTransaction.show(socialFragment!!)
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
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
}
