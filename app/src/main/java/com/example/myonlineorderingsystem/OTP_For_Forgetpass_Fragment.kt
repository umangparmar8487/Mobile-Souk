package com.example.myonlineorderingsystem

import Retrofitdatabase.*
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myonlineorderingsystem.databinding.FragmentOTPForForgetpassBinding
import com.otpview.OTPListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class OTP_For_Forgetpass_Fragment : Fragment() {
    lateinit var binding: FragmentOTPForForgetpassBinding
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentOTPForForgetpassBinding.inflate(inflater, container, false)
        val args = this.arguments
        val UID = args?.getString("UID2").toString()
        val resendotp=binding.fResendOTPTxt
        binding.fotpView.otpListener = object : OTPListener {
            override fun onInteractionListener() {}
            override fun onOTPComplete(otp: String) {
                val regOtp = ForgotOtpRequest(UID, otp)
                val call = applicationApi.retrofitService.forgotpasswordotp(regOtp)
                call.enqueue(object : Callback<UserRegisterResponse> {
                    override fun onResponse(
                        call: Call<UserRegisterResponse>,
                        response: Response<UserRegisterResponse>,
                    ) {
                        if (response.code() == 200) {
                            Toast.makeText(requireContext(),"New password send your register mail",Toast.LENGTH_LONG).show()
                            findNavController().navigate(R.id.action_OTP_For_Forgetpass_Fragment_to_loginFragment)
                        } else if (response.code() == 400) {
                            Toast.makeText(context, "Wrong OTP", Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG)
                            .show()
                    }
                })
            }
        }

        Log.e("resendotp start", "error")

        countDownTimer = object : CountDownTimer(TimeUnit.SECONDS.toMillis(60), 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                // Update the remaining time on the resend OTP button
                val remainingTime = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                resendotp.text = "Resend OTP in $remainingTime seconds"
                resendotp.isEnabled=false
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                // Enable the resend OTP button
                resendotp.isEnabled = true
                resendotp.text = "Resend otp?"
            }
        }.start()

        resendotp.setOnClickListener {

            val regOtp = ResendOtpRequest(UID)
            val call = applicationApi.retrofitService.resendOtp(regOtp)
            call.enqueue(object : Callback<UserRegisterResponse> {
                override fun onResponse(
                    call: Call<UserRegisterResponse>,
                    response: Response<UserRegisterResponse>,
                ) {
                    if (response.code() == 200) {
                        Toast.makeText(context, "OTP sent succesfully", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Failed to send OTP", Toast.LENGTH_LONG).show()
                    }
                    countDownTimer.start()
                    // Start the countdown timer for 60 seconds
                }
                override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
                    Toast.makeText(context, "Failed to send OTP", Toast.LENGTH_LONG).show()
                }
            })
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_OTP_For_Forgetpass_Fragment_to_loginFragment)
            }
        })

        return binding.root
    }
}