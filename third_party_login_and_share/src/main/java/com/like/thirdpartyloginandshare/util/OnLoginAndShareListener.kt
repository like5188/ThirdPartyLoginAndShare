package com.like.thirdpartyloginandshare.util

interface OnLoginAndShareListener {
    fun onSuccess()
    fun onFailure(errorMessage: String)
    fun onCancel()
}