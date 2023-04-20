package com.example.myonlineorderingsystem

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myonlineorderingsystem.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var productviewmodel: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.logOutBtn.setOnClickListener {
            (requireActivity().application as applicationshare).clearJwtToken()
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        }

        binding.changePasswordBtn.setOnClickListener {
            val mySecondFragment = ChangePasswordFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout_1, mySecondFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        binding.myaccountTxt.setOnClickListener {
            val mySecondFragment = MyAccountFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout_1, mySecondFragment)
            transaction.addToBackStack(null)
            transaction.commit()

        }


        productviewmodel = ViewModelProvider(this)[MainViewModel::class.java]
        val name = applicationshare.sharedPreferences.getString("name", null)
        val number = applicationshare.sharedPreferences.getString("mobileno", null)
        val email = applicationshare.sharedPreferences.getString("email", null)

//        binding.unameTxt.text = name
//        binding.unumberTxt.text = number
//        binding.uemailTxt.text = email

        val notificationManager = NotificationManagerCompat.from(requireContext())
        val areNotificationsEnabled = notificationManager.areNotificationsEnabled()
        if (areNotificationsEnabled) {
            if (applicationshare.sharedPreferences.getBoolean("check", false)) {
                binding.notification.isChecked = true
            }
            binding.notification.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    applicationshare.editor.putBoolean("check", true).commit()
                } else {
                    applicationshare.editor.putBoolean("check", false).commit()
                }
            }
        } else {
            binding.notification.setOnClickListener{
            Toast.makeText(requireContext(),"Please turn on notifications in your device settings", Toast.LENGTH_LONG).show()
            binding.notification.isEnabled = false}
        }
        return binding.root
    }


}

