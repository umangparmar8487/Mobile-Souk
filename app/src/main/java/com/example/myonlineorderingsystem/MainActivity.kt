package com.example.myonlineorderingsystem

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.myonlineorderingsystem.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var networkChangeReceiver: BroadcastReceiver


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkIfUserIsLoggedIn()
//internet mate ni permission
        networkChangeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show()
                    binding.myImageView.visibility=View.VISIBLE
                    binding.fragmentContainerView.visibility=View.GONE
                  //  binding.nointernet.visibility= View.VISIBLE
                    return
                }
                else{
                    binding.myImageView.visibility=View.GONE
                    binding.fragmentContainerView.visibility=View.VISIBLE
                    //binding.nointernet.visibility=View.GONE
                }
            }
        }
        //notification mate
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, arrayOf( android.Manifest.permission.POST_NOTIFICATIONS), 123 )
        }
        registerReceiver(
            networkChangeReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChangeReceiver)
    }
//login hase to direct home activity ma jase
    private fun checkIfUserIsLoggedIn() {
        val isLoggedIn = applicationshare.sharedPreferences.getBoolean("is_logged_in", false)
        if (isLoggedIn) {
            val homeIntent = Intent(this, HomeActivity::class.java)
            startActivity(homeIntent)
            finish()
        }
    }
//internet mate
private fun isNetworkAvailable(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        connectivityManager.activeNetwork
    } else {
        TODO("VERSION.SDK_INT < M")
    }
    if (network != null) {
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }
    return false
}

}