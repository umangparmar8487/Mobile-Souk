package com.example.myonlineorderingsystem

import Retrofitdatabase.*
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CartproductlistAdapter(
    private val CartListLiveData: LiveData<MutableList<DataXXX>>,
    private val productviewmodel: MainViewModel,
    private val context:Context,
) :
    RecyclerView.Adapter<CartproductlistAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val cImageview: ShapeableImageView = itemView.findViewById(R.id.cart_list_image)
        val cName: TextView = itemView.findViewById(R.id.cart_name_txt)
        val cPrice: TextView = itemView.findViewById(R.id.cart_price_txt)
        val deleteButton: ImageView = itemView.findViewById(R.id.cart_remove_btn)
        val incrementButton: Button = itemView.findViewById(R.id.increment_btn)
        val decrementButton: Button = itemView.findViewById(R.id.decrement_btn)
        val quantitytext: TextView = itemView.findViewById(R.id.cart_quantity_txt)
        val producttotal: TextView = itemView.findViewById(R.id.product_totalprice_txt)
        val saveforlater: Button = itemView.findViewById(R.id.saveforlater_btn)
        val loading = itemView.findViewById<ProgressBar>(R.id.cartlist_overlay)
        val incrementloading=itemView.findViewById<ProgressBar>(R.id.cartincrement_progress)
        val decrementloading=itemView.findViewById<ProgressBar>(R.id.cartdecrement_progress)

        init {
            itemView.setOnClickListener(this)
            deleteButton.setOnClickListener {
                loading.visibility = View.VISIBLE
                deleteButton.visibility = View.GONE
                val x = CartListLiveData.value?.get(adapterPosition)?._id
                val token =
                    applicationshare.sharedPreferences.getString("jwtToken", null).toString()
                val removeFromCartObj = RemoveProductFromCartRequest(x)
                val callApi =
                    applicationApi.retrofitService.removetocart("Bearer $token", removeFromCartObj)
                callApi.enqueue(object : Callback<RemoveProductFromCartResponse> {
                    override fun onResponse(
                        call: Call<RemoveProductFromCartResponse>,
                        response: Response<RemoveProductFromCartResponse>,
                    ) {
                        if (response.code() == 200) {
                            loading.visibility = View.GONE
                            productviewmodel.getCartlist(context)
                        } else {
                            loading.visibility = View.GONE
                            deleteButton.visibility = View.VISIBLE
                            Log.e("getcartlist", "Error fetching products: ${response.code()}")
                        }
                    }
                    override fun onFailure(
                        call: Call<RemoveProductFromCartResponse>,
                        t: Throwable,
                    ) {
                        loading.visibility = View.GONE
                        deleteButton.visibility = View.VISIBLE
                    }
                })
            }

            saveforlater.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    CartListLiveData.value?.get(position)?.productDetails!!._id.let {
                        MainViewModel().addtowishlistt(it)
                    }
                }
            }
            incrementButton.setOnClickListener {
                incrementloading.visibility=View.VISIBLE
                incrementButton.visibility=View.GONE
                val product = CartListLiveData.value?.get(adapterPosition)
                val add = IncreaseProductQuantityRequest(product?._id.toString())
                val token =
                    applicationshare.sharedPreferences.getString("jwtToken", null).toString()
                val call =
                    applicationApi.retrofitService.increaseproductquantity("Bearer $token", add)
                call.enqueue(object : Callback<IncreaseProductQuantityResponse> {
                    override fun onResponse(
                        call: Call<IncreaseProductQuantityResponse>,
                        response: Response<IncreaseProductQuantityResponse>,
                    ) {
                        if (response.code() == 200) {
                            productviewmodel.getCartlist(context)
                        } else if (response.code() == 400) {
                        }
                    }

                    override fun onFailure(
                        call: Call<IncreaseProductQuantityResponse>,
                        t: Throwable,
                    ) {
                    }
                })
            }
            decrementButton.setOnClickListener {
                decrementloading.visibility=View.VISIBLE
                decrementButton.visibility=View.GONE
                val product = CartListLiveData.value?.get(adapterPosition)
                if (product?.quantity!! > 1) {
                    val decrease = DecreaseProductQuantityRequeast(product?._id.toString())
                    val token =
                        applicationshare.sharedPreferences.getString("jwtToken", null).toString()
                    val call = applicationApi.retrofitService.decreaseproductquantity(
                        "Bearer $token",
                        decrease
                    )
                    call.enqueue(object : Callback<DecreaseProductQuantityResponses> {
                        override fun onResponse(
                            call: Call<DecreaseProductQuantityResponses>,
                            response: Response<DecreaseProductQuantityResponses>,
                        ) {
                            if (response.code() == 200) {
                                productviewmodel.getCartlist(context)
                            } else if (response.code() == 400) {
                                decrementButton.visibility=View.VISIBLE
                                decrementloading.visibility=View.GONE
                            }
                        }
                        override fun onFailure(
                            call: Call<DecreaseProductQuantityResponses>,
                            t: Throwable,
                        ) {
                            decrementButton.visibility=View.VISIBLE
                            decrementloading.visibility=View.GONE
                            // handle network error
                        }
                    })
                }
                else if(product?.quantity!! ==1){
                    decrementloading.visibility=View.GONE
                    decrementButton.visibility=View.VISIBLE
                }
            }
        }
        override fun onClick(v: View?) {
            val context = itemView.context
            val intent = Intent(context, ProductActivity2::class.java)
            CartListLiveData.value?.get(adapterPosition)?.let { it ->
                intent.putExtra(
                    "product_name2",
                    CartListLiveData.value?.get(adapterPosition)?.productDetails?.title!!
                )
                intent.putExtra(
                    "product_image2",
                    CartListLiveData.value?.get(adapterPosition)?.productDetails?.imageUrl
                )
                intent.putExtra(
                    "product_price2",
                    CartListLiveData.value?.get(adapterPosition)?.productDetails?.price
                )
                intent.putExtra("cartid", CartListLiveData.value?.get(adapterPosition)?.cartId)
                Log.e("cartid", CartListLiveData.value?.get(adapterPosition)?.cartId.toString())
                intent.putExtra(
                    "product_description2",
                    CartListLiveData.value?.get(adapterPosition)?.productDetails?.description
                )
                intent.putExtra("quantity", CartListLiveData.value?.get(adapterPosition)?.quantity)
                intent.putExtra("product_id", CartListLiveData.value?.get(adapterPosition)?._id)
                context.startActivity(intent)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_product_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() =
        CartListLiveData.value?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartproduct = CartListLiveData.value?.get(position)
        Log.e("adapter", cartproduct.toString())
        if (cartproduct != null) {
            holder.cImageview.load(cartproduct.productDetails?.imageUrl)
            holder.cName.text = cartproduct.productDetails?.title
            holder.cPrice.text = "M.R.P: $" + cartproduct.productDetails?.price
            holder.quantitytext.text = cartproduct.quantity.toString()
            holder.producttotal.text = "Total: $" + cartproduct.itemTotal.toString()
        }
    }
}