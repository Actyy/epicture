package com.example.epicture.vue

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.epicture.R
import com.example.epicture.controller.controller

class MyWebViewClient(controller: controller, private val sharedPreferences: SharedPreferences) : WebViewClient() {
    private val controller : controller? = controller
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (url != null) {
            view?.loadUrl(url)
            this.controller?.splitUrl(url, sharedPreferences)
            this.controller?.redirectAfterLogin(sharedPreferences)
        }
        return (true)
    }
}

class imgurLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imgur_login)
        this.supportActionBar?.hide()
        val webView = findViewById(R.id.ImgurLogin) as WebView
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().cacheMode = WebSettings.LOAD_NO_CACHE;

        val c = controller(this)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences("token_save", Context.MODE_PRIVATE)
        webView.webViewClient = MyWebViewClient(c, sharedPreferences)
        c.loadLogin(webView)


        //webView.loadUrl("https://www.google.com./")
    }
}