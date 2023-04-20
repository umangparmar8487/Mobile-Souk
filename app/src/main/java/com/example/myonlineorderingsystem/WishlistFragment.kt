package com.example.myonlineorderingsystem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myonlineorderingsystem.databinding.FragmentWishlistBinding

class WishlistFragment : Fragment() {
    lateinit var binding: FragmentWishlistBinding
    private lateinit var productViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentWishlistBinding.inflate(inflater, container, false)
        productViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        productViewModel.wishlist.observe(viewLifecycleOwner, Observer {
            binding.wishProgress.visibility = View.GONE
            binding.wishlistRecycleview.visibility = View.VISIBLE
            binding.wishlistRecycleview.layoutManager = LinearLayoutManager(context)
            binding.wishlistRecycleview.adapter = WishlistproductAdapter(
                productViewModel.wishlist,
                productViewModel,
                requireContext()
            )

            if (it.size != 0) {
            } else {
                binding.emptyWishImage.isVisible = true
            }
        })
        val token = applicationshare.sharedPreferences.getString("jwtToken", null)
        if (token != null) {
            productViewModel.getwishlist(requireContext())
        }

        return binding.root
    }

    override fun onResume() {
        binding.wishProgress.visibility = View.VISIBLE
        binding.wishlistRecycleview.visibility = View.GONE
        super.onResume()
        productViewModel.getwishlist(requireContext())
    }
}
