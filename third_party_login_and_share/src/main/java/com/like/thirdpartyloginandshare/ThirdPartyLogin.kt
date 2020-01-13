package com.like.thirdpartyloginandshare

import android.content.Intent
import com.like.thirdpartyloginandshare.login.*
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener

class ThirdPartyLogin {
    private lateinit var mStrategy: ILoginStrategy

    fun strategy(strategy: ILoginStrategy): ThirdPartyLogin {
        mStrategy = strategy
        return this
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        checkParams()
        mStrategy.onActivityResult(requestCode, resultCode, data)
    }

    fun listener(listener: OnLoginAndShareListener): ThirdPartyLogin {
        checkParams()
        mStrategy.setLoginListener(listener)
        return this
    }

    fun login() {
        checkParams()
        mStrategy.login()
    }

    fun logout() {
        checkParams()
        mStrategy.logout()
    }

    fun getData(dataType: DataType = USER_INFO, onSuccess: (String) -> Unit, onError: ((String) -> Unit)? = null) {
        checkParams()
        mStrategy.getData(dataType, onSuccess, onError)
    }

    private fun checkParams() {
        if (!::mStrategy.isInitialized) {
            throw UnsupportedOperationException("请先调用setPlatForm(platForm: PlatForm)方法设置对应的第三方登录平台")
        }
        when (mStrategy) {
            is QqLogin -> {
                if (!ThirdPartyInit.isQqInitialized()) {
                    throw IllegalArgumentException("必须先调用ThirdPartyInit.initQq()方法初始化QQ")
                }
            }
            is WxLogin -> {
                if (!ThirdPartyInit.isWxInitialized()) {
                    throw IllegalArgumentException("必须先调用ThirdPartyInit.initWx()方法初始化WX")
                }
            }
            is WbLogin -> {
                if (!ThirdPartyInit.isWbInitialized()) {
                    throw IllegalArgumentException("必须先调用ThirdPartyInit.initWb()方法初始化WB")
                }
            }
        }
    }
}