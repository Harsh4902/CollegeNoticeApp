package com.example.noteapp.model

import com.example.noteapp.model.User

data class UserResponse(
    val token: String,
    val user: User
)