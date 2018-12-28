package com.like.thirdpartyloginandshare.login

import android.app.Activity
import android.content.Intent

abstract class LoginStrategy(protected val activity: Activity) {
    protected val applicationContext = activity.applicationContext
    abstract fun login(listener: OnLoginListener)
    abstract fun logout()
    abstract fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}