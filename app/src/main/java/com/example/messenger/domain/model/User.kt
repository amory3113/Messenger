package com.example.messenger.domain.model

data class User(
    val uid: String = "",
    val fullName: String = "",
    val email: String= "",
    val nickname: String = "",
    val avatarUrl: String = "",
    val phoneNumber: String = "",
    val createdAt: Long = 0L,
    val isOnline: Boolean = false,
    val lastSeen: Long = 0L
)