package com.example.myonlineorderingsystem

import Retrofitdatabase.ForgotPasswordRequest
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
import androidx.navigation.fragment.findNavController
import com.example.myonlineorderingsystem.databinding.FragmentEmailForforgrtpassBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Email_Forforgrtpass_Fragment : Fragment() {
    lateinit var binding: FragmentEmailForforgrtpassBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEmailForforgrtpassBinding.inflate(inflater, container, false)

        val genbtn=binding.frogetGenOtpBtn
        val progress=binding.forgateemailProgressbar
       genbtn.setOnClickListener {

            val emailId = binding.fEmailTxt.text.toString()
            if (verification()) {
                progress.visibility=View.VISIBLE
                genbtn.visibility=View.GONE
                val regiUser = ForgotPasswordRequest(emailId)
                val call = applicationApi.retrofitService.forgotpassword(regiUser)
                call.enqueue(object : Callback<UserRegisterResponse> {
                    override fun onResponse(
                        call: Call<UserRegisterResponse>,
                        response: Response<UserRegisterResponse>,
                    ) {
                        if (response.code() == 200) {
                            progress.visibility=View.VISIBLE
                           genbtn.visibility=View.GONE
                            val UserId = response.body()?.data?._id
                            val bundle = Bundle()
                            bundle.putString("UID2", UserId)
                            val fragment: Fragment = OtpFragment()
                            fragment.arguments = bundle
                            Log.e("go to otp fragment", "error")
                            findNavController().navigate(
                                R.id.action_email_Forforgrtpass_Fragment_to_OTP_For_Forgetpass_Fragment,
                                bundle
                            )
                        } else if (response.code() == 400) {
                           progress.visibility=View.GONE
                          genbtn.visibility=View.VISIBLE
                            Toast.makeText(
                                requireContext(),
                                "Plese enter right email and password.New user first register",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
                        progress.visibility=View.GONE
                      genbtn.visibility=View.VISIBLE
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG)
                            .show()
                    }
                })
            }
        }
        return binding.root
    }
    private fun verification(): Boolean {
        val email=binding.fEmailTxt
        if (email.text.toString().isEmpty()) {
            email.requestFocus()
          email.error = "Plese enter your email"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.requestFocus()
          email.error =
                "Invalid email address or If you are new user than first register"
            return false
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_OTP_For_Forgetpass_Fragment_to_loginFragment)
            }
        })
        return true
    }
}

