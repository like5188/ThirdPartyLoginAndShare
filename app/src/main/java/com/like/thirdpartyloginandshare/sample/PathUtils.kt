package com.like.thirdpartyloginandshare.sample

import android.content.Context
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import java.io.File

object PathUtils {
    fun getUriFromFile(context: Context, file: File) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        // android7.0需要通过FileProvider来获取文件uri。android8.0更需要添加权限Manifest.permission.REQUEST_INSTALL_PACKAGES
        FileProvider.getUriForFile(context.applicationContext, "${context.packageName}.fileprovider", file)
    } else {
        Uri.fromFile(file)
    }
}