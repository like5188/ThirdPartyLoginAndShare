package com.like.thirdpartyloginandshare

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import com.like.thirdpartyloginandshare.share.*
import com.like.thirdpartyloginandshare.util.PlatForm
import com.like.thirdpartyloginandshare.util.SingletonHolder
import kotlin.jvm.functions.FunctionN

class ThirdPartyShare private constructor(private val mActivity: Activity) {
    companion object : SingletonHolder<ThirdPartyShare>(object : FunctionN<ThirdPartyShare> {
        override val arity: Int = 1 // number of arguments that must be passed to constructor

        override fun invoke(vararg args: Any?): ThirdPartyShare {
            return ThirdPartyShare(args[0] as Activity)
        }
    }) {
        fun with(activity: Activity): ThirdPartyShare = getInstance(activity)

        fun with(fragment: Fragment): ThirdPartyShare = getInstance(fragment.activity)
    }

    private lateinit var mStrategy: ShareStrategy

    fun setPlatForm(platForm: PlatForm): ThirdPartyShare {
        when (platForm) {
            PlatForm.QQ -> {
                mStrategy = QqShare(mActivity)
            }
            PlatForm.QZONE -> {
                mStrategy = QqZoneShare(mActivity)
            }
            PlatForm.WX -> {
                mStrategy = WxShare(mActivity)
            }
            PlatForm.WX_CIRCLE -> {
                mStrategy = WxCircleShare(mActivity)
            }
            PlatForm.WB -> {
                mStrategy = WbShare(mActivity)
            }
        }
        return this
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        checkParams()
        mStrategy.onActivityResult(requestCode, resultCode, data)
    }

    private fun checkParams() {
        if (!::mStrategy.isInitialized) {
            throw UnsupportedOperationException("请先调用setPlatForm(platForm: PlatForm)方法进行初始化")
        }
    }
}