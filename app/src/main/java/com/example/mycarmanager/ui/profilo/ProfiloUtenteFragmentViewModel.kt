package com.example.mycarmanager.ui.profilo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycarmanager.MainActivity
import com.example.mycarmanager.dbServices.data.UtenteDbServices
import com.example.mycarmanager.dbServices.model.Utente
import com.example.mycarmanager.dbServices.supabase.SupabaseInstance
import com.example.mycarmanager.ui.home.HomeFragment
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Stati possibili per la UI del profilo.
sealed class ProfileUiState {
    object Idle : ProfileUiState()
    object Loading : ProfileUiState()
    data class Success(val utente: Utente) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
    object Logout : ProfileUiState()
}

// ViewModel per la gestione del profilo utente.
// Si occupa di caricare i dati da Supabase e gestire la sessione di logout.
class ProfiloUtenteActivityViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val dbService = UtenteDbServices()

    init {
        // Il caricamento verrà avviato dal Fragment passando l'email
    }

    // Carica i dati dell'utente dal database usando l'email fornita.
    fun caricaDatiUtente(email: String?) {
        if (email.isNullOrBlank()) {
            _uiState.value = ProfileUiState.Error("Email utente non disponibile")
            return
        }

        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            try {
                val utente = dbService.getUtenteByEmail(email)
                if (utente != null) {
                    _uiState.value = ProfileUiState.Success(utente)
                } else {
                    _uiState.value = ProfileUiState.Error("Dati utente non trovati nel database")
                }
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Errore caricamento: ${e.message}")
            }
        }
    }

    // Effettua il logout dell'utente chiudendo la sessione in Supabase.
    fun logout() {
        viewModelScope.launch {
            try {
                SupabaseInstance.client.auth.signOut()
                _uiState.value = ProfileUiState.Logout
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Errore logout: ${e.message}")
            }
        }
    }
}
