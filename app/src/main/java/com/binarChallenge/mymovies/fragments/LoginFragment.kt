package com.binarChallenge.mymovies.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.binarChallenge.mymovies.R
import com.binarChallenge.mymovies.databinding.FragmentLoginBinding
import com.binarChallenge.mymovies.model.DatabaseStore
import com.binarChallenge.mymovies.ui.MainActivity
import com.binarChallenge.mymovies.utils.Constant
import com.binarChallenge.mymovies.utils.SharedHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {
    private var bind : FragmentLoginBinding? = null
    private val binding get() = bind!!
    private var dataUser: DatabaseStore? = null
    private lateinit var shared: SharedHelper


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataUser = DatabaseStore.getData(requireContext())
        shared = SharedHelper(requireContext())

        binding.apply {
            Registerw.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            Login.setOnClickListener {
                binding.apply {
                    val user = Username.text.toString()
                    val pass = Password.text.toString()

                    when {
                        user.isEmpty() && pass.isEmpty() -> {
                            Username.error = "Fill the username"
                            Password.error = "Fill the password"
                        }
                        user.isEmpty() -> Username.error = "Fill the username"
                        pass.isEmpty() -> Password.error = "Fill the password"
                        else -> {
//                            val ref = this
                            lifecycleScope.launch(Dispatchers.IO){
                                val data = dataUser?.userDao()?.getUsername(user)
//                            }

                                activity?.runOnUiThread {
                                    when (data?.username) {
                                        user -> when (data.password) {
                                            pass -> {
                                                loginSession(user, pass)
                                            Toast.makeText(requireContext(), "Login Success", Toast.LENGTH_SHORT).show()
                                                activity?.let {
                                                    val intent = Intent(it, MainActivity::class.java)
                                                    it.startActivity(intent)}
                                            }
                                            else -> Toast.makeText(requireContext(), "Wrong Password", Toast.LENGTH_SHORT).show()
                                        }
                                        else -> Toast.makeText(requireContext(), "Wrong Username", Toast.LENGTH_SHORT).show()
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (shared.getBoolean(Constant.LOGIN, false)){
            activity?.let {
                val intent = Intent(it,MainActivity::class.java)
                it.startActivity(intent)}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bind = null
    }

    private fun loginSession(user: String, pass: String) {
        shared.apply {
            put(Constant.USERNAME, user)
            put(Constant.PASSWORD, pass)
            put(Constant.LOGIN, true)
        }
    }
}