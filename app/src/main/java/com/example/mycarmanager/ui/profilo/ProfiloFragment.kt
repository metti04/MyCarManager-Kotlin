package com.example.mycarmanager.ui.profilo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mycarmanager.R

class ProfiloFragment : Fragment() {

    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fragment_spesa_auto)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the XML layout for this fragment
        return inflater.inflate(R.layout.fragment_profilo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI elements here using 'view.findViewById'
        //val fabAddVeicolo = view.findViewById<ExtendedFloatingActionButton>(R.id.efabAddVeicolo)
        //fabAddVeicolo.setOnClickListener {
        // Action for adding vehicle
        //}

        // Set up RecyclerView, adapters, etc.
    }

    /*
    private fun osservaProfilo() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ProfileUiState.Loading -> {
                            // Eventuale progress bar
                        }
                        is ProfileUiState.Success -> {
                            val u = state.utente
                            findViewById<TextView>(R.id.tvUsernameUtente).text = "Username: ${u.username}"
                            findViewById<TextView>(R.id.tvNome).text = "Nome: ${u.nome}"
                            findViewById<TextView>(R.id.tvCognome).text = "Cognome: ${u.cognome}"
                            findViewById<TextView>(R.id.tvEmail).text = "Email: ${u.email}"

                            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            findViewById<TextView>(R.id.tvDataDiNascita).text = "Data di nascita: ${u.dataDiNascita.format(formatter)}"
                        }
                        is ProfileUiState.Logout -> {
                            Toast.makeText(this@ProfiloUtenteActivity, "Logout effettuato", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@ProfiloUtenteActivity, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                        is ProfileUiState.Error -> {
                            Toast.makeText(this@ProfiloUtenteActivity, state.message, Toast.LENGTH_LONG).show()
                        }
                        else -> {}
                    }
                }
            }
        }
    }*/
}

