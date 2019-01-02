package com.like.thirdpartyloginandshare.login

import com.like.retrofit.ApiResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RetrofitApi {
    @GET("/sns/oauth2/access_token")
    fun getAccessTokenByCode(@QueryMap paramsMap: @JvmSuppressWildcards Map<String, Any>): Deferred<ApiResponse<String>>

}