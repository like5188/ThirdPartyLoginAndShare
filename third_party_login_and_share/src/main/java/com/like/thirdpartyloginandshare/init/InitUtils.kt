package com.like.thirdpartyloginandshare.init

import android.content.Context
import com.like.thirdpartyloginandshare.init.params.InitParams
import com.like.thirdpartyloginandshare.init.params.QqInitParams
import com.like.thirdpartyloginandshare.init.params.WbInitParams
import com.like.thirdpartyloginandshare.init.params.WxInitParams
import com.like.thirdpartyloginandshare.util.ApiFactory
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.auth.AuthInfo
import java.util.concurrent.atomic.AtomicBoolean

object InitUtils {
    private val isQqInitialized = AtomicBoolean(false)
    private val isWxInitialized = AtomicBoolean(false)
    private val isWbInitialized = AtomicBoolean(false)
    lateinit var qqInitParams: QqInitParams
    lateinit var wxInitParams: WxInitParams
    lateinit var wbInitParams: WbInitParams

    fun initQq(context: Context, initParams: InitParams) {
        if (initParams !is QqInitParams) throw IllegalArgumentException("初始化QQ失败，参数必须为QqInitParams")
        qqInitParams = initParams
        if (isQqInitialized.compareAndSet(false, true)) {

        }
    }

    fun initWx(context: Context, initParams: InitParams) {
        if (initParams !is WxInitParams) throw IllegalArgumentException("初始化微信失败，参数必须为WxInitParams")
        wxInitParams = initParams
        if (isWxInitialized.compareAndSet(false, true)) {
            ApiFactory.createWxApi(context, initParams.appId).registerApp(initParams.appId)
        }
    }

    fun initWb(context: Context, initParams: InitParams) {
        if (initParams !is WbInitParams) throw IllegalArgumentException("初始化微博失败，参数必须为WbInitParams")
        wbInitParams = initParams
        if (isWbInitialized.compareAndSet(false, true)) {
            WbSdk.install(
                context.applicationContext,
                AuthInfo(context.applicationContext, initParams.appKey, initParams.redirectUrl, initParams.scope)
            )
        }
    }

}