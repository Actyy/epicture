package com.example.epicture.model

import android.content.SharedPreferences

class ModelProfile {
    private lateinit var username : String
    private lateinit var pictures : MutableList<String>

    public fun setPictures(pictures: MutableList<String>) {
        this.pictures = pictures
    }

    public fun getPictures() : MutableList<String> {
        if (this::pictures.isInitialized)
            return(this.pictures)
        else {
            var emptyList : MutableList<String> = ArrayList()
            return (emptyList)
        }
    }

    public fun removeToken (sharedPreferences: SharedPreferences) {
        val editor: SharedPreferences.Editor =  sharedPreferences.edit()
        editor.remove("token")
        editor.apply()
    }
}