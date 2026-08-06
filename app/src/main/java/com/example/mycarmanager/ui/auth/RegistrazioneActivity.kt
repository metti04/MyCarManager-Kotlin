package com.example.mycarmanager.ui.auth

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
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
import com.example.mycarmanager.MainActivity
import com.example.mycarmanager.R
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * Activity che gestisce la schermata di registrazione di un nuovo utente.
 * Utilizza il pattern MVVM interagendo con RegistrazioneActivityViewModel.
 */
class RegistrazioneActivity : AppCompatActivity() {

    // Inizializzazione del ViewModel tramite delega viewModels()
    // Questo permette al ViewModel di sopravvivere ai cambiamenti di configurazione (es. rotazione schermo)
    private val viewModel: RegistrazioneActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Abilita la modalità Edge-to-Edge configurando il colore della barra di navigazione uguale allo sfondo (Blu)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(
                ContextCompat.getColor(this, R.color.Bianco),
                ContextCompat.getColor(this, R.color.Bianco)
            )
        )
        setContentView(R.layout.activity_registrazione)

        // Gestione dei padding per evitare che il contenuto finisca sotto la barra di stato o di navigazione
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clregistrazione)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            
            // Applichiamo solo il padding superiore al root (per la barra di stato blu)
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            
            // Applichiamo il padding inferiore al contenuto bianco (per la tastiera e nav bar)
            findViewById<View>(R.id.llFormRegistrazione).setPadding(
                24,
                24,
                24,
                systemBars.bottom + ime.bottom + 100
            )
            insets
        }

        // Configura gli elementi grafici e i listener
        setupUI()
        // Inizia l'osservazione dello stato del ViewModel
        observeRegistrazione()
    }

    /**
     * Inizializza i componenti della UI e imposta i listener per i click.
     */
    private fun setupUI() {
        // Recupero dei riferimenti agli elementi del layout tramite findViewById
        val etNome = findViewById<TextInputEditText>(R.id.txNome)
        val etCognome = findViewById<TextInputEditText>(R.id.txCognome)
        val etEmail = findViewById<TextInputEditText>(R.id.txEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.txPassword)
        val etData = findViewById<TextInputEditText>(R.id.txDataNascita)
        val etUsername = findViewById<TextInputEditText>(R.id.txUsername)
        val btnRegistrati = findViewById<Button>(R.id.txregistrati)
        val tvAccedi = findViewById<TextView>(R.id.txAccedi)

        // Apre il selettore di data quando si clicca sul campo data di nascita
        etData.setOnClickListener {
            showDatePicker(etData)
        }

        // Chiude l'activity corrente per tornare alla schermata di Login
        tvAccedi.setOnClickListener {
            finish()
        }

        // Invia i dati al ViewModel per il processo di registrazione
        btnRegistrati.setOnClickListener {
            viewModel.registerUser(
                etNome.text.toString(),
                etCognome.text.toString(),
                etEmail.text.toString(),
                etPassword.text.toString(),
                etData.text.toString(),
                etUsername.text.toString()
            )
        }
    }

    /**
     * Osserva lo stato (UiState) esposto dal ViewModel e reagisce ai cambiamenti.
     * Utilizza lifecycleScope per gestire correttamente il ciclo di vita della Coroutine.
     */
    private fun observeRegistrazione() {
        lifecycleScope.launch {
            // repeatOnLifecycle assicura che il Flow venga ascoltato solo quando l'Activity è visibile (in stato STARTED)
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is RegistrationUiState.Idle -> {
                            // Stato iniziale: l'app è in attesa di input dall'utente
                        }
                        is RegistrationUiState.Loading -> {
                            // Feedback visivo: informa l'utente che la registrazione è in corso
                            Toast.makeText(this@RegistrazioneActivity, "Registrazione in corso...", Toast.LENGTH_SHORT).show()
                        }
                        is RegistrationUiState.Success -> {
                            // Registrazione avvenuta con successo: feedback e chiusura schermata
                            Toast.makeText(this@RegistrazioneActivity, "Registrazione completata!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@RegistrazioneActivity, MainActivity::class.java))
                            finish()
                        }
                        is RegistrationUiState.Error -> {
                            // Mostra il messaggio d'errore ricevuto dal ViewModel (es. validazione fallita)
                            Toast.makeText(this@RegistrazioneActivity, state.message, Toast.LENGTH_LONG).show()
                            // Reset dello stato per permettere all'utente di correggere i dati e riprovare
                            viewModel.resetState()
                        }
                    }
                }
            }
        }
    }

    /**
     * Mostra un DatePickerDialog per permettere all'utente di selezionare la propria data di nascita.
     * Formatta la data selezionata in "dd/MM/yyyy" e la inserisce nell'EditText.
     */
    private fun showDatePicker(editText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val format = SimpleDateFormat("dd/MM/yyyy", Locale.ITALY)
                editText.setText(format.format(selectedDate.time))
            },
            year,
            month,
            day
        )
        // Impedisce la selezione di date future impostando come limite massimo il tempo attuale
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
}
