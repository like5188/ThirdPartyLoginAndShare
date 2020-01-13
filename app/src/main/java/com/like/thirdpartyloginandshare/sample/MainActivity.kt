package com.like.thirdpartyloginandshare.sample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.like.thirdpartyloginandshare.ThirdPartyInit
import com.like.thirdpartyloginandshare.ThirdPartyLogin
import com.like.thirdpartyloginandshare.login.QqLogin
import com.like.thirdpartyloginandshare.login.UNION_ID
import com.like.thirdpartyloginandshare.login.WbLogin
import com.like.thirdpartyloginandshare.login.WxLogin
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private val mThirdPartyLogin: ThirdPartyLogin by lazy { ThirdPartyLogin() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ThirdPartyInit.initQq(this, ThirdPartyInit.QqInitParams("101540498"))
        ThirdPartyInit.initWx(this, ThirdPartyInit.WxInitParams("wxa9cce595f2c0b87b", "secret"))
        ThirdPartyInit.initWb(this, ThirdPartyInit.WbInitParams("1929959086"))
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
        mThirdPartyLogin.onActivityResult(requestCode, resultCode, data)
    }

    fun wbLogin(view: View) {
        mThirdPartyLogin
            .strategy(WbLogin(this))
            .listener(object : OnLoginAndShareListener {
                override fun onSuccess() {
                    toast("微博登录成功")
                }

                override fun onFailure(errorMessage: String) {
                    toast("微博登录失败：$errorMessage")
                }

                override fun onCancel() {
                    toast("取消微博登录")
                }
            })
            .login()
    }

    fun getUserInfoWB(view: View) {
        mThirdPartyLogin.strategy(WbLogin(this)).getData(
            onSuccess = {
                Log.w(TAG, it)
            },
            onError = {
                Log.e(TAG, it)
            })
    }

    fun wbShare(view: View) {
        startActivity(Intent(this, WbShareActivity::class.java))
    }

    fun wxLogin(view: View) {
        mThirdPartyLogin
            .strategy(WxLogin(this))
            .listener(object : OnLoginAndShareListener {
                override fun onSuccess() {
                    toast("微信登录成功")
                }

                override fun onFailure(errorMessage: String) {
                    toast("微信登录失败：$errorMessage")
                }

                override fun onCancel() {
                    toast("取消微信登录")
                }
            })
            .login()
    }

    fun getUserInfoWX(view: View) {
        mThirdPartyLogin.strategy(WxLogin(this)).getData(
            onSuccess = {
                Log.w(TAG, it)
            },
            onError = {
                Log.e(TAG, it)
            })
    }

    fun wxShare(view: View) {
        startActivity(Intent(this, WxShareActivity::class.java))
    }

    fun wxCircleShare(view: View) {
        startActivity(Intent(this, WxCircleShareActivity::class.java))
    }

    fun qqLogin(view: View) {
        mThirdPartyLogin
            .strategy(QqLogin(this))
            .listener(object : OnLoginAndShareListener {
                override fun onSuccess() {
                    toast("QQ登录成功")
                }

                override fun onFailure(errorMessage: String) {
                    toast("QQ登录失败：$errorMessage")
                }

                override fun onCancel() {
                    toast("取消QQ登录")
                }
            })
            .login()
    }

    fun getUserInfoQQ(view: View) {
        mThirdPartyLogin.strategy(QqLogin(this)).getData(
            onSuccess = {
                Log.w(TAG, it)
            },
            onError = {
                Log.e(TAG, it)
            })
    }

    fun getUnionId(view: View) {
        mThirdPartyLogin.strategy(QqLogin(this)).getData(
            UNION_ID,
            onSuccess = {
                Log.w(TAG, it)
            },
            onError = {
                Log.e(TAG, it)
            })
    }

    fun qqShare(view: View) {
        startActivity(Intent(this, QqShareActivity::class.java))
    }

    fun qZoneShare(view: View) {
        startActivity(Intent(this, QZoneShareActivity::class.java))
    }

}
