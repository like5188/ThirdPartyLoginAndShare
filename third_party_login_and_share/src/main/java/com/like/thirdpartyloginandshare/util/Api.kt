package com.like.thirdpartyloginandshare.util

import com.like.retrofit.ApiResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface Api {

    @GET("/api/v1.0/configcenter")
    fun getDeferred(@QueryMap paramsMap: @JvmSuppressWildcards Map<String, Any>): Deferred<ApiResponse<Any>>

}