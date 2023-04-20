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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myonlineorderingsystem.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    private lateinit var networkChangeReceiver: BroadcastReceiver
    private var isNetworkConnected = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())



        networkChangeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show()
                    binding.networkimage.visibility=View.VISIBLE
                    binding.frameLayout1.visibility=View.GONE
                    binding.bottomNavigationView.visibility=View.GONE
                    return
                }
                else{
                    MainViewModel().getProductData(this@HomeActivity)
                    binding.networkimage.visibility=View.GONE
                    binding.frameLayout1.visibility=View.VISIBLE
                    binding.bottomNavigationView.visibility=View.VISIBLE

                }
            }
        }
        registerReceiver(
            networkChangeReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.wishlist -> replaceFragment(WishlistFragment())
                R.id.cart -> replaceFragment(CartFragment())
                R.id.orders -> replaceFragment(OrdersFragment())
                R.id.profile -> replaceFragment(ProfileFragment())
                else -> {}
            }
            true
        }

        val abc = intent.getBooleanExtra("fragment", false)
        if (abc) {
            replaceFragment(CartFragment())
            binding.bottomNavigationView.selectedItemId = R.id.cart
        }

        val abcd = intent.getBooleanExtra("fragment1", false)
        if (abcd) {
            replaceFragment(WishlistFragment())
            binding.bottomNavigationView.selectedItemId = R.id.wishlist
        }

        val xyz=intent.getBooleanExtra("fragment3",false)
        if(xyz){
            replaceFragment(OrdersFragment())
            binding.bottomNavigationView.selectedItemId=R.id.orders
        }
    }

    private fun replaceFragment(fragment: Fragment) {
       supportFragmentManager.beginTransaction()
        .replace(R.id.frame_layout_1, fragment)
        .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChangeReceiver)
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_layout_1)
        if (currentFragment is HomeFragment) {
            @Suppress("DEPRECATION")
            super.onBackPressed()
        } else {
            replaceFragment(HomeFragment())
            binding.bottomNavigationView.selectedItemId = R.id.home
        }
    }

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


