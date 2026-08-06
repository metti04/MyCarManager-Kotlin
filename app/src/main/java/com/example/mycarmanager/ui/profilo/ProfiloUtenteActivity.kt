package com.example.mycarmanager.ui.profilo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mycarmanager.R
import com.example.mycarmanager.ui.auth.LoginActivity
import com.example.mycarmanager.ui.home.HomeActivity
import com.example.mycarmanager.ui.scadenze.ScadenzeAutoCensiteActivity
import com.example.mycarmanager.ui.spese.SpeseAutoCensiteActivity
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

// Activity che gestisce la visualizzazione del profilo dell'utente.
// Permette di visualizzare i dati personali, effettuare il logout e accedere alla modifica dei dati.
class ProfiloUtenteActivity : AppCompatActivity() {

    private val viewModel: ProfiloUtenteActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profilo_utente)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clProfiloUtente)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupNavbar()
        setupButtons()
        osservaProfilo()
    }

    // Configura la barra di navigazione inferiore.
    // Imposta i listener per i click sulle diverse sezioni (Garage, Spese, Scadenze).
    private fun setupNavbar() {
        val navbar = findViewById<View>(R.id.nb)

        navbar.findViewById<LinearLayout>(R.id.llgarage).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        navbar.findViewById<LinearLayout>(R.id.llspese).setOnClickListener {
            startActivity(Intent(this, SpeseAutoCensiteActivity::class.java))
            finish()
        }

        navbar.findViewById<LinearLayout>(R.id.llscadenze).setOnClickListener {
            startActivity(Intent(this, ScadenzeAutoCensiteActivity::class.java))
            finish()
        }

        navbar.findViewById<LinearLayout>(R.id.llprofilo).setOnClickListener {
            // Già qui
        }
    }

    // Configura i pulsanti dell'Activity.
    // Gestisce il logout e l'apertura della schermata di modifica dati.
    private fun setupButtons() {
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            viewModel.logout()
        }

        findViewById<Button>(R.id.btnModificaDati).setOnClickListener {
            startActivity(Intent(this, ModificaDatiUtenteActivity::class.java))
        }
    }

    // Osserva lo stato del profilo esposto dal ViewModel.
    // Aggiorna la UI con i dati dell'utente o gestisce il logout e gli errori.
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
    }
}
