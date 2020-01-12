package com.like.thirdpartyloginandshare.util

import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import javax.net.ssl.HttpsURLConnection

object HttpUtils {
    fun requestAsync(httpsUrl: String, onSuccess: (String?) -> Unit, onError: ((String) -> Unit)? = null) {
        Thread {
            try {
                val url = URL(httpsUrl)
                val urlConnection: URLConnection = url.openConnection()
                val httpsConn: HttpsURLConnection = urlConnection as HttpsURLConnection
                httpsConn.allowUserInteraction = false
                httpsConn.instanceFollowRedirects = true
                httpsConn.requestMethod = "GET"
                httpsConn.connect()
                if (httpsConn.responseCode == HttpURLConnection.HTTP_OK) {
                    onSuccess(httpsConn.inputStream.bufferedReader().readText())
                } else {
                    onError?.invoke(httpsConn.responseMessage)
                }
            } catch (e: Exception) {
                onError?.invoke(e.message ?: "")
            }
        }.start()
    }
}