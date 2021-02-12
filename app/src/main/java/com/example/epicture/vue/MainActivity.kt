package com.example.epicture.vue

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.epicture.R
import kotlinx.android.synthetic.main.activity_main.*

import com.example.epicture.controller.controller
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.annotation.TargetApi;
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        var c = controller(this)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("token_save", Context.MODE_PRIVATE)
        if (c.checkIfConnected(sharedPreferences)) {
            val intent = Intent(this, baseActivity::class.java)
            this.startActivity(intent)
        }
        c.login(Loginbutton)
    }
}