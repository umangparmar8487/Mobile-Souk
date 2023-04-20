package com.example.myonlineorderingsystem

import android.app.Application
import android.content.SharedPreferences

class applicationshare: Application() {
    // Define companion object to access SharedPreferences and Editor from other classes.
    companion object {
        lateinit var sharedPreferences: SharedPreferences
        lateinit var editor: SharedPreferences.Editor
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize the SharedPreferences and Editor objects.
        sharedPreferences = getSharedPreferences("MYPREFERENCE", MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    // Define a method to clear the jwt token from SharedPreferences.
    fun clearJwtToken() {
        // Get an editor object.
        val editor = sharedPreferences.edit()

        // Clear the jwt token from SharedPreferences.
        editor.clear()
        editor.apply()
    }
}
