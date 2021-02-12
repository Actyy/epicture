package com.example.epicture.vue.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.epicture.R
import com.example.epicture.controller.uploadController
import kotlinx.android.synthetic.main.fragment_upload.view.*


class uploadFragment(uploadController: uploadController, sharedPreferences: SharedPreferences) : Fragment() {
    private var controller = uploadController
    private var sharedPreferences = sharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view =  inflater.inflate(R.layout.fragment_upload, container, false)
        controller.addImage(view.imageView)
        controller.takeImage(view.cameraButton)
        if (sharedPreferences != null) {
            controller.submit(view.uploadButton, view.post_title, view.description, sharedPreferences)
        }
        return view
    }
}