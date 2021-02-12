package com.example.epicture.vue.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import coil.load
import com.example.epicture.R
import com.example.epicture.vue.baseActivity
import kotlinx.android.synthetic.main.fragment_home.view.*

class homeFragment(baseActivity: baseActivity) : Fragment() {
    private var controller = com.example.epicture.controller.controller(baseActivity)
    private var homeController = com.example.epicture.controller.homeController(baseActivity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        homeController.loadPictures(view.scroll_layout_profile)
        return view
    }
}