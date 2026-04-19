package com.example.messenger.ul.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.messenger.domain.model.Chat
import com.example.messenger.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class SearchState {
    object Idle: SearchState()
    object Loading : SearchState()
    data class Success(val users: List<User>) : SearchState()
    data class Error(val message: String) : SearchState()
}

class SearchViewModel : ViewModel(){
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val _searchState = MutableStateFlow<SearchState>(SearchState.Loading)
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private var allUsers = listOf<User>()
    init {
        fetchAllUser()
    }

    fun fetchAllUser() {
        viewModelScope.launch {
            try {
                val currencyUId = auth.currentUser?.uid
                val snapshot = db.collection("users").get().await()
                allUsers = snapshot.documents.mapNotNull { it.toObject(User::class.java) }
                    .filter { it.uid != currencyUId }
                if (_searchText.value.isBlank()) {
                    _searchState.value = SearchState.Idle
                } else {
                    onSearchTextChange(_searchText.value)
                }
            } catch (e: Exception) {
                _searchState.value = SearchState.Error(e.localizedMessage ?: "Loading error")
            }
        }
    }
    fun onSearchTextChange(text: String) {
        _searchText.value = text
        if(text.isBlank()){
            _searchState.value = SearchState.Idle
        } else {
            val query = text.removePrefix("@")

            val filteredList = allUsers.filter {
                it.fullName.contains(query, ignoreCase = true) ||
                        it.nickname.contains(query, ignoreCase = true)
            }
            _searchState.value = SearchState.Success(filteredList)
        }
    }

    fun createOrGetChat(targetUserId: String, onChatReady: (String) -> Unit) {
        val currentUserId = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                val querySnapshot = db.collection("chats")
                    .whereArrayContains("participants", currentUserId)
                    .get()
                    .await()
                var existingChatId: String? = null
                for(document in querySnapshot.documents) {
                    val chat = document.toObject(Chat::class.java)
                    if(chat != null && chat.participants.contains(targetUserId)) {
                        existingChatId = chat.chatId
                        break
                    }
                }
                if(existingChatId != null) {
                    onChatReady(existingChatId)
                } else {
                    val newChatRef = db.collection("chats").document()
                    val newChat = Chat(
                        chatId = newChatRef.id,
                        participants = listOf(currentUserId, targetUserId),
                        lastMessage = "",
                        lastMessageTime = System.currentTimeMillis()
                    )
                    newChatRef.set(newChat).await()
                    onChatReady(newChat.chatId)
                }
            } catch (e: Exception) {

            }
        }
    }

}
