package com.basitbhatti.notesapp.supabase.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basitbhatti.notesapp.supabase.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repo = AuthRepository()

    private val _uiState = MutableStateFlow<AuthState>(AuthState.Idle)
    val uiState: StateFlow<AuthState> = _uiState

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthState.Loading
            repo.signIn(email, password)
                .onSuccess { _uiState.value = AuthState.Success }
                .onFailure { _uiState.value = AuthState.Error(it.message ?: "Error") }
        }
    }

    fun signUp(email: String, password: String){
        viewModelScope.launch {
            _uiState.value = AuthState.Loading
            repo.signUp(email, password)
                .onSuccess { _uiState.value = AuthState.Success }
                .onFailure { _uiState.value = AuthState.Error(it.message?:"Error") }
        }
    }

}


sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}