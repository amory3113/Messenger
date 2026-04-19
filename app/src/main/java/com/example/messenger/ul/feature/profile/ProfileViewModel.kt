package com.example.messenger.ul.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messenger.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val user: User) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

class ProfileViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    init {
        fetchUserProfile()
    }
    private fun fetchUserProfile(){
        val uid = auth.currentUser?.uid
        if (uid == null){
            _profileState.value = ProfileState.Error("User not authenticated")
            return
        }
        viewModelScope.launch {
            try {
                val document = db.collection("users").document(uid).get().await()
                val user = document.toObject(User::class.java)
                if(user != null){
                    _profileState.value = ProfileState.Success(user)
                } else {
                    _profileState.value = ProfileState.Error("Data not found")
                }
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.localizedMessage ?: "Loading error")
            }
        }
    }
    fun logout(){
        val uid = auth.currentUser?.uid
        if (uid != null) {
            db.collection("users").document(uid).update("online", false).addOnCompleteListener {
                auth.signOut()
            }
        }
    }
}