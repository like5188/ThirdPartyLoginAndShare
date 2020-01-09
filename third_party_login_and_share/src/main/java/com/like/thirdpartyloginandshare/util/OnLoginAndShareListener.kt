package com.like.thirdpartyloginandshare.util

interface OnLoginAndShareListener {
    fun onSuccess(data: Any? = null)

    fun onFailure(errorMessage: String)

    fun onCancel()
}