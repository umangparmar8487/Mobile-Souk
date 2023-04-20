package com.example.myonlineorderingsystem

import Retrofitdatabase.DataXXXXXXXXX
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myonlineorderingsystem.databinding.FragmentOrdersBinding

class OrdersFragment : Fragment() {
    private lateinit var binding:FragmentOrdersBinding
    private lateinit var productViewModel:MainViewModel
    private lateinit var cartId:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding=FragmentOrdersBinding.inflate(inflater,container,false)
        productViewModel= ViewModelProvider(this)[MainViewModel::class.java]

        productViewModel.orders.observe(viewLifecycleOwner, Observer {
            binding.orderProgressbar.visibility=View.GONE
            binding.orderslistRecycleview.visibility=View.VISIBLE
            binding.orderslistRecycleview.layoutManager = LinearLayoutManager(context)
            binding.orderslistRecycleview.adapter = OrdersProductAdapter(productViewModel.orders)

            if(it.size!=0){
                cartId= it[0].orderId.toString()
            }
            else{
                binding.noOrdersImage.isVisible=true
            }
        })
        val token=applicationshare.sharedPreferences.getString("jwtToken",null)
        if (token != null) {
            productViewModel.getOrderslist(requireContext())
        }
        else{
            binding.noOrdersImage.isVisible=true
        }
        return binding.root
    }
    override fun onResume() {
        binding.orderProgressbar.visibility=View.VISIBLE
        binding.orderslistRecycleview.visibility=View.GONE
        super.onResume()
        productViewModel.getOrderslist(requireContext())
    }

}