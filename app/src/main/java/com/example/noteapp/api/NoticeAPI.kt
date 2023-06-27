package com.example.noteapp.api

import com.example.noteapp.model.NoticeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.Path

interface NoticeAPI {

    @GET("file/all")
    suspend fun getNotices() : Response<List<NoticeResponse>>

    @GET("file/{fileName}")
    suspend fun downloadFile(@Path("fileName") fileName: String) : Response<Multipart>
}