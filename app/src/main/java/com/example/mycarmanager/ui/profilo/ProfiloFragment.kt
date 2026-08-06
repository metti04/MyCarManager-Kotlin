package com.example.mycarmanager.ui.profilo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mycarmanager.R
import com.example.mycarmanager.ui.auth.LoginActivity
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

// Fragment che gestisce la visualizzazione del profilo dell'utente.
class ProfiloFragment : Fragment() {

    private val viewModel: ProfiloUtenteActivityViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate del layout specifico per il profilo
        return inflater.inflate(R.layout.fragment_profilo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupButtons(view)
        osservaProfilo(view)
    }

    // Configura i pulsanti del fragment.
    private fun setupButtons(view: View) {
        // Gestione Logout
        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            viewModel.logout()
        }

        // Gestione Modifica Dati
        view.findViewById<Button>(R.id.btnModificaDati).setOnClickListener {
            // Se vuoi restare con le Activity per la modifica:
            val intent = Intent(requireContext(), ModificaDatiUtenteFragment::class.java)
            startActivity(intent)
        }
    }

    // Osserva lo stato del profilo dal ViewModel.
    private fun osservaProfilo(view: View) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ProfileUiState.Loading -> {
                            // Qui potresti mostrare un caricamento
                        }
                        is ProfileUiState.Success -> {
                            val u = state.utente
                            view.findViewById<TextView>(R.id.tvUsernameUtente).text = "Username: ${u.username}"
                            view.findViewById<TextView>(R.id.tvNome).text = "Nome: ${u.nome}"
                            view.findViewById<TextView>(R.id.tvCognome).text = "Cognome: ${u.cognome}"
                            view.findViewById<TextView>(R.id.tvEmail).text = "Email: ${u.email}"
                            
                            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            view.findViewById<TextView>(R.id.tvDataDiNascita).text = "Data di nascita: ${u.dataDiNascita.format(formatter)}"
                        }
                        is ProfileUiState.Logout -> {
                            // In caso di logout, torna alla schermata di Login
                            val intent = Intent(requireContext(), LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                        is ProfileUiState.Error -> {
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}
