package com.example.noteapp.api

import com.example.noteapp.model.UserRegister
import com.example.noteapp.model.UserRequest
import com.example.noteapp.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {

    @POST("auth/register")
    suspend fun registerUser(@Body user: UserRegister): Response<UserResponse>

    @POST("auth/authenticate")
    suspend fun loginUser(@Body user: UserRequest): Response<UserResponse>

}