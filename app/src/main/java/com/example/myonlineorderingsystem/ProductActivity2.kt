package com.example.myonlineorderingsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.myonlineorderingsystem.databinding.ActivityProduct2Binding
import com.example.myonlineorderingsystem.databinding.ActivityProductBinding

class ProductActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityProduct2Binding
    private lateinit var productviewmodel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_product2)
        productviewmodel = ViewModelProvider(this)[MainViewModel::class.java]

        val productName = intent.getStringExtra("product_name2")
        val productImage = intent.getStringExtra("product_image2")
        val productPrice = intent.getStringExtra("product_price2")
        val productDescription = intent.getStringExtra("product_description2")

        binding.ppProductImageView.load(productImage)
        binding.ppProductnameTxt.text = productName
        binding.ppPriceTxt.text = "Price: $ ${productPrice}"
        binding.ppDescriptionTxt.text = productDescription

        binding.ppBackIcon.setOnClickListener{
            @Suppress("DEPRECATION")
            onBackPressed()
        }
    }
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            @Suppress("DEPRECATION")
            super.onBackPressed()
        }
    }
    }

