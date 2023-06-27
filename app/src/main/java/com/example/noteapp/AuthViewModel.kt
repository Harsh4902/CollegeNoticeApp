package com.example.noteapp

import android.provider.ContactsContract.CommonDataKinds.Email
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.model.UserRegister
import com.example.noteapp.model.UserRequest
import com.example.noteapp.model.UserResponse
import com.example.noteapp.repository.UserRepository
import com.example.noteapp.utils.Constants
import com.example.noteapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

    val userResponseLiveData : LiveData<NetworkResult<UserResponse>>
    get() = userRepository.userResponseLiveData

    fun registerUser(userRegister: UserRegister){
        viewModelScope.launch {
            userRepository.registerUser(userRegister)
        }
    }

    fun loginUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }
    }

    fun validateCredential(userName: String, email: String, password:String) : Pair<Boolean,String>{
        var result = Pair(true,"")

        if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            result = Pair(false,"Please fill details")
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            result = Pair(false,"Please provide valid email")
        }
        else if((password.length <= 5) || (password.length > 16)){
            result = Pair(false,"Password should be minimum 6 and maximum 16 charachter long")
        }

        return result
    }

    fun validateCredentialForLogin(email: String, password: String): Pair<Boolean,String> {
        var result = Pair(true,"")
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            result = Pair(false,"Please Fill Details")
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            result = Pair(false,"Please provide valid email")
        }
        else if(password.length <= 5 && password.length > 16){
            result = Pair(false,"Password should be minimum 6 and maximum 16 charachter long")
        }

        return result
    }

}