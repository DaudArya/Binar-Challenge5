package com.binarChallenge.mymovies.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.binarChallenge.mymovies.R
import com.binarChallenge.mymovies.databinding.FragmentRegisterBinding
import com.binarChallenge.mymovies.model.DatabaseStore
import com.binarChallenge.mymovies.model.User
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class RegisterFragment : Fragment() {
    private var bind : FragmentRegisterBinding? = null
    private val binding get() = bind!!
    private var dataUser: DatabaseStore? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataUser = DatabaseStore.getData(requireContext())

        binding.apply {

            submit.setOnClickListener {
                when {
                    fullname.text.isNullOrEmpty() -> fullname.error = "Fill column name"
                    email_id.text.isNullOrEmpty() -> email_id.error = "Fill column email"
                    birthday.text.isNullOrEmpty() -> birthday.error = "Fill column address"
                    address.text.isNullOrEmpty() -> address.error = "Fill column phone number"
                    usernameig.text.isNullOrEmpty() -> usernameig.error = "Fill column username"
                    pws.text.isNullOrEmpty() -> pws.error = "Fill column password"
                    else -> {
                        val objDataUser = User(
                            null,
                            fullname.text.toString(),
                            email_id.text.toString(),
                            birthday.text.toString(),
                            address.text.toString(),
                            usernameig.text.toString(),
                            pws.text.toString()
                        )
                        lifecycleScope.launch(Dispatchers.IO){
                                val user = usernameig.text.toString()
                                val data = dataUser?.userDao()?.getUsername(user)
                                when {
                                    data?.username != user -> {
                                        lifecycleScope.launch(Dispatchers.IO){
                                            dataUser?.userDao()?.insertUser(objDataUser)
                                            runBlocking(Dispatchers.Main) {
                                                Toast.makeText(requireContext(), "Register User Success", Toast.LENGTH_SHORT).show()
                                                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                                            }
                                        }
                                    }

                                    else -> {
                                        activity?.runOnUiThread {
                                            Toast.makeText(requireContext(), "Username has taken", Toast.LENGTH_SHORT).show()
                                        }

                                    }
                                }


                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bind = null
    }
}