package com.like.thirdpartyloginandshare.login

import android.content.Intent
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener

interface ILoginStrategy {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    fun setLoginListener(listener: OnLoginAndShareListener)
    fun login()
    fun logout()
    fun getData(
        dataType: DataType = USER_INFO,
        params: Map<String, Any>? = null,
        onSuccess: (String) -> Unit,
        onError: ((String) -> Unit)? = null
    )
}

sealed class DataType
object USER_INFO : DataType()
object UNION_ID : DataType()