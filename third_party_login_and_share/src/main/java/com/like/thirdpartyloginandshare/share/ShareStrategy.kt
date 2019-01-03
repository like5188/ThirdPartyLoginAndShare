package com.like.thirdpartyloginandshare.share

import android.content.Intent
import com.like.thirdpartyloginandshare.share.params.ShareParams
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener

interface ShareStrategy {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    fun setShareListener(listener: OnLoginAndShareListener): ShareStrategy
    fun share(params: ShareParams)
}