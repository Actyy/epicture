package com.example.epicture.controller

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import coil.load
import com.example.epicture.model.ModelProfile
import com.example.epicture.model.model
import com.example.epicture.vue.MainActivity
import okhttp3.*
import java.io.IOException


class profileController(private val context: Context): Callback {
    private var profileData = ModelProfile()
    private var appData = model()
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("token_save", Context.MODE_PRIVATE)
    private lateinit var scrollView : LinearLayout

    public fun loadPictures(scrollLayoutProfile: LinearLayout) {
        this.scrollView = scrollLayoutProfile
        val client = OkHttpClient().newBuilder()
            .build()
        val request: Request = Request.Builder()
            .url("https://api.imgur.com/3/account/me/images")
            .method("GET", null)
            .addHeader("Authorization", "Bearer " + appData.getAccessToken(sharedPreferences))
            .build()
        val response: Unit = client.newCall(request).enqueue(this)
    }

    override fun onFailure(call: Call, e: IOException) {
        println("RequestFailed check internet connexion")
    }

    override fun onResponse(call: Call, response: Response) {
        var data = response.body()?.string()
        if (data != null) {
            getUrlsFromData(data)
        }
    }

    private fun getUrlsFromData(data : String) {
        var urls : MutableList<String> = ArrayList()
        var splittedData = data.split('{')
        for(i in 2 until splittedData.size) {
            var tmp = splittedData[i].split("\"link\":")[1]
            tmp = tmp.split('"')[1]
            tmp = tmp.replace("\\/", "/")
            urls.add(tmp)
        }
        profileData.setPictures(urls)
        displayPictures()
    }

    public fun getPicturesUrls(): MutableList<String> {
        return(profileData.getPictures())
    }

    public fun displayPictures() {
        Handler(Looper.getMainLooper()).post(Runnable {
            this.scrollView.removeAllViews()
        })
        var urls = getPicturesUrls()
        var imageViews : MutableList<ImageView> = ArrayList()
        var count = 0
        var j = 0
        for(i in urls) {
            j++
            Handler(Looper.getMainLooper()).post(Runnable {
                imageViews.add(ImageView(context))
                imageViews[count].load(i);
                imageViews[count].id = count
                this.scrollView.addView(imageViews[count])
                count++
            })
            if (j == 10) {
                Thread.sleep(5000)
                j = 0
            }
        }
    }

    public fun disconnect(Disconnectbutton: Button) {
        Disconnectbutton.setOnClickListener() {
            profileData.removeToken(sharedPreferences)
            val intent = Intent(this.context, MainActivity::class.java)
            this.context.startActivity(intent)
        }
    }
}