package com.like.thirdpartyloginandshare

import android.content.Intent
import com.like.thirdpartyloginandshare.share.*
import com.like.thirdpartyloginandshare.share.params.ShareParams
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener

class ThirdPartyShare {
    private lateinit var mStrategy: IShareStrategy

    fun strategy(strategy: IShareStrategy): ThirdPartyShare {
        mStrategy = strategy
        return this
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        checkParams()
        mStrategy.onActivityResult(requestCode, resultCode, data)
    }

    fun listener(listener: OnLoginAndShareListener): ThirdPartyShare {
        checkParams()
        mStrategy.setShareListener(listener)
        return this
    }

    /**
     * 分享
     * @param params    对应平台的对应参数。参数在[com.like.thirdpartyloginandshare.share.params]包下。
     */
    fun share(params: ShareParams) {
        checkParams()
        mStrategy.share(params)
    }

    private fun checkParams() {
        if (!::mStrategy.isInitialized) {
            throw UnsupportedOperationException("请先调用setPlatForm(platForm: PlatForm)方法设置对应的第三方分享平台")
        }
        when (mStrategy) {
            is QqShare, is QZoneShare -> {
                if (!ThirdPartyInit.isQqInitialized()) {
                    throw IllegalArgumentException("必须先调用ThirdPartyInit.initQq()方法初始化QQ")
                }
            }
            is WxShare -> {
                if (!ThirdPartyInit.isWxInitialized()) {
                    throw IllegalArgumentException("必须先调用ThirdPartyInit.initWx()方法初始化WX")
                }
            }
            is WbShare -> {
                if (!ThirdPartyInit.isWbInitialized()) {
                    throw IllegalArgumentException("必须先调用ThirdPartyInit.initWb()方法初始化WB")
                }
            }
        }
    }
}