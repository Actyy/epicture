package com.example.epicture.controller

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.widget.*
import coil.load
import com.example.epicture.model.ModelSearch
import okhttp3.*
import java.io.IOException


class searchController(private val context: Context): Callback {
    private val appData = com.example.epicture.model.model()
    private val searchData = ModelSearch()
    private lateinit var scrollView : LinearLayout

    public fun doSearch(searchStr: Editable, sortStr: String) {
        var url = "https://api.imgur.com/3/gallery/search/" + sortStr + "?q=" + searchStr + "&q_type=jpg"
        val client = OkHttpClient().newBuilder()
            .build()
        val request: Request = Request.Builder()
            .url(url)
            .method("GET", null)
            .addHeader("Authorization", "Client-ID " + appData.getClientId())
            .build()
        val response: Unit = client.newCall(request).enqueue(this)
    }

    override fun onFailure(call: Call, e: IOException) {
        Toast.makeText(context, "Could not retrieve data, check your internet connexion", Toast.LENGTH_SHORT).show()
    }

    override fun onResponse(call: Call, response: Response) {
        var data = response.body()?.string()
        if (data != null) {
            getUrlsFromData(data)
        }
    }

    private fun getUrlsFromData(data: String) {
        println(data)
        var urls : MutableList<String> = ArrayList()
        var splittedData = data.split('{')
        for(i in 2 until splittedData.size) {
            if (splittedData[i].startsWith("\"id\":")) {
                var tmp = splittedData[i].split("\"link\":\"")[1]
                tmp = tmp.split("\"")[0]
                tmp = tmp.replace("\\/", "/")
                if(tmp.endsWith(".jpg")) {
                    urls.add(tmp)
                }
            }

        }
        searchData.setPictures(urls)
        this.displayPictures()
    }

    public fun displayPictures() {
        Handler(Looper.getMainLooper()).post(Runnable {
            this.scrollView.removeAllViews()
        })
        var urls = getPictures()
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

    public fun getPictures() : MutableList<String>{
        return(searchData.getPictures())
    }

    public fun getSearchArgs(buttonSearch: Button, searchContent: EditText, switch1: Switch, scrollViewSearch: LinearLayout) {
        this.scrollView = scrollViewSearch
        switch1.setOnClickListener() {
            if (searchContent.text.length != 0) {
                var sortType = ""
                if (switch1.isChecked) {
                    sortType = "viral"
                } else {
                    sortType = "time"
                }
                var searchContent = searchContent.text
                doSearch(searchContent, sortType)
            }
        }
        buttonSearch.setOnClickListener {
            if (searchContent.text.length == 0) {
                Toast.makeText(context, "You need to enter content to search for !", Toast.LENGTH_SHORT).show()
            } else {
                var sortType = ""
                if (switch1.isChecked) {
                    sortType = "viral"
                } else {
                    sortType = "time"
                }
                var searchContent = searchContent.text
                doSearch(searchContent, sortType)
            }
        }
    }
}
