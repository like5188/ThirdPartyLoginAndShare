package com.like.thirdpartyloginandshare

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import com.like.thirdpartyloginandshare.login.LoginStrategy
import com.like.thirdpartyloginandshare.login.QqLogin
import com.like.thirdpartyloginandshare.login.WbLogin
import com.like.thirdpartyloginandshare.login.WxLogin
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.like.thirdpartyloginandshare.util.PlatForm
import com.like.thirdpartyloginandshare.util.SingletonHolder
import kotlin.jvm.functions.FunctionN

class ThirdPartyLogin private constructor(activity: Activity) : LoginStrategy(activity) {
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
                mStrategy = QqLogin(activity)
            }
            PlatForm.QZONE -> {
                throw UnsupportedOperationException("暂不支持 QZONE 进行登录")
            }
            PlatForm.WX -> {
                mStrategy = WxLogin.getInstance(activity)
            }
            PlatForm.WX_CIRCLE -> {
                throw UnsupportedOperationException("暂不支持 WX_CIRCLE 进行登录")
            }
            PlatForm.WB -> {
                mStrategy = WbLogin(activity)
            }
        }
        return this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        checkParams()
        mStrategy.onActivityResult(requestCode, resultCode, data)
    }

    override fun setLoginListener(listener: OnLoginAndShareListener): ThirdPartyLogin {
        checkParams()
        mStrategy.setLoginListener(listener)
        return this
    }

    override fun login() {
        checkParams()
        mStrategy.login()
    }

    override fun logout() {
        checkParams()
        mStrategy.logout()
    }

    private fun checkParams() {
        if (!::mStrategy.isInitialized) {
            throw UnsupportedOperationException("请先调用setPlatForm(platForm: PlatForm)方法设置对应的第三方登录平台")
        }
    }
}