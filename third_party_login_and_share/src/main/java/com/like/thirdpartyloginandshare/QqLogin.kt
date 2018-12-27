package com.like.thirdpartyloginandshare

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tencent.connect.common.Constants
import com.tencent.tauth.IRequestListener
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError


/**
 * QQ登录工具类
 * 应用需要在调用接口的Activity的onActivityResult方法中调用[onActivityResult]
 *
 * 登录常见错误码信息：
 * 110201：未登陆
 * 110405：登录请求被限制
 * 110404：请求参数缺少appid
 * 110401：请求的应用不存在
 * 110407：应用已经下架
 * 110406：应用没有通过审核
 * 100044：错误的sign
 * 110500：获取用户授权信息失败
 * 110501：获取应用的授权信息失败
 * 110502：设置用户授权失败
 * 110503：获取token失败
 * 110504：系统内部错误
 */
class QqLogin() {

    private val mUiListener = object : IUiListener {
        /*
        {
            "ret":0,
            "pay_token":"xxxxxxxxxxxxxxxx",
            "pf":"openmobile_android",
            "expires_in":"7776000",
            "openid":"xxxxxxxxxxxxxxxxxxx",
            "pfkey":"xxxxxxxxxxxxxxxxxxx",
            "msg":"sucess",
            "access_token":"xxxxxxxxxxxxxxxxxxxxx"
        }
         */
        override fun onComplete(p0: Any?) {
        }

        override fun onCancel() {
        }

        override fun onError(p0: UiError?) {
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mUiListener)
        }
    }

    fun login(activity: Activity) {
        checkSessionValidAndLogin(activity) {
            it.login(activity, "all", mUiListener)
        }
    }

    fun login(fragment: Fragment) {
        checkSessionValidAndLogin(fragment.context) {
            it.login(fragment, "all", mUiListener)
        }
    }

    private inline fun checkSessionValidAndLogin(context: Context?, login: (Tencent) -> Unit) {
        Tencent.createInstance(QQ_APP_ID, context?.applicationContext)?.let {
            if (it.checkSessionValid(QQ_APP_ID)) {
                it.initSessionCache(it.loadSession(QQ_APP_ID))
            } else {// token过期，请调用登录接口拉起手Q授权登录
                login(it)
            }
        }
    }

    fun logout(context: Context) {
        Tencent.createInstance(QQ_APP_ID, context.applicationContext)?.logout(context)
    }

    /*
    {
        "is_yellow_year_vip": "0",
        "ret": 0,
        "figureurl_qq_1": "http://q.qlogo.cn/qqapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/40",
        "figureurl_qq_2": "http://q.qlogo.cn/qqapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/100",
        "nickname": "小罗",
        "yellow_vip_level": "0",
        "msg": "",
        "figureurl_1": "http://qzapp.qlogo.cn/qzapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/50",
        "vip": "0",
        "level": "0",
        "figureurl_2": "http://qzapp.qlogo.cn/qzapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/100",
        "is_yellow_vip": "0",
        "gender": "男",
        "figureurl": "http://qzapp.qlogo.cn/qzapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/30"
    }
     */
    fun getUserInfo(context: Context, listener: IRequestListener) {
//        Tencent.createInstance(QQ_APP_ID, context.applicationContext)
//            ?.requestAsync(Constants.GRAPH_SIMPLE_USER_INFO, null, Constants.HTTP_GET, listener, null)
    }

}