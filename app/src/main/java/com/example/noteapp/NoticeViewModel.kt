package com.example.noteapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor(private val noticeRepository: NoticeRepository): ViewModel() {

    val noticeLiveData get() = noticeRepository.noticeResponseLiveData
    val statusLiveData get() = noticeRepository.statusLivaData

    fun getAllNotices(){
        viewModelScope.launch(Dispatchers.IO) {
            noticeRepository.getAllNotices()
        }
    }

    fun downloadNotice(fileName : String){
        viewModelScope.launch (Dispatchers.IO){
            noticeRepository.downloadNotice(fileName)
        }
    }
}