package com.example.epicture.vue.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.epicture.R
import com.example.epicture.controller.searchController
import com.example.epicture.vue.baseActivity
import kotlinx.android.synthetic.main.fragment_search.view.*

class searchFragment(baseActivity: baseActivity) : Fragment() {
    private val controller = searchController(baseActivity)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_search, container, false)
        controller.getSearchArgs(view.button_search, view.search_content, view.switch1, view.scroll_view_search)
        return view
    }
}