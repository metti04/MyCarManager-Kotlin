package com.example.mycarmanager.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycarmanager.dbServices.data.UtenteDbServices
import com.example.mycarmanager.dbServices.model.Utente
import com.example.mycarmanager.dbServices.supabase.SupabaseInstance
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
            
            try {
                // Parsing della data selezionata (dd/MM/yyyy -> LocalDate)
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val localDate = LocalDate.parse(data, formatter)

                // Creiamo l'oggetto Utente per il database
                val nuovoUtente = Utente(
                    username = user,
                    password = pass,
                    email = email,
                    nome = nome,
                    cognome = cognome,
                    dataDiNascita = localDate
                )

                // 1. Creiamo l'account in Supabase Auth
                SupabaseInstance.client.auth.signUpWith(Email) {
                    this.email = email
                    this.password = pass
                }

                // 2. Inseriamo i dettagli dell'utente nella nostra tabella personalizzata
                val dbService = UtenteDbServices()
                dbService.inserisciUtente(nuovoUtente)

                // Se tutto va a buon fine, imposta lo stato su Success
                _uiState.value = RegistrationUiState.Success
            } catch (e: Exception) {
                // In caso di errore (es. email già esistente o problema di rete)
                Log.d("Registazione", "Errore durante la registrazione: ${e.message}")
                _uiState.value = RegistrationUiState.Error("Errore durante la registrazione: ${e.message}")
            }
        }
    }

    /**
     * Riporta lo stato a Idle. Utile per resettare la UI dopo aver mostrato un errore.
     */
    fun resetState() {
        _uiState.value = RegistrationUiState.Idle
    }
}
