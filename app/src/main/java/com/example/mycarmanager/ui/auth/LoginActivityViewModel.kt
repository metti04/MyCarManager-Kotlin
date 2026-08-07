package com.example.mycarmanager.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycarmanager.dbServices.data.UtenteDbServices
import com.example.mycarmanager.dbServices.model.Utente
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
    data class Success(val email: String) : LoginUiState()
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
            try {
                val dbService = UtenteDbServices()
                val utente = dbService.getUtenteByEmail(email.trim())
                
                if (utente != null && utente.password == pass) {
                    _uiState.value = LoginUiState.Success(email.trim())
                } else {
                    _uiState.value = LoginUiState.Error("Email o Password errati")
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error("Accesso fallito: ${e.message}")
                Log.d("LoginActivityViewModel", "Accesso fallito: ${e.message}")
            }
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

                /*// Creiamo l'oggetto Utente per il database
                val nuovoUtente = Utente(
                    username = user,
                    password = pass,
                    email = email,
                    nome = nome,
                    cognome = cognome,
                    dataDiNascita = localDate
                )

                // Inseriamo i dettagli dell'utente
                val dbService = UtenteDbServices()
                dbService.inserisciUtente(nuovoUtente)
*/              val user = SupabaseInstance.client.auth.currentUserOrNull()
                val email = user?.email ?: ""
                _uiState.value = LoginUiState.Success(email)
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error("Errore Google: ${e.message}")
            }
        }
    }
    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }

}
