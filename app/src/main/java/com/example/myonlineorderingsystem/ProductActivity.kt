package com.example.myonlineorderingsystem

import Retrofitdatabase.*
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.myonlineorderingsystem.databinding.ActivityProductBinding

class ProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductBinding
    private lateinit var productviewmodel: MainViewModel
    var wishlistid:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product)
        productviewmodel = ViewModelProvider(this)[MainViewModel::class.java]
        val productName = intent.getStringExtra("product_name")
        val productImage = intent.getStringExtra("product_image")
        val productPrice = intent.getStringExtra("product_price")
        val productDescription = intent.getStringExtra("product_description")
        val cartid = intent.getStringExtra("cartid")
        wishlistid = intent.getStringExtra("wishlistid")
        println("THis is " + wishlistid)
        binding.pProductImageView.load(productImage)
        binding.pProductnameTxt.text = productName
        binding.pPriceTxt.text = "M.R.P: $ ${productPrice}"
        binding.pDescriptionTxt.text =productDescription
        if (cartid != null) {
            binding.addToCartBtn.text = "Already in cart"
            binding.addToCartBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.lightgreen))
            binding.addToCartBtn.setTextColor(ContextCompat.getColor(this,R.color.pinkColor))
            binding.addToCartBtn.isEnabled=false
        } else {
            binding.addToCartBtn.text = "Add to cart"
        }
        binding.addToCartBtn.setOnClickListener {
            if (cartid == null) {
                binding.addToCartBtn.text = "Already in cart"
                binding.addToCartBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.lightgreen))
                binding.addToCartBtn.setTextColor(ContextCompat.getColor(this,R.color.pinkColor))
                binding.addToCartBtn.isEnabled=false
                val productId = intent.getStringExtra("product_id").toString()
                productviewmodel.addtocart(productId)
                productviewmodel.getProductData(this@ProductActivity)
            } else {
                Toast.makeText(this, "Product is already in cart", Toast.LENGTH_LONG).show()
            }
        }
        if (wishlistid != null) {
            binding.wishlistImg.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            binding.wishlistImg.setImageResource(R.drawable.baseline_favorite_border_24)
        }
        binding.wishlistImg.setOnClickListener {
            println("THis is " + wishlistid)
            if (wishlistid == null) {
                val productId = intent.getStringExtra("product_id").toString()
                val dat = productviewmodel.addtowishlistt(productId)
                binding.productOverlay.visibility = View.VISIBLE
                binding.wishlistImg.isVisible=false

                dat.observe(this, Observer {wishlistResponse ->
                    binding.productOverlay.visibility = View.GONE
                    binding.wishlistImg.isVisible=true
                    if(wishlistResponse!=null){
                        binding.wishlistImg.setImageResource(R.drawable.baseline_favorite_24)
                        wishlistid = wishlistResponse
                        productviewmodel.getProductData(this@ProductActivity)
                        productviewmodel.getwishlist(this)
                    }
                })
            } else{
                binding.productOverlay.visibility = View.VISIBLE
                binding.wishlistImg.isVisible=false
                print("Response $it")
                binding.wishlistImg.setImageResource(R.drawable.baseline_favorite_border_24)
                productviewmodel.removefromewishlist(wishlistid)
                wishlistid=null
                binding.productOverlay.visibility = View.GONE
                binding.wishlistImg.isVisible=true
            }
        }

        binding.pBackIcon.setOnClickListener{
            @Suppress("DEPRECATION")
            onBackPressed()
            productviewmodel.getProductData(this@ProductActivity)
        }

        binding.pCartIcon.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("fragment",true)
                flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        binding.pWishIcon.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("fragment1",true)
                flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }
    override fun onBackPressed() {
        productviewmodel.getProductData(this@ProductActivity)
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            @Suppress("DEPRECATION")
            super.onBackPressed()
        }
    }
}
