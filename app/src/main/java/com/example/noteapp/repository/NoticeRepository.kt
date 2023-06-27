package com.example.noteapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.noteapp.api.NoticeAPI
import com.example.noteapp.model.NoticeResponse
import com.example.noteapp.model.UserResponse
import com.example.noteapp.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NoticeRepository @Inject constructor(private val noticeAPI : NoticeAPI) {

    private val _noticeResponseLiveData = MutableLiveData<NetworkResult<List<NoticeResponse>>>()
    val noticeResponseLiveData : MutableLiveData<NetworkResult<List<NoticeResponse>>>
        get() = _noticeResponseLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLivaData : LiveData<NetworkResult<String>>
        get() = _statusLiveData

    suspend fun getAllNotices(){
        _noticeResponseLiveData.postValue(NetworkResult.Loading())
        val response = noticeAPI.getNotices()
        if (response.isSuccessful && response.body() != null) {
            _noticeResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObject = JSONObject(response.errorBody()!!.charStream().readText())
            _noticeResponseLiveData.postValue(NetworkResult.Error(errorObject.getString("message")))
        } else {
            _noticeResponseLiveData.postValue(NetworkResult.Error("Unknown Error"))
        }
    }

    suspend fun downloadNotice(fileName : String){
        _statusLiveData.postValue(NetworkResult.Loading())
    }

    private fun handleResponse(response: Response<NoticeResponse>, message : String) {
        if(response.isSuccessful && response.body() != null){
            _statusLiveData.postValue(NetworkResult.Success(message))
        }
        else{
            _statusLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

}