package com.binarChallenge.mymovies.fragments

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class EditProfileFragment : Fragment() {
    private var bind : FragmentEditProfileBinding? = null
    private val binding get() = bind!!
    private var user: DatabaseStore? = null
    private lateinit var shared: SharedHelper
    private lateinit var userViewModel: UserViewModel
    var selectedImage : Uri? = null
    var selectedBitmap : Bitmap? = null


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

            super.onViewCreated(view, savedInstanceState)
            photoedit.setOnClickListener {
                select_image(it)
            }


        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==1){
            if (grantResults.size>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val intent = Intent (Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode==2 && resultCode == Activity.RESULT_OK && data !=null){
            selectedImage = data.data
        }
        try {
            context?.let{
                if(selectedImage!=null){
                    if(Build.VERSION.SDK_INT >=28){
                        val source = ImageDecoder.createSource(it.contentResolver, selectedImage!!)
                        selectedBitmap = ImageDecoder.decodeBitmap(source)
                        photoedit.setImageBitmap(selectedBitmap)
                    }
                }
            }
        } catch (e : Exception){
            e.printStackTrace()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bind = null
    }

    private fun select_image(it: View?) {
        activity?.let {
            if (ContextCompat.checkSelfPermission(it.applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf((android.Manifest.permission.READ_EXTERNAL_STORAGE)),1)
            } else{
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            }

        }
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
//
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

    fun onSNACK(view: View){
        //Snackbar(view)
        val snackbar = Snackbar.make(view, "Edit Profile Success",
            Snackbar.LENGTH_LONG).setAction("Action", null)
        snackbar.setActionTextColor(Color.BLACK)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(Color.LTGRAY)
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.textSize = 28f
        snackbar.show()
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