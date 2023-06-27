package com.example.noteapp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.noteapp.api.UserAPI
import com.example.noteapp.model.UserRegister
import com.example.noteapp.model.UserRequest
import com.example.noteapp.model.UserResponse
import com.example.noteapp.utils.Constants.TAG
import com.example.noteapp.utils.NetworkResult
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {

    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: MutableLiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData

    suspend fun registerUser(userRegister: UserRegister){
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.registerUser(userRegister)
        handleResponse(response)
    }


    suspend fun loginUser(userRequest: UserRequest){
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.loginUser(userRequest)
        handleResponse(response)
    }

    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObject = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObject.getString("message")))
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Unknown Error"))
        }
    }
}