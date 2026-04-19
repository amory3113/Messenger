package com.example.messenger.ul.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messenger.domain.model.Message
import com.example.messenger.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ChatViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages
    private val _peerUser = MutableStateFlow<User?>(null)
    val peerUser: StateFlow<User?> = _peerUser

    fun initChat(chatId: String){
        listenForMessages(chatId)
        fetchPeerInfo(chatId)
    }

    fun fetchPeerInfo(chatId: String){
        val currentUid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                val chatDoc = db.collection("chats").document(chatId).get().await()
                val participants = chatDoc.get("participants") as? List<String> ?: return@launch
                val peerId = participants.find { it != currentUid } ?: return@launch

                val userDoc = db.collection("users").document(peerId).get().await()
                _peerUser.value = userDoc.toObject(User::class.java)
            } catch (e: Exception) {

            }
        }
    }

    fun listenForMessages(chatId: String){
        db.collection("chats").document(chatId).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val msgs = snapshot.toObjects(Message::class.java)
                _messages.value = msgs
            }
    }
    fun sendMessage(chatId: String, text: String) {
        if(text.isBlank()) return
        val currentUid = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                val messageRef = db.collection("chats").document(chatId).collection("messages").document()
                val message = Message(
                    messageId = messageRef.id,
                    senderId = currentUid,
                    text = text,
                    timestamp = System.currentTimeMillis()
                )
                messageRef.set(message)
                db.collection("chats").document(chatId).update(
                    mapOf("lastMessage" to text, "lastMessageTime" to message.timestamp)
                ).await()
            } catch (e: Exception) {

            }
        }
    }
    fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: ""
    }
}