package com.example.epicture.model

class ModelSearch {
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
}