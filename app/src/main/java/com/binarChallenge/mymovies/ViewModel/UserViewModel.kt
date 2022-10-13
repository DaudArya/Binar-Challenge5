package com.binarChallenge.mymovies.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.binarChallenge.mymovies.model.User

class UserViewModel : ViewModel() {

    val user : MutableLiveData<User> = MutableLiveData()
    fun dataUser(userEntity: User){
        user.postValue(userEntity)
    }
}