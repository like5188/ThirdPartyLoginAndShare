package com.like.thirdpartyloginandshare.init

import android.content.Context
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.auth.AuthInfo
import com.tencent.mm.opensdk.openapi.IWXAPI
import java.util.concurrent.atomic.AtomicBoolean

object InitUtils {
    var isQqInitialized = AtomicBoolean(false)
    var isWxInitialized = AtomicBoolean(false)
    var isWbInitialized = AtomicBoolean(false)

    fun initQq() {
        if (isQqInitialized.compareAndSet(false, true)) {

        }
    }

    fun initWx(api: IWXAPI, appId: String) {
        if (isWxInitialized.compareAndSet(false, true)) {
            api.registerApp(appId)
        }
    }

    fun initWb(context: Context, appKey: String, redirectUrl: String, scope: String) {
        if (isWbInitialized.compareAndSet(false, true)) {
            WbSdk.install(context.applicationContext, AuthInfo(context.applicationContext, appKey, redirectUrl, scope))
        }
    }

}