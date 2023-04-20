package com.example.myonlineorderingsystem

import Retrofitdatabase.*
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myonlineorderingsystem.databinding.FragmentCartBinding
import retrofit2.Call
import retrofit2.Response
import kotlin.math.roundToInt

class CartFragment : Fragment() {
    private lateinit var productViewModel: MainViewModel
    private lateinit var binding: FragmentCartBinding
    private lateinit var cartId:String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        productViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        productViewModel.cartlist.observe(viewLifecycleOwner, Observer {
            binding.cartProgreess.visibility=View.GONE
            binding.Cartrecycleview.visibility=View.VISIBLE
            binding.cartlinearLayout.visibility=View.VISIBLE
            binding.Cartrecycleview.layoutManager = LinearLayoutManager(context)
            binding.Cartrecycleview.adapter = CartproductlistAdapter(
                productViewModel.cartlist,
                productViewModel,
                requireContext()
            )
            binding.cartProgreess.visibility=View.GONE
            binding.Cartrecycleview.visibility=View.VISIBLE
            binding.cartlinearLayout.visibility=View.VISIBLE

            if(it.size!=0){
                cartId= it[0].cartId.toString()
            }
            else{
                binding.placeorderBtn.isVisible =false
                binding.cartTotalTxt.isVisible=false
                binding.emptyCartImage.isVisible=true
            }
        })

        productViewModel.totalPrice.observe(this.viewLifecycleOwner, Observer { totalPrice ->
            binding.cartTotalTxt.text ="Total: $${String.format("%.2f", totalPrice.toDouble())}"
        })
        val token=applicationshare.sharedPreferences.getString("jwtToken",null)
        if (token != null) {
            productViewModel.getCartlist(requireContext())
        }
        binding.placeorderBtn.setOnClickListener {
            binding.cartProgreess.visibility=View.VISIBLE
            val token = applicationshare.sharedPreferences.getString("jwtToken", null).toString()
            val cartTotal=productViewModel.totalPrice.toString()
            if(cartTotal!=null){
            val regiUser = PlaceOrderRequest(cartId,cartTotal)
            val call = applicationApi.retrofitService.placeorder("Bearer $token",regiUser)
            call.enqueue(object : retrofit2.Callback<PlaceOrderResponse> {

                override fun onResponse(
                    call: Call<PlaceOrderResponse>,
                    response: Response<PlaceOrderResponse>,
                ) {
                    binding.cartProgreess.visibility=View.GONE
                    if(response.code()==201){
                        productViewModel.getCartlist(requireContext())
                        Toast.makeText(requireContext(),"your order is successfully placed",Toast.LENGTH_LONG).show()
                        showNotification("Order Placed", "Your order has been successfully placed.")
                        binding.orderplacedImage.visibility=View.VISIBLE
                        binding.Cartrecycleview.visibility=View.GONE
                        binding.cartlinearLayout.visibility=View.GONE
                        Log.d("RefreshProductData", "Cart list updated for user with token")
                    }
                    else if(response.code()==400){
                        Toast.makeText(requireContext(),"Failed to place order",Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<PlaceOrderResponse>, t: Throwable) {
                    Toast.makeText(requireContext(),"something went wrong failed to place order",Toast.LENGTH_LONG).show()
                }
            })
        } else {
                Toast.makeText(requireContext(), "Cart is empty", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }
    private fun showNotification(title: String, message: String) {
        if (applicationshare.sharedPreferences!!.getBoolean("check",false)){
        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "cart_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Cart", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        val intent = Intent(requireContext(), HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("fragment3", true)
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notificationBuilder = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.phonemartlogo)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        notificationManager.notify(1, notificationBuilder.build())
    }
    }
    override fun onResume() {
        binding.cartProgreess.visibility=View.VISIBLE
        binding.Cartrecycleview.visibility=View.GONE
        binding.cartlinearLayout.visibility=View.GONE
        binding.emptyCartImage.visibility=View.GONE
        super.onResume()
        productViewModel.getCartlist(requireContext())
    }
}

