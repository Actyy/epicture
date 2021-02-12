package com.example.epicture.model

import android.content.SharedPreferences

class model {
    private val clientId = "072a27538eebf82"
    private var username = ""
    private var id = ""

    fun setAccessToken(sharedPreferences: SharedPreferences, token : String) : Int {
        val editor: SharedPreferences.Editor =  sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
        return (0)
    }

    fun setAccountUsername(usernameUser : String) : Int {
        this.username = usernameUser
        return (0)
    }

    fun setAccountId(accountId : String) : Int {
        this.id = accountId
        return (0)
    }

    fun getAccessToken(sharedPreferences: SharedPreferences) : String? {
        var token = sharedPreferences.getString("token", "empty")
        return (token)
    }

    fun getClientId() : String {
        return(clientId);
    }

    fun getAccountId() : String {
        return(id);
    }

    fun getAccountUsername() : String {
        return(username);
    }
}