package com.binarChallenge.mymovies.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.binarChallenge.mymovies.R
import com.binarChallenge.mymovies.ViewModel.UserViewModel
import com.binarChallenge.mymovies.databinding.FragmentEditProfileBinding
import com.binarChallenge.mymovies.model.DatabaseStore
import com.binarChallenge.mymovies.model.User
import com.binarChallenge.mymovies.ui.MainActivity
import com.binarChallenge.mymovies.utils.Constant
import com.binarChallenge.mymovies.utils.SharedHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class EditProfileFragment : Fragment() {
    private var bind : FragmentEditProfileBinding? = null
    private val binding get() = bind!!
    private var user: DatabaseStore? = null
    private lateinit var shared: SharedHelper
    private lateinit var userViewModel: UserViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shared = SharedHelper(requireContext())
        user = DatabaseStore.getData(requireContext())
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        binding.apply {
            getDataProfile()


            btnEditSave.setOnClickListener {
                saveDataProfile()
            }

            btndelete.setOnClickListener {
                deleteDataProfile()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bind = null
    }

    private fun getDataProfile() {
        val username = shared.getString(Constant.USERNAME)
        when{
            user != null -> getUser(username)
        }
        binding.apply {
            userViewModel.user.observe(viewLifecycleOwner){
                Nameedit.setText(it.name)
                Emailedit.setText(it.email)
                bornedit.setText(it.born)
                addressedit.setText(it.adddress)
            }
        }
    }

    private fun getUser(username: String?) {
        lifecycleScope.launch(Dispatchers.IO) {
            val data = user?.userDao()?.getUsername(username)
            runBlocking(Dispatchers.Main) {
                data?.let {
                    userViewModel.dataUser(it)
                }
            }
        }
    }

    private fun saveDataProfile() {
        binding.apply {
            when {
                Nameedit.text.isNullOrEmpty() -> Nameedit.error = "Fill the name"
                Emailedit.text.isNullOrEmpty() -> Emailedit.error = "Fill the name"
                bornedit.text.isNullOrEmpty() -> bornedit.error = "Fill the age"
                addressedit.text.isNullOrEmpty() -> addressedit.error = "Fill the phone number"
                else -> {
                    userViewModel.user.observe(viewLifecycleOwner){
                        val newData = User(
                            it.id,
                            Nameedit.text.toString(),
                            Emailedit.text.toString(),
                            bornedit.text.toString(),
                            addressedit.text.toString(),
                            it.username,
                            it.password
                        )

                        lifecycleScope.launch(Dispatchers.IO) {
                            val res = user?.userDao()?.updateProfileUser(newData)
                            runBlocking(Dispatchers.Main) {
                                when {
                                    res != 0 -> {
                                        Toast.makeText(requireContext(), "Edit Profile Success", Toast.LENGTH_SHORT).show()
//                                        findNavController().navigate(R.id.action_editProfileFragment_to_profileUserFragment)
                                        activity?.let {
                                            val intent = Intent(it, MainActivity::class.java)
                                            it.startActivity(intent)}
                                    }
                                    else -> Toast.makeText(requireContext(), "Edit Profile Failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun deleteDataProfile() {
        binding.apply {
            userViewModel.user.observe(viewLifecycleOwner){
                val newData = User(
                    it.id,
                    Nameedit.text.toString(),
                    Emailedit.text.toString(),
                    bornedit.text.toString(),
                    addressedit.text.toString(),
                    it.username,
                    it.password
                )

                lifecycleScope.launch(Dispatchers.IO) {
                    val res = user?.userDao()?.deleteUser(newData)
                    runBlocking(Dispatchers.Main) {
                        when {
                            res != 0 -> {
                                shared.clear()
                                Toast.makeText(requireContext(), "Delete User Success", Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_editProfileFragment_to_loginFragment)
                            }
                            else -> Toast.makeText(requireContext(), "Delete User Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}