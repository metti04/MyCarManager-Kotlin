package com.example.mycarmanager.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycarmanager.dbServices.supabase.SupabaseInstance
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginActivityViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)

    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun loginUser(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _uiState.value = LoginUiState.Error("Tutti i campi sono obbligatori")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            /*try {
                SupabaseInstance.client.auth.signInWith(Email) {
                    this.email = email
                    this.password = pass
                }
                _uiState.value = LoginUiState.Success
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error("Accesso fallito: ${e.message}")
            }*/_uiState.value = LoginUiState.Success
        }
    }

    fun loginWithGoogle(token: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                SupabaseInstance.client.auth.signInWith(IDToken) {
                    idToken = token
                    provider = Google
                }
                _uiState.value = LoginUiState.Success
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error("Errore Google: ${e.message}")
            }
        }
    }
    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }

}
