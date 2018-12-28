package com.like.thirdpartyloginandshare

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import com.like.thirdpartyloginandshare.login.*
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.like.thirdpartyloginandshare.util.PlatForm
import com.like.thirdpartyloginandshare.util.SingletonHolder
import kotlin.jvm.functions.FunctionN

class ThirdPartyLogin private constructor(private val mActivity: Activity) {
    companion object : SingletonHolder<ThirdPartyLogin>(object : FunctionN<ThirdPartyLogin> {
        override val arity: Int = 1 // number of arguments that must be passed to constructor

        override fun invoke(vararg args: Any?): ThirdPartyLogin {
            return ThirdPartyLogin(args[0] as Activity)
        }
    }) {
        fun with(activity: Activity): ThirdPartyLogin = getInstance(activity)

        fun with(fragment: Fragment): ThirdPartyLogin = getInstance(fragment.activity)
    }

    private lateinit var mStrategy: LoginStrategy

    fun setPlatForm(platForm: PlatForm): ThirdPartyLogin {
        when (platForm) {
            PlatForm.QQ -> {
                mStrategy = QqLogin(mActivity)
            }
            PlatForm.QZONE -> {
                throw UnsupportedOperationException("暂不支持 QZONE 进行登录")
            }
            PlatForm.WEIXIN -> {
                mStrategy = WxLogin.getInstance(mActivity)
            }
            PlatForm.WEIXIN_CIRCLE -> {
                throw UnsupportedOperationException("暂不支持 WEIXIN_CIRCLE 进行登录")
            }
            PlatForm.SINA -> {
                mStrategy = WbLogin(mActivity)
            }
        }
        return this
    }

    fun login(listener: OnLoginAndShareListener) {
        checkParams()
        mStrategy.login(listener)
    }

    fun logout() {
        checkParams()
        mStrategy.logout()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        checkParams()
        mStrategy.onActivityResult(requestCode, resultCode, data)
    }

    private fun checkParams() {
        if (!::mStrategy.isInitialized) {
            throw UnsupportedOperationException("请先调用setPlatForm(platForm: PlatForm)方法进行初始化")
        }
    }
}