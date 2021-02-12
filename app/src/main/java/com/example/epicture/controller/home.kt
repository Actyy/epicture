package com.example.epicture.controller

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import coil.load
import com.example.epicture.model.Modelhome
import com.example.epicture.model.model
import kotlinx.android.synthetic.main.fragment_home.view.*
import okhttp3.*
import java.io.IOException


class homeController(private val context: Context): Callback {
    private var homeData = Modelhome()
    private var appData = model()
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("token_save", Context.MODE_PRIVATE)
    private lateinit var scrollView : LinearLayout

    public fun loadPictures(scrollLayoutProfile: LinearLayout) {
        this.scrollView = scrollLayoutProfile
        val clientID = "Client-ID " + appData.getClientId()
        val client = OkHttpClient().newBuilder()
            .build()
        val request: Request = Request.Builder()
            .url("https://api.imgur.com/3/gallery/hot/viral/day/1?showViral=true&mature=true&album_previews=true")
            .method("GET", null)
            .addHeader("Authorization", clientID)
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
        var id : MutableList<String> = ArrayList()
        var splittedData = data.split("\"link\":\"", "\", \"ups\"")
        var idData = data.split("{\"id\":\"", "\",\"title\":")
        var y = 1

        for(i in 2 until splittedData.size) {
            var tmp2 = idData[y]
            var tmp = splittedData[i].split("\",")[0]
            if (tmp.contains(".jpg") || tmp.contains(".png") || tmp.contains(".jpeg")) {
                if (tmp.isNotEmpty()) {
                    id.add(tmp2)
                    urls.add(tmp)
                }
            }
            y+=2
        }

        homeData.setPictures(urls)
        this.printPictures(urls)
    }

    public fun getViews(urls: MutableList<String>, imageViews: MutableList<ImageView>, view: View, count: Int, i: Int) : Int {
        var c = count
        for (i in urls) {
            imageViews.add(ImageView(context))
            imageViews[count].load(i);
            imageViews[count].id = count
            view.scroll_layout_profile.addView(imageViews[count])
            c++
        }
        return (c)
    }

    public fun printPictures(urls: MutableList<String>) {
        Handler(Looper.getMainLooper()).post(Runnable {
            this.scrollView.removeAllViews()
        })
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

    public fun getPicturesUrls(): MutableList<String> {
        return(homeData.getPictures())
    }
}