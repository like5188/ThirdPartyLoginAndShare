package com.like.thirdpartyloginandshare

import android.app.Activity
import android.content.Intent
import com.like.thirdpartyloginandshare.Callback.onActivityResultForQqOrSina
import com.umeng.socialize.UMShareAPI

/**
 * 回调处理：
 * QQ与新浪：[onActivityResultForQqOrSina]
 * 微信：[com.like.thirdpartyloginandshare.apshare.ShareEntryActivity]
 * 支付宝：[com.like.thirdpartyloginandshare.apshare.ShareEntryActivity]
 * 钉钉：[com.like.thirdpartyloginandshare.ddshare.DDShareActivity]
 */
object Callback {
    fun onActivityResultForQqOrSina(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data)
    }
}