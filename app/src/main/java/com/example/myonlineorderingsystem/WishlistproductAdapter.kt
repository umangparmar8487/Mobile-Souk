package com.example.myonlineorderingsystem

import Retrofitdatabase.DataXXXXX
import Retrofitdatabase.RemoveFromWatchListRequest
import Retrofitdatabase.RemoveFromWatchListResponse
import Retrofitdatabase.applicationApi
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WishlistproductAdapter(
    private val WishlistLiveData: LiveData<List<DataXXXXX>>,
    private val productviewmodel: MainViewModel,
    private val context: Context,
) : RecyclerView.Adapter<WishlistproductAdapter.Viewholder>() {

    inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val Imageview: ShapeableImageView = itemView.findViewById(R.id.wish_list_image)
        val Name: TextView = itemView.findViewById(R.id.wish_name_txt)
        val Price: TextView = itemView.findViewById(R.id.wish_price_txt)
        val removewishlisticon: View? = itemView.findViewById(R.id.remove_wish_icon)
        val description: TextView = itemView.findViewById(R.id.wish_description_txt)
        val progressBar: ProgressBar = itemView.findViewById(R.id.wishlist_overlay)
        var token: String

        init {
            itemView.setOnClickListener(this)

            removewishlisticon!!.setOnClickListener {
                val x = WishlistLiveData.value?.get(adapterPosition)?._id

                progressBar.visibility = View.VISIBLE
                removewishlisticon.visibility = View.GONE
                val token =
                    applicationshare.sharedPreferences.getString("jwtToken", null).toString()
                val removeFromwishlist = RemoveFromWatchListRequest(x)
                val call = applicationApi.retrofitService.removefromwatchlist(
                    "Bearer $token",
                    removeFromwishlist
                )
                call.enqueue(object : Callback<RemoveFromWatchListResponse> {
                    override fun onResponse(
                        call: Call<RemoveFromWatchListResponse>,
                        response: Response<RemoveFromWatchListResponse>,
                    ) {
                        progressBar.visibility = View.GONE
                        if (response.code() == 201) {
                            progressBar.visibility = View.GONE
                            productviewmodel.getwishlist(context)
                            Log.e("remove from wishlist", "successfully remove from wishlist")
                        } else if (response.code() == 400) {
                            progressBar.visibility = View.GONE
                            removewishlisticon.visibility = View.VISIBLE
                            Log.e("remove from wishlist", "not remove from wishlist")
                        }
                    }

                    override fun onFailure(call: Call<RemoveFromWatchListResponse>, t: Throwable) {
                        progressBar.visibility = View.GONE
                        removewishlisticon.visibility = View.VISIBLE

                        Log.e("remove from wishlist", "some error occurred", t)
                    }
                })
                productviewmodel.getwishlist(context)
            }

            applicationshare.sharedPreferences.let {
                token = it.getString("jwtToken", null).toString()
            }
        }

        override fun onClick(v: View?) {
            val context = itemView.context
            val intent = Intent(context, ProductActivity2::class.java)
            intent.putExtra(
                "product_name2",
                WishlistLiveData.value?.get(adapterPosition)?.productDetails?.title
            )
            intent.putExtra(
                "product_image2",
                WishlistLiveData.value?.get(adapterPosition)?.productDetails?.imageUrl
            )
            intent.putExtra(
                "product_price2",
                WishlistLiveData.value?.get(adapterPosition)?.productDetails?.price
            )
            intent.putExtra(
                "product_description2",
                WishlistLiveData.value?.get(adapterPosition)?.productDetails?.description
            )
            intent.putExtra("cartid", productviewmodel.cartid.toString())
            intent.putExtra("wishlistid", WishlistLiveData.value?.get(adapterPosition)?._id)
            val cartId: Int? = null
            intent.putExtra("cartid", cartId)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.wishlist_product_list, parent, false)
        return Viewholder(view)
    }

    override fun getItemCount() = WishlistLiveData.value?.size ?: 0

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val product = WishlistLiveData.value?.get(position)
        if (product != null) {
            holder.Imageview.load(product.productDetails?.imageUrl)
            holder.Name.text = product.productDetails?.title
            holder.Price.text = "M.R.P: $" + product.productDetails?.price
            holder.description.text = product.productDetails?.description
        }
    }

}


