package com.like.thirdpartyloginandshare.util

import android.app.Activity
import android.content.Context
import com.sina.weibo.sdk.auth.sso.SsoHandler
import com.sina.weibo.sdk.share.WbShareHandler
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.tauth.Tencent

object ApiFactory {
    private var mQqApi: Tencent? = null
    private var mWxApi: IWXAPI? = null

    fun createQqApi(context: Context, appId: String): Tencent {
        val instance1 = mQqApi
        if (instance1 != null) {
            return instance1
        }

        return synchronized(this) {
            val instance2 = mQqApi
            if (instance2 != null) {
                instance2
            } else {
                val created = Tencent.createInstance(appId, context.applicationContext)
                mQqApi = created
                created
            }
        }
    }

    fun createWxApi(context: Context, appId: String): IWXAPI {
        val instance1 = mWxApi
        if (instance1 != null) {
            return instance1
        }

        return synchronized(this) {
            val instance2 = mWxApi
            if (instance2 != null) {
                instance2
            } else {
                val created = WXAPIFactory.createWXAPI(context.applicationContext, appId, true)
                mWxApi = created
                created
            }
        }
    }

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    fun createWbApi(activity: Activity) = SsoHandler(activity)

    fun createWbShareApi(activity: Activity) = WbShareHandler(activity)

}