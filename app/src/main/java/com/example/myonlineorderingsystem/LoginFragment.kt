package com.example.myonlineorderingsystem

import Retrofitdatabase.UserLoginRequest
import Retrofitdatabase.UserRegisterResponse
import Retrofitdatabase.applicationApi
import android.content.Intent
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
import com.example.myonlineorderingsystem.databinding.FragmentLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        val login=binding.loginBtn
        val progress=binding.loginProgressbar
        binding.registerTxt.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.forgetPassTxt.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_email_Forforgrtpass_Fragment)
        }
        login.setOnClickListener {
            val emailId = binding.loginEmailTxt.text.toString()
            val password = binding.loginPasswordTxt.text.toString()

            if (verification()) {
                progress.visibility = View.VISIBLE
                login.visibility = View.GONE
                val regiUser = UserLoginRequest(emailId, password)
                val call = applicationApi.retrofitService.login(regiUser)
                call.enqueue(object : Callback<UserRegisterResponse> {
                    override fun onResponse(
                        call: Call<UserRegisterResponse>,
                        response: Response<UserRegisterResponse>,
                    ) {
                        Log.e("jwtresponse", "eror")
                        if (response.code() == 200) {
                            progress.visibility = View.VISIBLE
                            login.visibility = View.GONE
                            val editor = applicationshare.sharedPreferences.edit()
                            val jwtToken = response.body()?.data?.jwtToken.toString()
                            val name = response.body()?.data?.name
                            val number = response.body()?.data?.mobileNo
                            val email = response.body()?.data?.emailId
                            editor?.putString("jwtToken", jwtToken)
                            editor?.putString("name", name)
                            editor?.putString("mobileno", number)
                            editor?.putString("email", email)
                            editor?.putBoolean("check", true)
                            Log.e("jwttoken put", jwtToken)
                            editor.putBoolean("is_logged_in", true)
                            editor?.apply()
                            val intent = Intent(requireContext(), HomeActivity::class.java).apply {
                                flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            startActivity(intent)
                        } else if (response.code() == 400) {
                            progress.visibility = View.GONE
                            login.visibility = View.VISIBLE
                            Toast.makeText(
                                requireContext(),
                                "Plese enter right email and password . New user first register",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
                        progress.visibility = View.GONE
                        login.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG)
                            .show()
                    }
                })
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finishAffinity()
                }
            })
        return binding.root
    }

    private fun verification(): Boolean {
        val email=binding.loginEmailTxt
        val password=binding.loginPasswordTxt
        //Validate email
        if (email.text.toString().isEmpty()) {
            email.requestFocus()
            email.error = "Please enter your email"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.requestFocus()
            email.error =
                "Invalid email address"
            return false
        }
        //Validate password
        else if (password.text.toString().isEmpty()) {
            password.requestFocus()
            password.error = "Please enter your password"
            return false
        }
        return true
    }
}
