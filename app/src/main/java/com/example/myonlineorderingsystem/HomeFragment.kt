package com.example.myonlineorderingsystem

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myonlineorderingsystem.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var productViewModel: MainViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        productViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        productViewModel.productList.observe(viewLifecycleOwner, Observer {
            binding.overlay.visibility = View.GONE
            binding.HomeRecycleview.visibility=View.VISIBLE
            binding.HomeRecycleview.layoutManager = LinearLayoutManager(context)
            val adapterx = HomeproductlistAdapter(productViewModel , requireContext())
            adapterx.submitList(it)
            binding.HomeRecycleview.adapter = adapterx
        })
        productViewModel.getProductData(requireContext())

        return binding.root
    }
    override fun onResume() {
        binding.overlay.visibility=View.VISIBLE
        binding.HomeRecycleview.visibility=View.GONE
        super.onResume()
        val token = applicationshare.sharedPreferences.getString("jwtToken", null)
        if (token != null) {
            productViewModel.getProductData(requireContext())
        }
    }
}