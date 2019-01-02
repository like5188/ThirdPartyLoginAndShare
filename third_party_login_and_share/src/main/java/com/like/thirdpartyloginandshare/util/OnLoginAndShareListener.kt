package com.like.thirdpartyloginandshare.util

interface OnLoginAndShareListener {
    fun onSuccess(content: String = "")

    fun onFailure(errorMessage: String)

    fun onCancel()
}