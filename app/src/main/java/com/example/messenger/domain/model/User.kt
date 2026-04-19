package com.example.messenger.domain.model

import com.google.firebase.firestore.PropertyName

data class User(
    val uid: String = "",
    val fullName: String = "",
    val email: String= "",
    val nickname: String = "",
    val avatarUrl: String = "",
    val phoneNumber: String = "",
    val createdAt: Long = 0L,
    val lastSeen: Long = 0L,

    @get:PropertyName("online")
    @set:PropertyName("online")
    var isOnline: Boolean = false
)