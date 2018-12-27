package com.like.thirdpartyloginandshare

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.text.TextUtils
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
class QqLogin(context: Context, private val mLoginListener: LoginListener) {
    private val mTencent = Tencent.createInstance(QQ_APP_ID, context.applicationContext)

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mLoginListener)
        }
    }

    fun login(activity: Activity) {
        checkSessionValidAndLogin(activity) {
            mTencent.login(activity, "all", mLoginListener)
        }
    }

    fun login(fragment: Fragment) {
        checkSessionValidAndLogin(fragment.context) {
            mTencent.login(fragment, "all", mLoginListener)
        }
    }

    fun isLogin(context: Context) = mTencent.isSessionValid

    fun logout(context: Context) {
        mTencent.logout(context)
    }

    fun getUserInfo(context: Context, listener: GetUserInfoListener) {
        if (isLogin(context)) {
            UserInfo(context, mTencent.qqToken).getUserInfo(listener)
        } else {
            listener.onFailure("获取用户信息失败 尚未登录")
        }
    }

    fun getUnionId(context: Context, listener: GetUnionIdListener) {
        if (isLogin(context)) {
            UnionInfo(context, mTencent.qqToken).getUnionId(listener)
        } else {
            listener.onFailure("获取unionId失败 尚未登录")
        }
    }

    private inline fun checkSessionValidAndLogin(context: Context?, login: () -> Unit) {
        if (mTencent.checkSessionValid(QQ_APP_ID)) {
            mTencent.initSessionCache(mTencent.loadSession(QQ_APP_ID))
            mLoginListener.onSuccess()
        } else {// token过期，请调用登录接口拉起手Q授权登录
            login()
        }
    }

    abstract class GetUserInfoListener : IUiListener {
        override fun onComplete(response: Any?) {
            val jsonObject = response as JSONObject?
            if (null != jsonObject && jsonObject.length() != 0) {
                onSuccess(jsonObject)
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

        abstract fun onSuccess(jsonObject: JSONObject)

        abstract fun onFailure(errorMessage: String)
    }

    abstract class GetUnionIdListener : IUiListener {
        override fun onComplete(response: Any?) {
            val jsonObject = response as JSONObject?
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

    abstract inner class LoginListener : IUiListener {

        override fun onComplete(response: Any?) {
            val jsonObject = response as JSONObject?
            if (null != jsonObject && jsonObject.length() != 0) {
                try {
                    val token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN)
                    val expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN)
                    val openId = jsonObject.getString(Constants.PARAM_OPEN_ID)
                    if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
                        mTencent.setAccessToken(token, expires)
                        mTencent.openId = openId
                    }
                } catch (e: Exception) {
                }
                onSuccess()
            } else {
                onFailure("登录失败 返回为空")
            }
        }

        override fun onError(e: UiError) {
            onFailure("登录失败 ${e.errorDetail}")
        }

        override fun onCancel() {
            onFailure("取消登录")
        }

        abstract fun onSuccess()

        abstract fun onFailure(errorMessage: String)
    }

}