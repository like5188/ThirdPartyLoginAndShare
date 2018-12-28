package com.like.thirdpartyloginandshare.login

import android.app.Activity
import android.content.Intent
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener

abstract class LoginStrategy(protected val activity: Activity) {
    protected val applicationContext = activity.applicationContext
    abstract fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    abstract fun setLoginListener(listener: OnLoginAndShareListener)
    abstract fun login(listener: OnLoginAndShareListener)
    abstract fun logout()
}