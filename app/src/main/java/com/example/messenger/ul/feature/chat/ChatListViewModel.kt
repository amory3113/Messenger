package com.example.messenger.ul.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messenger.domain.model.Chat
import com.example.messenger.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class ChatItem(
    val chat: Chat,
    val peerUser: User
)

class ChatListViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val _chatItems = MutableStateFlow<List<ChatItem>>(emptyList())
    val chatItems: StateFlow<List<ChatItem>> = _chatItems

    init {
        listenForChats()
    }

    private fun listenForChats() {
        val currentUid = auth.currentUser?.uid ?: return

        db.collection("chats")
            .whereArrayContains("participants", currentUid)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val chats = snapshot.toObjects(Chat::class.java)
                viewModelScope.launch {
                    val enrichedItems = chats.mapNotNull { chat ->
                        val peerId = chat.participants.find { it != currentUid } ?: return@mapNotNull null
                        val userDoc = db.collection("users").document(peerId).get().await()
                        val user = userDoc.toObject(User::class.java) ?: return@mapNotNull null
                        ChatItem(chat, user)
                    }.sortedByDescending { it.chat.lastMessageTime }
                    _chatItems.value = enrichedItems
                }
            }
    }
}