package com.like.thirdpartyloginandshare.login

import android.app.Activity
import android.content.Intent
import com.like.thirdpartyloginandshare.util.ApiFactory
import com.like.thirdpartyloginandshare.util.HttpUtils
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.sina.weibo.sdk.auth.AccessTokenKeeper
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.auth.WbConnectErrorMessage
import org.json.JSONObject

class WbLogin(private val activity: Activity) : LoginStrategy {
    private val mSsoHandler by lazy { ApiFactory.createWbApi(activity) }
    private var mLoginListener: LoginListener? = null
    private var mOnLoginAndShareListener: OnLoginAndShareListener? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mSsoHandler.authorizeCallBack(requestCode, resultCode, data)
    }

    override fun setLoginListener(listener: OnLoginAndShareListener): LoginStrategy {
        mOnLoginAndShareListener = listener
        mLoginListener = LoginListener(listener)
        return this
    }

    override fun login() {
        if (AccessTokenKeeper.readAccessToken(activity.applicationContext).isSessionValid) {
            mOnLoginAndShareListener?.onSuccess()
        } else {
            // 此种授权方式会根据手机是否安装微博客户端来决定使用sso授权还是网页授权，
            // 如果安装有微博客户端 则调用微博客户端授权，否则调用Web页面方式授权
            mSsoHandler.authorize(mLoginListener)
        }
    }

    override fun logout() {
        AccessTokenKeeper.clear(activity.applicationContext)
    }

    /**
     * @param uid           需要查询的用户ID
     * @param screen_name   需要查询的用户昵称
     *
     * 注意：
     * 1、参数uid与screen_name二者必选其一，且只能选其一
     * 2、接口升级后，对未授权本应用的uid，将无法获取其个人简介、认证原因、粉丝数、关注数、微博数及最近一条微博内容
     */
    fun getUserInfo(uid: String = "", screen_name: String = "", onSuccess: (UserInfo) -> Unit, onError: ((String) -> Unit)? = null) {
        // 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
        val oauth2AccessToken = AccessTokenKeeper.readAccessToken(activity.applicationContext)
        if (oauth2AccessToken.isSessionValid) {
            HttpUtils.requestAsync(
                "https://api.weibo.com/2/users/show.json?access_token=${oauth2AccessToken.token}&uid=$uid&screen_name=$screen_name",
                {
                    if (it.isNullOrEmpty()) {
                        onError?.invoke("用户信息为空")
                    } else {
                        try {
                            val jsonObject = JSONObject(it)
                            val userInfo = UserInfo()
                            userInfo.screen_name = jsonObject.getString("screen_name")
                            userInfo.gender = jsonObject.getString("gender")
                            userInfo.name = jsonObject.getString("name")
                            userInfo.profile_image_url = jsonObject.getString("profile_image_url")
                            userInfo.avatar_large = jsonObject.getString("avatar_large")
                            userInfo.avatar_hd = jsonObject.getString("avatar_hd")
                            onSuccess(userInfo)
                        } catch (e: Exception) {
                            onError?.invoke(e.message ?: "")
                        }
                    }
                },
                onError
            )
        } else {
            onError?.invoke("获取用户信息失败 尚未登录")
        }
    }

    inner class LoginListener(private val listener: OnLoginAndShareListener) : WbAuthListener {
        override fun onSuccess(token: Oauth2AccessToken?) {
            if (token != null) {
                if (token.isSessionValid) {
                    listener.onSuccess()
                    // 保存 Token 到 SharedPreferences
                    AccessTokenKeeper.writeAccessToken(activity.applicationContext, token)
                } else {
                    listener.onFailure("登录失败 token无效")
                }
            } else {
                listener.onFailure("登录失败 返回为空")
            }
        }

        override fun onFailure(errorMessage: WbConnectErrorMessage) {
            listener.onFailure("登录失败 ${errorMessage.errorMessage}")
        }

        override fun cancel() {
            listener.onCancel()
        }

        fun onSuccess() {
            listener.onSuccess()
        }
    }

    class UserInfo {
        /**
         * 用户昵称
         */
        var screen_name = ""
        /**
         * 性别，m：男、f：女、n：未知
         */
        var gender = ""
        var name = ""
        /**
         * 用户头像地址（中图），50×50像素
         */
        var profile_image_url = ""
        /**
         * 用户头像地址（大图），180×180像素
         */
        var avatar_large = ""
        /**
         * 用户头像地址（高清），高清头像原图
         */
        var avatar_hd = ""
    }
}