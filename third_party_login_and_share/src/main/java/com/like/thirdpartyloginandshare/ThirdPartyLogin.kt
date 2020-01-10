package com.like.thirdpartyloginandshare

import android.app.Activity
import android.content.Intent
import com.like.thirdpartyloginandshare.login.LoginStrategy
import com.like.thirdpartyloginandshare.login.QqLogin
import com.like.thirdpartyloginandshare.login.WbLogin
import com.like.thirdpartyloginandshare.login.WxLogin
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.like.thirdpartyloginandshare.util.PlatForm

class ThirdPartyLogin(private val activity: Activity) : LoginStrategy {
    private lateinit var mStrategy: LoginStrategy

    fun setPlatForm(platForm: PlatForm): ThirdPartyLogin {
        ThirdPartyInit.checkInit(platForm)
        when (platForm) {
            PlatForm.QQ -> {
                mStrategy = QqLogin(activity)
            }
            PlatForm.QZONE -> {
                throw UnsupportedOperationException("暂不支持 QZONE 进行登录")
            }
            PlatForm.WX -> {
                mStrategy = WxLogin(activity)
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

    override fun getUserInfo(onSuccess: (String) -> Unit, onError: ((String) -> Unit)?) {
        checkParams()
        mStrategy.getUserInfo(onSuccess, onError)
    }

    override fun getUnionId(onSuccess: (String) -> Unit, onError: ((String) -> Unit)?) {
        checkParams()
        mStrategy.getUnionId(onSuccess, onError)
    }

    private fun checkParams() {
        if (!::mStrategy.isInitialized) {
            throw UnsupportedOperationException("请先调用setPlatForm(platForm: PlatForm)方法设置对应的第三方登录平台")
        }
    }
}