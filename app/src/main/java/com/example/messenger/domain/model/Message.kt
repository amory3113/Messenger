package com.example.messenger.domain.model

data class Message(
    val messageId: String="",
    val senderId: String="",
    val text: String="",
    val timestamp: Long = 0L,
    val isRead: Boolean = false,
)