package com.example.myonlineorderingsystem

import Retrofitdatabase.applicationApi
import Retrofitdatabase.changepasswordRequest
import Retrofitdatabase.changepasswordResponse
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myonlineorderingsystem.databinding.FragmentChangePasswordBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChangePasswordFragment : Fragment() {
    private lateinit var binding: FragmentChangePasswordBinding
    private lateinit var productviewmodel:MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        productviewmodel= ViewModelProvider(this)[MainViewModel::class.java]
        binding.ChangeBackicon.setNavigationOnClickListener {
            @Suppress("DEPRECATION")
            productviewmodel.navigateBack(requireFragmentManager())
        }
        binding.ordersSubmitButton.setOnClickListener {
            val newPassword = binding.newPasswordTxt.text.toString()
            val confirmPassword = binding.newConfirmpassTxt.text.toString()
            val token = applicationshare.sharedPreferences.getString("jwtToken", null)
            if (verification()) {
                val data = changepasswordRequest(newPassword, confirmPassword)
                val call = applicationApi.retrofitService.changepassword("Bearer $token", data)

                call.enqueue(object : Callback<changepasswordResponse> {
                    override fun onResponse(
                        call: Call<changepasswordResponse>,
                        response: Response<changepasswordResponse>
                    ) {
                        if (response.code()==200) {
                            (requireActivity().application as applicationshare).clearJwtToken()
                            val intent= Intent(requireActivity(),MainActivity::class.java)
                            intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            Toast.makeText(
                                requireContext(),
                                "Password changed successfully",
                                Toast.LENGTH_LONG
                            ).show()
                        } else if(response.code()==400) {
                            Toast.makeText(
                                requireContext(),
                                "Failed to change password",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<changepasswordResponse>, t: Throwable) {
                        Toast.makeText(
                            requireContext(),
                            "Failed to change password: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }
        }
        return binding.root
    }

    private fun verification(): Boolean {
        // Validate new password
        if (binding.newPasswordTxt.text.toString().isEmpty()) {
            binding.newPasswordTxt.requestFocus()
            binding.newPasswordTxt.error = "Please enter password"
            return false
        }
        // Validate new password confirmation
        if (binding.newConfirmpassTxt.text.toString().isEmpty()) {
            binding.newConfirmpassTxt.requestFocus()
            binding.newConfirmpassTxt.error = "Please enter confirm password"
            return false
        } else if (binding.newConfirmpassTxt.text.toString() != binding.newPasswordTxt.text.toString()) {
            binding.newConfirmpassTxt.requestFocus()
            binding.newConfirmpassTxt.error = "Passwords do not match"
            return false
        }
        return true
    }
}
