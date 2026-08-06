package com.example.mycarmanager.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycarmanager.dbServices.model.Utente
import com.example.mycarmanager.dbServices.supabase.SupabaseInstance
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeActivityViewModel : ViewModel() {

    private val _username = MutableStateFlow("Utente")
    val username: StateFlow<String> = _username.asStateFlow()

    init {
        recuperaProfilo()
    }

    private fun recuperaProfilo() {
        viewModelScope.launch {
            try {
                val user = SupabaseInstance.client.auth.currentUserOrNull()
                // Se abbiamo un utente loggato in Supabase, prendiamo la sua mail o nome
                user?.email?.let {
                    _username.value = it.substringBefore("@")
                }
            } catch (e: Exception) {
                // In caso di errore restiamo con "Utente"
            }
        }
    }
}
