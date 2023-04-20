package com.example.myonlineorderingsystem

import Retrofitdatabase.UserRegisterRequest
import Retrofitdatabase.UserRegisterResponse
import Retrofitdatabase.applicationApi
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.myonlineorderingsystem.databinding.FragmentRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.coroutineContext

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.generateOtpBtn.setOnClickListener {
            val email = binding.rEmailTxt.text.toString()
            val mobileNo = binding.rMobileTxt.text.toString()
            val fullName = binding.rNameTxt.text.toString()
            val password = binding.rPasswordTxt.text.toString()
            val progress=binding.registerProgress
            val genertotp=binding.generateOtpBtn
            if (validation()) {
                progress.visibility = View.VISIBLE
                genertotp.visibility = View.GONE
                val regiUser = UserRegisterRequest(email, mobileNo, fullName, password)
                val call = applicationApi.retrofitService.registerUser(regiUser)
                call.enqueue(object : Callback<UserRegisterResponse> {
                    override fun onResponse(
                        call: Call<UserRegisterResponse>,
                        response: Response<UserRegisterResponse>,
                    ) {
                        Log.e("response", response.code().toString())
                        if (response.code() == 201) {
                            progress.visibility = View.VISIBLE
                           genertotp.visibility = View.GONE
                            val UserId = response.body()?.data?._id
                            val bundle = Bundle()
                            bundle.putString("UID", UserId)
                            val fragment: Fragment = OtpFragment()
                            fragment.arguments = bundle
                            findNavController().navigate(
                                R.id.action_registerFragment_to_otpFragment,
                                bundle
                            )
                            Log.e("go to otp fragment", "error")
                        } else if (response.code() == 400) {
                            progress.visibility = View.GONE
                           genertotp.visibility = View.VISIBLE
                            Toast.makeText(context, "User already exists", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
                      progress.visibility = View.GONE
                     genertotp.visibility = View.VISIBLE
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }

        binding.loginTxt.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        })

        return binding.root
    }

    private fun validation(): Boolean {
        val name=binding.rNameTxt
        val number=binding.rMobileTxt
        val email=binding.rEmailTxt
        val password=binding.rPasswordTxt
        val confirmpass=binding.rConfirmpassTxt
        // Validate full name
        if (name.text.toString().isEmpty()) {
            name.requestFocus()
            name.error = "Please enter your name"
            return false
        }

        // Validate mobile number
        if (number.text.toString().isEmpty()) {
            number.requestFocus()
            number.error = "Please enter your mobile number"
            return false
        } else if (!Patterns.PHONE.matcher(number.text.toString()).matches()) {
            number.requestFocus()
            number.error = "Please enter valid mobile number"
            return false
        } else if (number.text.toString().length != 10) {
            number.requestFocus()
            number.error = "Enter 10 digit"
            return false
        }

        // Validate email
        if (email.text.toString().isEmpty()) {
            email.requestFocus()
            email.error = "Please enter your email"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.requestFocus()
          email.error = "Please enter valid email"
            return false
        }

        // Validate password
        if (password.text.toString().isEmpty()) {
            password.requestFocus()
           password.error = "Please enter password"
            return false
        }

        // Validate re-entered password
        if (confirmpass.text.toString().isEmpty()) {
           confirmpass.error = "Please enter confirm password"
            return false
        } else if (confirmpass.text.toString() != password.text.toString()) {
           confirmpass.requestFocus()
          confirmpass.error = "Passwords do not match"
            return false
        }
        return true
    }

}
