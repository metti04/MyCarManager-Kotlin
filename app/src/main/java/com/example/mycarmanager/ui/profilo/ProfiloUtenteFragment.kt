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
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter

// Fragment che gestisce la visualizzazione del profilo dell'utente.
class ProfiloUtenteFragment : Fragment() {

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
        
        val userEmail = arguments?.getString("USER_EMAIL")
        viewModel.caricaDatiUtente(userEmail)

        setupButtons(view)
        osservaProfilo(view)
    }

    // Configura i pulsanti del fragment.
    private fun setupButtons(view: View) {
        // Gestione Logout
        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            viewModel.logout()
        }

        // Gestione passaggio alla schermata di Modifica Dati
        view.findViewById<Button>(R.id.btnModificaDati).setOnClickListener {
            // Recuperiamo l'email corrente per passarla al prossimo fragment
            val userEmail = arguments?.getString("USER_EMAIL")
            
            // Creiamo il nuovo fragment e iniettiamo l'email tramite un Bundle di argomenti
            val fragment = ModificaDatiUtenteFragment().apply {
                arguments = Bundle().apply {
                    putString("USER_EMAIL", userEmail)
                }
            }
            
            // Eseguiamo la transazione per sostituire il fragment corrente con quello di modifica.
            // addToBackStack permette all'utente di tornare al profilo premendo il tasto 'Indietro' del telefono.
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
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
                            view.findViewById<TextView>(R.id.tvUsernameUtenteHeader).text = "${u.nome} ${u.cognome}"
                            view.findViewById<TextView>(R.id.tvUsernameUtente).text = "Username: ${u.username}"
                            view.findViewById<TextView>(R.id.tvNome).text = "Nome: ${u.nome}"
                            view.findViewById<TextView>(R.id.tvCognome).text = "Cognome: ${u.cognome}"
                            view.findViewById<TextView>(R.id.tvEmail).text = "Email: ${u.email}"
                            
                            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            view.findViewById<TextView>(R.id.tvDataDiNascita).text = "Data di nascita: ${u.dataDiNascita.toJavaLocalDate().format(formatter)}"
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
