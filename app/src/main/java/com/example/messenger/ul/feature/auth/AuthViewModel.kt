package com.example.messenger.ul.feature.auth

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.example.messenger.domain.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel(){
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun signUp(email: String, password: String, fullName: String, nickname: String) {

        if (email.isBlank() || password.isBlank() || fullName.isBlank() || nickname.isBlank()) {
            _authState.value = AuthState.Error("Заполните все поля")
            return
        }
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user
                if(firebaseUser != null){
                    val currentTime = System.currentTimeMillis()
                    val user = User(
                        uid = firebaseUser.uid,
                        fullName = fullName,
                        email = email,
                        nickname = nickname,
                        phoneNumber = "",
                        createdAt = currentTime,
                        isOnline = true,
                        lastSeen = currentTime
                    )
                    db.collection("users").document(user.uid).set(user).await()
                    _authState.value = AuthState.Success
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.localizedMessage ?: "Неизвестная ошибка")
            }
        }
    }
    fun resetState() {
        _authState.value = AuthState.Idle
    }

    fun login(email: String, password: String){
        if (email.isBlank() || password.isBlank()){
            _authState.value = AuthState.Error("Заполните поля")
            return
        }
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                _authState.value = AuthState.Success
            } catch (e: Exception){
                _authState.value = AuthState.Error("Неверная почта или пароль")
            }
        }
    }

}