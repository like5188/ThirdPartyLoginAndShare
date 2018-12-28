package com.like.thirdpartyloginandshare.login

interface OnLoginListener {
    fun onSuccess()

    fun onFailure(errorMessage: String)
}