
package com.example.myonlineorderingsystem

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myonlineorderingsystem.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var ViewModel: MainViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment using FragmentHomeBinding.
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Initialize the ViewModel.
        ViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        // Observe changes to the product list and update the RecyclerView accordingly.
        ViewModel.productList.observe(viewLifecycleOwner, Observer {
            // Hide the loading overlay and show the RecyclerView.
            binding.overlay.visibility = View.GONE
            binding.HomeRecycleview.visibility=View.VISIBLE

            // Set the RecyclerView's layout manager and adapter.
            binding.HomeRecycleview.layoutManager = LinearLayoutManager(context)
            val adapterx = HomeproductlistAdapter(ViewModel , requireContext())
            adapterx.submitList(it)
            binding.HomeRecycleview.adapter = adapterx
        })

        // Fetch the product data.
      ViewModel.getProductData(requireContext())

        // Return the root view.
        return binding.root
    }
    override fun onResume() {
        // Show the loading overlay and hide the RecyclerView.
        binding.overlay.visibility=View.VISIBLE
        binding.HomeRecycleview.visibility=View.GONE

        // Call getProductData() to fetch the latest product data, if the user is logged in.
        super.onResume()
        val token = applicationshare.sharedPreferences.getString("jwtToken", null)
        if (token != null) {
            ViewModel.getProductData(requireContext())
        }
    }
}
