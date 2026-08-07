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
import kotlinx.datetime.toKotlinLocalDate
import java.time.format.DateTimeFormatter

// Stati possibili per la UI di modifica profilo.
sealed class EditProfileUiState {
    object Idle : EditProfileUiState()
    object Loading : EditProfileUiState()
    data class Loaded(val utente: Utente) : EditProfileUiState()
    object Success : EditProfileUiState()
    data class Error(val message: String) : EditProfileUiState()
}

/**
 * ViewModel per la gestione della modifica dei dati utente.
 * Implementa la logica di business per caricare i dati attuali e salvare le nuove informazioni.
 */
class ModificaDatiUtenteActivityViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<EditProfileUiState>(EditProfileUiState.Idle)
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private val dbService = UtenteDbServices()

    // Carica i dati dell'utente dal database per pre-popolare i campi di modifica.
    // Riceve l'email passata dal Fragment per coerenza con il sistema di navigazione basato su Intent/Bundle.
    fun caricaDatiAttuali(emailPassata: String?) {
        viewModelScope.launch {
            _uiState.value = EditProfileUiState.Loading
            try {
                // Tentiamo di usare l'email passata (metodo manuale), 
                // con un fallback sulla sessione di Supabase se necessario.
                val email = emailPassata ?: SupabaseInstance.client.auth.currentUserOrNull()?.email
                
                if (!email.isNullOrBlank()) {
                    val utente = dbService.getUtenteByEmail(email)
                    if (utente != null) {
                        // Stato 'Loaded': i dati sono pronti per essere mostrati nella UI
                        _uiState.value = EditProfileUiState.Loaded(utente)
                    } else {
                        _uiState.value = EditProfileUiState.Error("Utente non trovato nel database")
                    }
                } else {
                    _uiState.value = EditProfileUiState.Error("Email non disponibile")
                }
            } catch (e: Exception) {
                _uiState.value = EditProfileUiState.Error("Errore nel caricamento dati: ${e.message}")
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
                val localDate = java.time.LocalDate.parse(data, formatter).toKotlinLocalDate()

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
