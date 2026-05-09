package com.example.messenger.domain.model

import com.google.firebase.firestore.PropertyName

data class Message(
    val messageId: String="",
    val senderId: String="",
    val text: String="",
    val timestamp: Long = 0L,
    @get:PropertyName("isRead")
    @set:PropertyName("isRead")
    var isRead: Boolean = false,
)