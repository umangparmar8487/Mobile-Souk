package com.example.myonlineorderingsystem

import Retrofitdatabase.*
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {


    private val _productList = MutableLiveData<List<DataX>?>()
    val productList: MutableLiveData<List<DataX>?>
        get() = _productList

    private val _cartlist = MutableLiveData<MutableList<DataXXX>>()
    val cartlist: LiveData<MutableList<DataXXX>>
        get() = _cartlist

    private val _wishlist = MutableLiveData<List<DataXXXXX>>()
    val wishlist: LiveData<List<DataXXXXX>>
        get() = _wishlist

    private val _orders = MutableLiveData<List<DataXXXXXXXXX>>()
    val orders: LiveData<List<DataXXXXXXXXX>>
        get() = _orders

    private var _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double>
        get() = _totalPrice

    private var _cartid = MutableLiveData<String>()
    val cartid: LiveData<String>
        get() = _cartid

    fun getProductData(context: Context) {
        val token = applicationshare.sharedPreferences.getString("jwtToken", null).toString()
        val call = applicationApi.retrofitService.productresponse("Bearer $token")
        call.enqueue(object : Callback<ProductResponse> {
            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>,
            ) {
                if (response.code() == 200) {
                    _productList.value = response.body()!!.data!!
                    Log.e("ProductViewModel", "Products fetched successfully")
                } else if (response.code() == 500) {
                    otherlogin(context)
                } else {
                    Log.e("ProductViewModel", "Error fetching products: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Log.e("ProductViewModel", "Failed to get product data", t)
            }
        })
    }

    fun getCartlist(context: Context) {
        val token = applicationshare.sharedPreferences.getString("jwtToken", null).toString()
        val dat = emptyList<DataXXX>().toMutableList()
        val call = applicationApi.retrofitService.getmycart("Bearer $token")
        call.enqueue(object : Callback<GetMyCartResponse> {
            override fun onResponse(
                call: Call<GetMyCartResponse>,
                response: Response<GetMyCartResponse>,
            ) {
                if (response.code() == 200) {
                    if (response.body()!!.data != null && response.body()!!.cartTotal != null) {
                        _cartlist.value = (response.body()!!.data as MutableList<DataXXX>?)!!
                        _totalPrice.value = response.body()!!.cartTotal!!
                        Log.e("getcartlist", cartlist.value.toString())
                        println("THIS is list : " + response.body()!!.data.toString())
                        Log.e("ViewModelCart", "Cart list fetched successfully")
                    } else {
                        _cartlist.value = dat
                    }
                } else if (response.code() == 500) {
                    otherlogin(context)
                } else {
                    Log.e("ViewModelCart", "Error fetching cart list: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetMyCartResponse>, t: Throwable) {
                Log.e("ProductViewModelCart", "Failed to get cart list", t)
            }
        })
    }

    fun getOrderslist(context: Context) {
        val token = applicationshare.sharedPreferences.getString("jwtToken", null).toString()
        val call = applicationApi.retrofitService.getorderhistory("Bearer $token")
        call.enqueue(object : Callback<GetOrderHistoryResponse> {
            override fun onResponse(
                call: Call<GetOrderHistoryResponse>,
                response: Response<GetOrderHistoryResponse>,
            ) {
                if (response.code() == 200) {
                    if (response.body()!!.data != null) {
                        _orders.value = (response.body()!!.data as MutableList<DataXXXXXXXXX>?)!!
                        Log.e("orderslist", orders.value.toString())
                        Log.e("orders", "order list fetched successfully")
                    }
                } else if (response.code() == 500) {
                    otherlogin(context)
                } else {
                    Log.e("orders", "Error fetching order list: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetOrderHistoryResponse>, t: Throwable) {
                Log.e("orders", "Failed to get orders list", t)
            }
        })
    }

    fun getwishlist(context: Context) {
        val token = applicationshare.sharedPreferences.getString("jwtToken", null).toString()
        val call = applicationApi.retrofitService.getwatchlist("Bearer $token")
        call.enqueue(object : Callback<GetWatchListResponse> {
            override fun onResponse(
                call: Call<GetWatchListResponse>,
                response: Response<GetWatchListResponse>,
            ) {
                if (response.code() == 200) {
                    if (response.body()!!.data != null) {
                        _wishlist.value = response.body()!!.data!!
                        Log.e("getwishlist", " succesfully getwishlist")
                    }
                    Log.e("getwishlist", wishlist.value.toString())
                } else if (response.code() == 500) {
                    otherlogin(context)
                } else {
                    Log.e("ProductViewModel", "Error fetching products: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetWatchListResponse>, t: Throwable) {
                Log.e("ProductViewModel", "Failed to get product data", t)
            }
        })
    }

    fun addtowishlistt(id: String?): LiveData<String> {
        val wishid = MutableLiveData<String>()
        val token = applicationshare.sharedPreferences.getString("jwtToken", null).toString()
        val request = AddToWatchListRequest(id)
        val call = applicationApi.retrofitService.addtowatchlist("Bearer $token", request)
        call.enqueue(object : Callback<AddToWatchListResponse> {
            override fun onResponse(
                call: Call<AddToWatchListResponse>,
                response: Response<AddToWatchListResponse>,
            ) {
                if (response.code() == 200) {
                    val responseData = response.body()?.data
                    if (responseData != null && responseData._id != null) {
                        wishid.value = response.body()!!.data?._id!!
                        Log.d("get product id", response.body()!!.data!!._id!!)
                        Log.e("Add to wishlist", "successfully add to wishlist")
                    } else {
                        Log.e("Add to wishlist", "response body or data is null")
                        wishid.value = "failed"
                    }
                } else if (response.code() == 400) {
                    Log.e("Add to wishlist", "not add to wishlist")
                    wishid.value = "failed"
                }
            }

            override fun onFailure(call: Call<AddToWatchListResponse>, t: Throwable) {
                Log.e("Add to wishlist", "some error occurred", t)
                wishid.value = "failed"
            }
        })
        return wishid
    }

    fun removefromewishlist(id: String?) {
        val token = applicationshare.sharedPreferences.getString("jwtToken", null).toString()
        val removeFromwishlist = RemoveFromWatchListRequest(id)
        val call =
            applicationApi.retrofitService.removefromwatchlist("Bearer $token", removeFromwishlist)
        call.enqueue(object : Callback<RemoveFromWatchListResponse> {
            override fun onResponse(
                call: Call<RemoveFromWatchListResponse>,
                response: Response<RemoveFromWatchListResponse>,
            ) {
                if (response.code() == 201) {
                    Log.e("remove from wishlist", "successfully remove from wishlist")
                } else if (response.code() == 400) {
                    Log.e("remove from wishlist", "not remove from wishlist")
                }
            }

            override fun onFailure(call: Call<RemoveFromWatchListResponse>, t: Throwable) {
                Log.e("remove from wishlist", "some error occurred", t)
            }
        })
    }

    fun addtocart(id: String?) {
        val token = applicationshare.sharedPreferences.getString("jwtToken", null).toString()
        val request = AddToCartRequest(id.toString())
        val call = applicationApi.retrofitService.addtocart("Bearer $token", request)
        call.enqueue(object : Callback<AddToCartResponse> {
            override fun onResponse(
                call: Call<AddToCartResponse>,
                response: Response<AddToCartResponse>,
            ) {
                if (response.code() == 201) {
                    _cartid.value = response.body()?.data?.cartId!!
                    Log.d("Cartid", _cartid.value.toString())
                    Log.e("ProductActivity", "successfully add to cart")
                } else if (response.code() == 400) {
                    Log.e("ProductActivity", "cannot add to cart")
                }
            }

            override fun onFailure(call: Call<AddToCartResponse>, t: Throwable) {
                Log.e("ProductActivity", "some error occurred", t)
            }
        })
    }

    private fun otherlogin(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
        applicationshare.editor.clear().commit()
    }

    fun navigateBack(fragmentManager: FragmentManager) {
        if (shouldNavigateBack(fragmentManager)) {
            fragmentManager.popBackStack()
        } else {
        }
    }

    private fun shouldNavigateBack(fragmentManager: FragmentManager): Boolean {
        return fragmentManager.backStackEntryCount > 0
    }
}
