package com.like.thirdpartyloginandshare.share

import android.app.Activity
import android.content.Intent
import com.like.thirdpartyloginandshare.share.params.app.AppParams
import com.like.thirdpartyloginandshare.share.params.image.ImageParams
import com.like.thirdpartyloginandshare.share.params.imageandtext.ImageAndTextParams
import com.like.thirdpartyloginandshare.share.params.multiimage.MultiImageParams
import com.like.thirdpartyloginandshare.share.params.music.MusicParams
import com.like.thirdpartyloginandshare.share.params.page.PageParams
import com.like.thirdpartyloginandshare.share.params.text.TextParams
import com.like.thirdpartyloginandshare.share.params.video.VideoParams
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener

abstract class ShareStrategy(protected val activity: Activity) {
    protected val applicationContext = activity.applicationContext
    abstract fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    abstract fun setShareListener(listener: OnLoginAndShareListener)
    /**
     * 分享文本
     */
    abstract fun shareText(params: TextParams)

    /**
     * 分享图片
     */
    abstract fun shareImage(params: ImageParams)

    /**
     * 分享多图
     */
    abstract fun shareMultiImage(params: MultiImageParams)

    /**
     * 分享图文
     */
    abstract fun shareImageAndText(params: ImageAndTextParams)

    /**
     * 分享音乐
     */
    abstract fun shareMusic(params: MusicParams)

    /**
     * 分享视频
     */
    abstract fun shareVideo(params: VideoParams)

    /**
     * 分享应用
     */
    abstract fun shareApp(params: AppParams)

    /**
     * 分享网页
     */
    abstract fun sharePage(params: PageParams)
}