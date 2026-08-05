package com.example.mycarmanager.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Rappresenta i possibili stati della schermata di registrazione.
 * - Idle: Stato iniziale, in attesa di input.
 * - Loading: Operazione di registrazione in corso.
 * - Success: Registrazione completata con successo.
 * - Error: Si è verificato un problema (contiene il messaggio d'errore).
 */
sealed class RegistrationUiState {
    object Idle : RegistrationUiState()
    object Loading : RegistrationUiState()
    object Success : RegistrationUiState()
    data class Error(val message: String) : RegistrationUiState()
}

class RegistrazioneActivityViewModel : ViewModel() {

    // _uiState è privato per proteggere lo stato da modifiche esterne
    private val _uiState = MutableStateFlow<RegistrationUiState>(RegistrationUiState.Idle)
    
    // uiState espone lo stato all'Activity in sola lettura tramite StateFlow
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    /**
     * Esegue il processo di registrazione dell'utente.
     * Include validazione dei campi e simulazione di una chiamata asincrona.
     */
    fun registerUser(nome: String, cognome: String, email: String, pass: String, data: String, user: String) {
        
        // 1. Validazione: Controllo campi vuoti
        if (nome.isBlank() || cognome.isBlank() || email.isBlank() || pass.isBlank() || data.isBlank() || user.isBlank()) {
            _uiState.value = RegistrationUiState.Error("Tutti i campi sono obbligatori")
            return
        }

        // 2. Validazione: Formato Email tramite pattern Android standard
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = RegistrationUiState.Error("Email non valida")
            return
        }

        // 3. Validazione: Lunghezza minima password
        if (pass.length < 6) {
            _uiState.value = RegistrationUiState.Error("La password deve avere almeno 6 caratteri")
            return
        }

        // 4. Esecuzione logica di registrazione in una Coroutine
        viewModelScope.launch {
            // Imposta lo stato su Loading per informare la UI
            _uiState.value = RegistrationUiState.Loading
            
            // Simulazione di una chiamata di rete o database (es. 2 secondi)
            delay(2000)
            
            // TODO: Inserire qui la logica reale (integrazione con Firebase o API Server)
            
            // Se tutto va a buon fine, imposta lo stato su Success
            _uiState.value = RegistrationUiState.Success
        }
    }

    /**
     * Riporta lo stato a Idle. Utile per resettare la UI dopo aver mostrato un errore.
     */
    fun resetState() {
        _uiState.value = RegistrationUiState.Idle
    }
}
