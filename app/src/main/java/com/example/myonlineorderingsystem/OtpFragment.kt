package com.example.myonlineorderingsystem

import Retrofitdatabase.*
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myonlineorderingsystem.databinding.FragmentOtpBinding
import com.otpview.OTPListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit
class OtpFragment : Fragment() {
    lateinit var binding: FragmentOtpBinding
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentOtpBinding.inflate(inflater, container, false)
        val args = this.arguments
        val UID = args?.getString("UID").toString()

        binding.otpView.otpListener = object : OTPListener {
            override fun onInteractionListener() {}
            override fun onOTPComplete(otp: String) {
                val regOtp = UserRegisterOtpRequest(UID, otp)
                val call = applicationApi.retrofitService.registerOtp(regOtp)
                call.enqueue(object : Callback<UserRegisterResponse> {
                    override fun onResponse(
                        call: Call<UserRegisterResponse>,
                        response: Response<UserRegisterResponse>,
                    ) {
                        if (response.code() == 200) {
                            findNavController().navigate(R.id.action_otpFragment_to_loginFragment)
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
        val resendotp=binding.resendOTPTxt
        countDownTimer = object : CountDownTimer(TimeUnit.SECONDS.toMillis(60), 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                // Update the remaining time on the resend OTP button
                val remainingTime = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
               resendotp.text = "Resend OTP in $remainingTime seconds"
                resendotp.isEnabled =false

            }
            @SuppressLint("SetTextI18n")
            override fun onFinish() {
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
                    // Restart the countdown timer for 60 seconds
                    countDownTimer.start()
                }
                override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
                    Toast.makeText(context, "Failed to send OTP", Toast.LENGTH_LONG).show()
                }
            })
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_otpFragment_to_loginFragment)
            }
        })
        return binding.root
    }
}


