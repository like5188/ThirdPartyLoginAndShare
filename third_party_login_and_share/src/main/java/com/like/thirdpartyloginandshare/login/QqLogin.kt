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
class QqLogin(private val activity: Activity) : ILoginStrategy {
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

    override fun setLoginListener(listener: OnLoginAndShareListener) {
        mOnLoginAndShareListener = listener
        mLoginListener = LoginListener(listener)
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

    override fun getData(dataType: DataType, onSuccess: (String) -> Unit, onError: ((String) -> Unit)?) {
        if (!mTencent.isSessionValid) {
            onError?.invoke("尚未登录QQ")
            return
        }
        when (dataType) {
            USER_INFO -> {
                UserInfo(activity.applicationContext, mTencent.qqToken).getUserInfo(object : IUiListener {
                    override fun onComplete(response: Any?) {
                        onSuccess(response?.toString() ?: "")
                    }

                    override fun onCancel() {
                        onError?.invoke("操作被取消了")
                    }

                    override fun onError(e: UiError?) {
                        onError?.invoke(e?.errorDetail ?: "")
                    }
                })
            }
            UNION_ID -> {
                UnionInfo(activity.applicationContext, mTencent.qqToken).getUnionId(object : IUiListener {
                    override fun onComplete(response: Any?) {
                        onSuccess(response?.toString() ?: "")
                    }

                    override fun onCancel() {
                        onError?.invoke("操作被取消了")
                    }

                    override fun onError(e: UiError?) {
                        onError?.invoke(e?.errorDetail ?: "")
                    }
                })
            }
        }
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

}