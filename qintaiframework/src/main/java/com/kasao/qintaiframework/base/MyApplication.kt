package com.kasao.qintaiframework.base

import android.app.Application
import com.kasao.qintaiframework.until.ScreenUtil

open class MyApplication : Application() {
    companion object {
        val isDebug: Boolean = false
     public var applicaton = Helper.instance

    }

    private object Helper {
        var instance = MyApplication()
    }

    override fun onCreate() {
        super.onCreate()
      applicaton=this
    }
}