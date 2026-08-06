package com.example.mycarmanager.ui.profilo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycarmanager.dbServices.data.UtenteDbServices
import com.example.mycarmanager.dbServices.model.Utente
import com.example.mycarmanager.dbServices.supabase.SupabaseInstance
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Stati possibili per la UI di modifica profilo.
sealed class EditProfileUiState {
    object Idle : EditProfileUiState()
    object Loading : EditProfileUiState()
    data class Loaded(val utente: Utente) : EditProfileUiState()
    object Success : EditProfileUiState()
    data class Error(val message: String) : EditProfileUiState()
}

// ViewModel per la gestione della modifica dei dati utente.
// Si occupa del caricamento dei dati attuali e del salvataggio delle modifiche.
class ModificaDatiUtenteActivityViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<EditProfileUiState>(EditProfileUiState.Idle)
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private val dbService = UtenteDbServices()

    init {
        caricaDatiAttuali()
    }

    // Carica i dati attuali dell'utente per pre-popolare i campi di modifica.
    private fun caricaDatiAttuali() {
        viewModelScope.launch {
            _uiState.value = EditProfileUiState.Loading
            try {
                val user = SupabaseInstance.client.auth.currentUserOrNull()
                if (user != null) {
                    val email = user.email ?: ""
                    val utente = dbService.getUtenteByEmail(email)
                    if (utente != null) {
                        _uiState.value = EditProfileUiState.Loaded(utente)
                    } else {
                        _uiState.value = EditProfileUiState.Error("Utente non trovato")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = EditProfileUiState.Error("Errore: ${e.message}")
            }
        }
    }

    // Salva le modifiche effettuate dall'utente nel database Supabase.
    // Esegue la validazione dei campi prima del salvataggio.
    fun salvaModifiche(nome: String, cognome: String, email: String, pass: String, data: String, user: String) {
        if (nome.isBlank() || cognome.isBlank() || email.isBlank() || user.isBlank()) {
            _uiState.value = EditProfileUiState.Error("Nome, cognome, email e username sono obbligatori")
            return
        }

        viewModelScope.launch {
            _uiState.value = EditProfileUiState.Loading
            try {
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val localDate = LocalDate.parse(data, formatter)

                val utenteAggiornato = Utente(
                    username = user,
                    password = pass,
                    email = email,
                    nome = nome,
                    cognome = cognome,
                    dataDiNascita = localDate
                )

                dbService.inserisciUtente(utenteAggiornato)

                _uiState.value = EditProfileUiState.Success
            } catch (e: Exception) {
                _uiState.value = EditProfileUiState.Error("Errore salvataggio: ${e.message}")
            }
        }
    }

    // Resetta lo stato del ViewModel a Idle.
    fun resetState() {
        _uiState.value = EditProfileUiState.Idle
    }
}
