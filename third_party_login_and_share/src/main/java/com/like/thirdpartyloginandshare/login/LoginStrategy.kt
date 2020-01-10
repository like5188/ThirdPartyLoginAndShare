package com.like.thirdpartyloginandshare.login

import android.content.Intent
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener

interface LoginStrategy {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    fun setLoginListener(listener: OnLoginAndShareListener): LoginStrategy
    fun login()
    fun logout()
    fun getUserInfo(onSuccess: (String) -> Unit, onError: ((String) -> Unit)? = null)
    fun getUnionId(onSuccess: (String) -> Unit, onError: ((String) -> Unit)? = null)
}