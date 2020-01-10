package com.like.thirdpartyloginandshare.login

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import com.like.thirdpartyloginandshare.ThirdPartyInit
import com.like.thirdpartyloginandshare.util.ApiFactory
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.tencent.connect.UnionInfo
import com.tencent.connect.UserInfo
import com.tencent.connect.common.Constants
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import org.json.JSONObject

/**
 * QQ登录工具类
 * 应用需要在调用接口的Activity的onActivityResult方法中调用[onActivityResult]
 */
class QqLogin(private val activity: Activity) : LoginStrategy {
    private val mTencent by lazy {
        ApiFactory.createQqApi(activity.applicationContext, ThirdPartyInit.qqInitParams.appId)
    }
    private var mLoginListener: LoginListener? = null
    private var mOnLoginAndShareListener: OnLoginAndShareListener? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mLoginListener as? IUiListener)
        }
    }

    override fun setLoginListener(listener: OnLoginAndShareListener): LoginStrategy {
        mOnLoginAndShareListener = listener
        mLoginListener = LoginListener(listener)
        return this
    }

    override fun login() {
        if (mTencent.checkSessionValid(ThirdPartyInit.qqInitParams.appId)) {
            mTencent.initSessionCache(mTencent.loadSession(ThirdPartyInit.qqInitParams.appId))
            mOnLoginAndShareListener?.onSuccess()
        } else {
            // token过期，请调用登录接口拉起手Q授权登录
            // 如果没有安装QQ，会自动打开下载页面
            mTencent.login(activity, "all", mLoginListener)
        }
    }

    override fun logout() {
        mTencent.logout(activity.applicationContext)
    }

    inner class LoginListener(private val listener: OnLoginAndShareListener) : IUiListener {
        override fun onComplete(response: Any?) {
            val jsonObject = response as? JSONObject
            if (null != jsonObject && jsonObject.length() != 0) {
                try {
                    val accessToken = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN)
                    val expiresIn = jsonObject.getString(Constants.PARAM_EXPIRES_IN)
                    val openId = jsonObject.getString(Constants.PARAM_OPEN_ID)
                    if (!TextUtils.isEmpty(accessToken) && !TextUtils.isEmpty(expiresIn) && !TextUtils.isEmpty(openId)) {
                        mTencent.setAccessToken(accessToken, expiresIn)
                        mTencent.openId = openId
                        listener.onSuccess()
                    } else {
                        listener.onFailure("登录失败 解析数据失败")
                    }
                } catch (e: Exception) {
                    listener.onFailure(e.message ?: "")
                }
            } else {
                listener.onFailure("登录失败 返回为空")
            }
        }

        override fun onError(e: UiError) {
            listener.onFailure("登录失败 ${e.errorDetail}")
        }

        override fun onCancel() {
            listener.onCancel()
        }

        fun onSuccess() {
            listener.onSuccess()
        }

    }

    fun getUserInfo(listener: GetUserInfoListener) {
        if (mTencent.isSessionValid) {
            UserInfo(activity.applicationContext, mTencent.qqToken).getUserInfo(listener)
        } else {
            listener.onFailure("获取用户信息失败 尚未登录")
        }
    }

    fun getUnionId(listener: GetUnionIdListener) {
        if (mTencent.isSessionValid) {
            UnionInfo(activity.applicationContext, mTencent.qqToken).getUnionId(listener)
        } else {
            listener.onFailure("获取unionId失败 尚未登录")
        }
    }

    abstract class GetUserInfoListener : IUiListener {
        override fun onComplete(response: Any?) {
            val jsonObject = response as? JSONObject
            if (null != jsonObject && jsonObject.length() != 0) {
                val ret = jsonObject.optInt("ret", -1)
                if (ret == 0) {
                    val userInfo = UserInfo()
                    try {
                        userInfo.nickname = jsonObject.getString("nickname")
                        userInfo.gender = jsonObject.getString("gender")
                        userInfo.figureurl_qq_1 = jsonObject.getString("figureurl_qq_1")
                        userInfo.figureurl_qq_2 = jsonObject.getString("figureurl_qq_2")
                        onSuccess(userInfo)
                    } catch (e: Exception) {
                        onFailure("获取用户信息失败 ${e.message}")
                    }
                } else {
                    val msg = jsonObject.optString("msg")
                    onFailure("获取用户信息失败 $msg")
                }
            } else {
                onFailure("获取用户信息失败 返回为空")
            }
        }

        override fun onCancel() {
            onFailure("取消获取用户信息")
        }

        override fun onError(e: UiError?) {
            onFailure("获取用户信息失败 ${e?.errorDetail}")
        }

        abstract fun onSuccess(userInfo: UserInfo)

        abstract fun onFailure(errorMessage: String)
    }

    abstract class GetUnionIdListener : IUiListener {
        override fun onComplete(response: Any?) {
            val jsonObject = response as? JSONObject
            if (null != jsonObject && jsonObject.length() != 0) {
                try {
                    onSuccess(jsonObject.getString("unionid"))
                } catch (e: Exception) {
                    onFailure("获取unionId成功 但是解析数据失败")
                }
            } else {
                onFailure("获取unionId失败 返回为空")
            }
        }

        override fun onCancel() {
            onFailure("取消获取unionId")
        }

        override fun onError(e: UiError?) {
            onFailure("获取unionId失败 ${e?.errorDetail}")
        }

        abstract fun onSuccess(unionId: String)

        abstract fun onFailure(errorMessage: String)
    }

    class UserInfo {
        var nickname = ""
        var gender = ""
        /**
         * 大小为40×40像素的QQ头像URL。
         */
        var figureurl_qq_1 = ""
        /**
         * 大小为100×100像素的QQ头像URL。需要注意，不是所有的用户都拥有QQ的100x100的头像，但40x40像素则是一定会有。
         */
        var figureurl_qq_2 = ""
    }

}