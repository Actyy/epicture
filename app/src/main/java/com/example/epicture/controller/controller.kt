package com.example.epicture.controller

import android.content.Context
import android.content.Intent
import android.webkit.WebView
import android.widget.Button
import com.example.epicture.model.model
import com.example.epicture.vue.imgurLogin
import android.content.SharedPreferences
import android.os.Build
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import com.example.epicture.vue.baseActivity


class controller(private val context: Context) {
    var m = model()

    public fun login(Loginbutton: Button) {
        Loginbutton.setOnClickListener() {
            val intent = Intent(this.context, imgurLogin::class.java)
            context.startActivity(intent)
        }
    }

    fun splitUrl(url: String, sharedPreferences: SharedPreferences) {
        m.setAccountUsername(url.split("account_username=", "&account_id")[1])
        m.setAccountId(url.split("&account_id=").last())
        m.setAccessToken(sharedPreferences, url.split("access_token=", "&expires")[1])
    }

    fun redirectAfterLogin(sharedPreferences : SharedPreferences) {
        val i = Intent(this.context, baseActivity::class.java)
        context.startActivity(i)
    }

    public fun loadLogin(webView: WebView) {
        var url = "https://api.imgur.com/oauth2/authorize?client_id=" + m.getClientId() + "&response_type=token"
        println("URL = " + url)
        webView.clearCache(true)
        webView.clearHistory()
        this.clearCookies(this.context)
        webView.loadUrl(url)
    }

    fun clearCookies(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()
        } else if (context != null) {
            val cookieSyncManager = CookieSyncManager.createInstance(context)
            cookieSyncManager.startSync()
            val cookieManager: CookieManager = CookieManager.getInstance()
            cookieManager.removeAllCookie()
            cookieManager.removeSessionCookie()
            cookieSyncManager.stopSync()
            cookieSyncManager.sync()
        }
    }

    fun checkIfConnected(sharedPreferences: SharedPreferences) : Boolean {
        if (m.getAccessToken(sharedPreferences) == "empty")
            return (false)
        return (true)
    }
}
