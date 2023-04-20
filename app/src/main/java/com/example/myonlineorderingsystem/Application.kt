package com.example.myonlineorderingsystem
import android.app.Application
import android.content.SharedPreferences

class applicationshare: Application() {
    companion object{
        lateinit var sharedPreferences :SharedPreferences
        lateinit var  editor: SharedPreferences.Editor
    }
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences("MYPREFERENCE", MODE_PRIVATE)
        editor= sharedPreferences.edit()
    }
    fun clearJwtToken() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}