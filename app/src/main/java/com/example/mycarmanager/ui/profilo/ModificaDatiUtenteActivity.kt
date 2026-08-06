package com.example.mycarmanager.ui.profilo

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mycarmanager.R
import com.example.mycarmanager.ui.home.HomeActivity
import com.example.mycarmanager.ui.scadenze.ScadenzeAutoCensiteActivity
import com.example.mycarmanager.ui.spese.SpeseAutoCensiteActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

// Activity che gestisce la schermata di modifica dei dati personali dell'utente.
class ModificaDatiUtenteActivity : AppCompatActivity() {

    private val viewModel: ModificaDatiUtenteActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(
                ContextCompat.getColor(this, R.color.Bianco),
                ContextCompat.getColor(this, R.color.Bianco)
            )
        )
        setContentView(R.layout.activity_modifica_dati_utente)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clProfiloUtente)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            findViewById<View>(R.id.llModificaDatiUtente).setPadding(24, 24, 24, systemBars.bottom + ime.bottom + 50)
            insets
        }

        setupNavbar()
        setupButtons()
        osservaStato()
    }

    // Configura la barra di navigazione inferiore.
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
            startActivity(Intent(this, ProfiloUtenteActivity::class.java))
            finish()
        }
    }

    // Configura i pulsanti e i listener della schermata.
    // Gestisce la selezione della data, l'annullamento e il salvataggio dei dati.
    private fun setupButtons() {
        val etData = findViewById<TextInputEditText>(R.id.txDataNascita)
        etData.setOnClickListener {
            showDatePicker(etData)
        }

        findViewById<Button>(R.id.btnAnnulla).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnSalva).setOnClickListener {
            viewModel.salvaModifiche(
                findViewById<TextInputEditText>(R.id.txNome).text.toString(),
                findViewById<TextInputEditText>(R.id.txCognome).text.toString(),
                findViewById<TextInputEditText>(R.id.txEmail).text.toString(),
                findViewById<TextInputEditText>(R.id.txPassword).text.toString(),
                findViewById<TextInputEditText>(R.id.txDataNascita).text.toString(),
                findViewById<TextInputEditText>(R.id.txUsername).text.toString()
            )
        }
    }

    // Osserva lo stato di modifica del profilo dal ViewModel.
    // Gestisce il caricamento dei dati iniziali, il successo del salvataggio e gli errori.
    private fun osservaStato() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is EditProfileUiState.Loading -> {
                            // Progress
                        }
                        is EditProfileUiState.Loaded -> {
                            val u = state.utente
                            findViewById<TextInputEditText>(R.id.txNome).setText(u.nome)
                            findViewById<TextInputEditText>(R.id.txCognome).setText(u.cognome)
                            findViewById<TextInputEditText>(R.id.txEmail).setText(u.email)
                            findViewById<TextInputEditText>(R.id.txPassword).setText(u.password)
                            findViewById<TextInputEditText>(R.id.txUsername).setText(u.username)
                            
                            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            findViewById<TextInputEditText>(R.id.txDataNascita).setText(u.dataDiNascita.format(formatter))
                        }
                        is EditProfileUiState.Success -> {
                            Toast.makeText(this@ModificaDatiUtenteActivity, "Dati aggiornati!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        is EditProfileUiState.Error -> {
                            Toast.makeText(this@ModificaDatiUtenteActivity, state.message, Toast.LENGTH_LONG).show()
                            viewModel.resetState()
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    // Mostra un DatePickerDialog per selezionare la data di nascita.
    private fun showDatePicker(editText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val format = SimpleDateFormat("dd/MM/yyyy", Locale.ITALY)
                editText.setText(format.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
}
