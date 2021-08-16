package com.like.thirdpartyloginandshare.sample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.like.thirdpartyloginandshare.Callback
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMAuthListener
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private val mUMAuthListener = object : UMAuthListener {
        override fun onStart(platform: SHARE_MEDIA?) {
            Log.d(TAG, "auth onStart $platform")
        }

        override fun onComplete(platform: SHARE_MEDIA?, action: Int, data: MutableMap<String, String>?) {
            Log.d(TAG, "auth onComplete $platform $data")
        }

        override fun onError(platform: SHARE_MEDIA?, action: Int, t: Throwable?) {
            Log.d(TAG, "auth onError $platform $t")
        }

        override fun onCancel(platform: SHARE_MEDIA?, action: Int) {
            Log.d(TAG, "auth onCancel $platform")
        }
    }

    private val mUMShareListener = object : UMShareListener {
        override fun onStart(platform: SHARE_MEDIA?) {
            Log.d(TAG, "share onStart $platform")
        }

        override fun onResult(platform: SHARE_MEDIA?) {
            Log.d(TAG, "share onResult $platform")
        }

        override fun onError(platform: SHARE_MEDIA?, t: Throwable?) {
            Log.d(TAG, "share onError $platform $t")
        }

        override fun onCancel(platform: SHARE_MEDIA?) {
            Log.d(TAG, "share onCancel $platform")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        copyFile("eeee.mp4")
        copyFile("aaa.png")
        copyFile("bbbb.jpg")
        copyFile("ccc.JPG")
        copyFile("eee.jpg")
        copyFile("ddd.jpg")
        copyFile("fff.jpg")
        copyFile("ggg.JPG")
        copyFile("hhhh.jpg")
        copyFile("kkk.JPG")
    }

    private fun copyFile(fileName: String) {
        val file = File(getExternalFilesDir(null), fileName)
        if (!file.exists()) {
            GlobalScope.launch {
                try {
                    assets.open(fileName).use { inputStream ->
                        FileOutputStream(file).use { outputStream ->
                            outputStream.write(inputStream.readBytes())
                        }
                    }
                    Log.d("MainActivity", "copyFile成功：$fileName")
                } catch (e: Exception) {
                    Log.d("MainActivity", "copyFile失败：${e.message}")
                }
            }
        } else {
            Log.w("MainActivity", "文件已经存在：$fileName")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Callback.onActivityResultForQqOrSina(this, requestCode, resultCode, data)
    }

    fun wbLogin(view: View) {
        UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.SINA, mUMAuthListener)
    }

    fun deleteWbLogin(view: View) {
        UMShareAPI.get(this).deleteOauth(this, SHARE_MEDIA.SINA, mUMAuthListener)
    }

    fun wxLogin(view: View) {
        UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, mUMAuthListener)
    }

    fun deleteWxLogin(view: View) {
        UMShareAPI.get(this).deleteOauth(this, SHARE_MEDIA.WEIXIN, mUMAuthListener)
    }

    fun qqLogin(view: View) {
        UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, mUMAuthListener)
    }

    fun deleteQqLogin(view: View) {
        UMShareAPI.get(this).deleteOauth(this, SHARE_MEDIA.QQ, mUMAuthListener)
    }

    fun share(view: View) {
        ShareAction(this)
            .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
            .setCallback(mUMShareListener)
            .withText("hello")
            .withMedia(UMImage(this, File(getExternalFilesDir(null), "aaa.png")))
            .open()
    }

}
