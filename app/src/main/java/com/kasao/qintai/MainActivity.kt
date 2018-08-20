package com.kasao.qintai

import android.support.v4.app.FragmentTransaction
import com.kasao.qintai.api.ApiManager
import com.kasao.qintai.fragment.main.HomeFrament
import com.kasao.qintai.fragment.social.SocialFragment
import com.kasao.qintai.fragment.user.UserFragment
import com.kasao.qintai.model.domain.UserDomain
import com.kasao.qintaiframework.base.BaseActivity
import com.kasao.qintaiframework.bottomnavigation.BottomNavigationBar
import com.kasao.qintaiframework.bottomnavigation.BottomNavigationItem
import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.until.GsonUtil
import okhttp3.ResponseBody
import java.util.*

class MainActivity : BaseActivity(), BottomNavigationBar.OnTabSelectedListener {
    var homeFrament: HomeFrament? = null
    var socialFragment: SocialFragment? = null
    var userFragment: UserFragment? = null
    var bottomBar: BottomNavigationBar? = null

    override fun onLayoutLoad(): Int {
        return R.layout.activity_main
    }

    override fun findView() {
        bottomBar = findViewById(R.id.bottomBar)
        rendView()
    }

    override fun rendView() {
        initFragment(0)
        bottomBar?.setMode(BottomNavigationBar.MODE_DEFAULT)
        bottomBar?.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
        bottomBar?.addItem(BottomNavigationItem(R.mipmap.ic_launcher, "Home").setActiveColor(R.color.primary_dark_material_dark))
                ?.addItem(BottomNavigationItem(R.mipmap.ic_launcher, "socali").setActiveColor(R.color.primary_dark_material_dark))
                ?.addItem(BottomNavigationItem(R.mipmap.ic_launcher, "User").setActiveColor(R.color.white))
                ?.setFirstSelectedPosition(0)
                ?.initialise()
        bottomBar?.setTabSelectedListener(this)
    }

    override fun onloadData() {
        val url = "/Api/UserApi/userLoginApi"
        var params = HashMap<String, String>()
        params.put("login_name", "15120039895")
        params.put("password", "123123")
        ApiManager.getInstance.login(url, params, object : HttpRespnse {
            override fun _onComplete() {
            }

            override fun _onNext(t: ResponseBody) {
                var rtn = GsonUtil.GsonToBean(t.string(), UserDomain::class.java)

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
}
