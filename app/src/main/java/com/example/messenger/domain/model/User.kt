package com.example.messenger.domain.model

data class User(
    val uid: String = "",
    val fullName: String = "",
    val email: String= "",
    val nickname: String = "",
    val avatarUrl: String = ""
)