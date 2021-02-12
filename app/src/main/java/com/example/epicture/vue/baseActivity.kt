 package com.example.epicture.vue

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.epicture.R
import com.example.epicture.vue.fragments.*
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.fragment_upload.*

class baseActivity : AppCompatActivity() {
    private val uploadController = com.example.epicture.controller.uploadController(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        this.supportActionBar?.hide()
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("token_save", Context.MODE_PRIVATE)

        val home = homeFragment(this)
        val profile = profileFragment(this)
        val search = searchFragment(this)
        val upload = uploadFragment(uploadController, sharedPreferences)
        val favourite = favouritesFragment()

        makeCurrentFragment(home)
        navigation_bottom.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_home -> makeCurrentFragment(home)
                R.id.ic_profile -> makeCurrentFragment(profile)
                R.id.ic_favourite -> makeCurrentFragment(favourite)
                R.id.ic_search -> makeCurrentFragment(search)
                R.id.ic_upload -> makeCurrentFragment(upload)
            }
            true
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        uploadController.onActivityResult(requestCode, resultCode, data, imageView)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        uploadController.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}