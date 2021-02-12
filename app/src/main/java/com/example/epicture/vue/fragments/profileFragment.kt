package com.example.epicture.vue.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.epicture.R
import com.example.epicture.vue.baseActivity
import kotlinx.android.synthetic.main.fragment_profile.view.*


class profileFragment(baseActivity: baseActivity) : Fragment() {
    private var controller = com.example.epicture.controller.controller(baseActivity)
    private var profileController = com.example.epicture.controller.profileController(baseActivity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_profile, container, false)
        profileController.loadPictures(view.scroll_layout_profile)
        profileController.disconnect(view.logoutButton)
        return view
    }
}