package com.example.myonlineorderingsystem

import Retrofitdatabase.DataX
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.imageview.ShapeableImageView

class HomeproductlistAdapter(
    private val productviewmodel: MainViewModel, private val context: Context,
) : ListAdapter<DataX, HomeproductlistAdapter.ViewHolder>(DiffU()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_product_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
        holder.itemView.setOnClickListener {
            productviewmodel.getProductData(context)
            val context = holder.itemView.context
            val intent = Intent(context, ProductActivity::class.java)
            intent.putExtra("product_name", product.title)
            intent.putExtra("product_image", product.imageUrl)
            intent.putExtra("product_price", product.price)
            intent.putExtra("product_description", product.description)
            intent.putExtra("product_id", product._id)
            intent.putExtra("cartid", product.cartItemId)
            intent.putExtra("wishlistid", product.watchListItemId)
            context.startActivity(intent)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ShapeableImageView = itemView.findViewById(R.id.product_list_image)
        private val namet: TextView = itemView.findViewById(R.id.product_list_name_txt)
        private val pricet: TextView = itemView.findViewById(R.id.product_list_price_txt)
        private val descriptiont: TextView =
            itemView.findViewById(R.id.product_list_description_txt)
        private val addtocartlist: Button = itemView.findViewById(R.id.h_Add_to_cart_btn)
        private val addtowishlist: Button = itemView.findViewById(R.id.h_Add_to_wish_btn)
        private val wishloading = itemView.findViewById<ProgressBar>(R.id.homewish_overlay)

        fun bind(product: DataX) {
            image.load(product.imageUrl)
            namet.text = product.title
            pricet.text = "M.R.P: $${product.price}"
            descriptiont.text = product.description
            val cartid = product.cartItemId
            if (cartid != null) {
                addtocartlist.text = "Already in cart"
                addtocartlist.setBackgroundColor(ContextCompat.getColor(context, R.color.lightgreen))
                addtocartlist.setTextColor(ContextCompat.getColor(context,R.color.pinkColor))
                addtocartlist.isEnabled=false
            } else {
                addtocartlist.text = "Add to cart"
                addtocartlist.setBackgroundColor(ContextCompat.getColor(context, R.color.pinkColor))
                addtocartlist.setTextColor(ContextCompat.getColor(context,R.color.white))
            }
            addtocartlist.setOnClickListener {
                if (cartid == null) {
                    addtocartlist.setBackgroundColor(ContextCompat.getColor(context, R.color.lightgreen))
                    addtocartlist.setTextColor(ContextCompat.getColor(context,R.color.pinkColor))
                    addtocartlist.isEnabled=false
                    addtocartlist.text = "Already in cart"
                    val x = product._id
                    productviewmodel.addtocart(x)
                    product.cartItemId = "abcd"
                } else {
                    addtocartlist.isEnabled = false
                }
            }
            val wishlistid = product.watchListItemId
            if (wishlistid != null) {
                addtowishlist.setBackgroundColor(ContextCompat.getColor(context, R.color.lightgreen))
                addtowishlist.setTextColor(ContextCompat.getColor(context,R.color.pinkColor))
                addtowishlist.text = "Already in wishlist"
            } else {
                addtowishlist.setBackgroundColor(ContextCompat.getColor(context, R.color.pinkColor))
                addtowishlist.setTextColor(ContextCompat.getColor(context,R.color.white))
                addtowishlist.text = "Add to wishlist"
            }
            addtowishlist.setOnClickListener {
                wishloading.visibility = View.VISIBLE
                addtowishlist.visibility = View.GONE
                if (wishlistid == null) {
                    addtowishlist.setBackgroundColor(ContextCompat.getColor(context, R.color.lightgreen))
                    addtowishlist.setTextColor(ContextCompat.getColor(context,R.color.pinkColor))
                    addtowishlist.text = "Already in wishlist"
                    addtocartlist.isEnabled=false
                    val x = product._id
                    productviewmodel.addtowishlistt(x)
                    product.watchListItemId = "abcd"
                    productviewmodel.getwishlist(context)
                    productviewmodel.getProductData(context)
                } else {
                    wishloading.visibility = View.GONE
                    addtowishlist.visibility = View.VISIBLE
                }
            }
        }
    }
}
class DiffU : DiffUtil.ItemCallback<DataX>() {
    override fun areItemsTheSame(oldItem: DataX, newItem: DataX): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: DataX, newItem: DataX): Boolean {
        return oldItem == newItem
    }
}