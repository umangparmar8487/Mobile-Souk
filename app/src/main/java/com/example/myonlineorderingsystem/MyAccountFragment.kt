package com.example.myonlineorderingsystem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.myonlineorderingsystem.databinding.FragmentMyAccountBinding

class MyAccountFragment : Fragment() {
    private lateinit var binding:FragmentMyAccountBinding
    private lateinit var productviewmodel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding= FragmentMyAccountBinding.inflate(inflater,container,false)
        productviewmodel = ViewModelProvider(this)[MainViewModel::class.java]
        val name = applicationshare.sharedPreferences.getString("name", null)
        val number = applicationshare.sharedPreferences.getString("mobileno", null)
        val email = applicationshare.sharedPreferences.getString("email", null)

        binding.unumberTxt.text = number
        binding.uemailTxt.text = email
        binding.unameTxt.text=name

        binding.ChangeBackicon.setNavigationOnClickListener {
            @Suppress("DEPRECATION")
            productviewmodel.navigateBack(requireFragmentManager())
        }

        return binding.root
    }


}