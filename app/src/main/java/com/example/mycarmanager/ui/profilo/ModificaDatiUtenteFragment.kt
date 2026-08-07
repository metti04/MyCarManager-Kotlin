package com.example.mycarmanager.ui.profilo

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mycarmanager.R
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import kotlinx.datetime.toJavaLocalDate
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Fragment per la modifica dei dati del profilo utente.
 * Gestisce l'interfaccia di editing e comunica con il ViewModel per il caricamento e salvataggio.
 */
class ModificaDatiUtenteFragment : Fragment() {

    private val viewModel: ModificaDatiUtenteActivityViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate del layout specifico per la modifica dati
        return inflater.inflate(R.layout.activity_modifica_dati_utente, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Gestione padding per la tastiera (come in Login/Registrazione)
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.clProfiloUtente)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            
            // Padding superiore impostato a 0 per permettere all'header di arrivare alla barra delle notifiche
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            
            // Padding inferiore dinamico per la tastiera applicato al LinearLayout interno
            view.findViewById<View>(R.id.llModificaDatiUtente).setPadding(24, 24, 24, systemBars.bottom + ime.bottom + 100)
            insets
        }

        // Recupero l'email dell'utente passata come argomento dal Fragment precedente (ProfiloUtenteFragment).
        // Questo garantisce che stiamo modificando i dati dell'utente corretto.
        val userEmail = arguments?.getString("USER_EMAIL")
        
        // Chiedo al ViewModel di caricare i dati attuali dal DB usando l'email appena recuperata.
        viewModel.caricaDatiAttuali(userEmail)
        
        setupButtons(view)
        osservaStato(view)
    }

    // Configura i pulsanti e i listener della schermata.
    private fun setupButtons(view: View) {
        val etData = view.findViewById<TextInputEditText>(R.id.txDataNascita)
        etData.setOnClickListener {
            showDatePicker(etData)
        }

        view.findViewById<Button>(R.id.btnAnnulla).setOnClickListener {
            // Torna indietro al fragment precedente nella cronologia (il profilo)
            parentFragmentManager.popBackStack()
        }

        view.findViewById<Button>(R.id.btnSalva).setOnClickListener {
            viewModel.salvaModifiche(
                view.findViewById<TextInputEditText>(R.id.txNome).text.toString(),
                view.findViewById<TextInputEditText>(R.id.txCognome).text.toString(),
                view.findViewById<TextInputEditText>(R.id.txEmail).text.toString(),
                view.findViewById<TextInputEditText>(R.id.txPassword).text.toString(),
                view.findViewById<TextInputEditText>(R.id.txDataNascita).text.toString(),
                view.findViewById<TextInputEditText>(R.id.txUsername).text.toString()
            )
        }
    }

    // Osserva lo stato di modifica del profilo dal ViewModel.
    private fun osservaStato(view: View) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is EditProfileUiState.Loading -> {
                            // Progress
                        }
                        is EditProfileUiState.Loaded -> {
                            val u = state.utente
                            view.findViewById<TextView>(R.id.tvUsernameUtenteHeader).text = "${u.nome} ${u.cognome}"
                            view.findViewById<TextInputEditText>(R.id.txNome).setText(u.nome)
                            view.findViewById<TextInputEditText>(R.id.txCognome).setText(u.cognome)
                            view.findViewById<TextInputEditText>(R.id.txEmail).setText(u.email)
                            view.findViewById<TextInputEditText>(R.id.txPassword).setText(u.password)
                            view.findViewById<TextInputEditText>(R.id.txUsername).setText(u.username)
                            
                            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            view.findViewById<TextInputEditText>(R.id.txDataNascita).setText(u.dataDiNascita.toJavaLocalDate().format(formatter))
                        }
                        is EditProfileUiState.Success -> {
                            Toast.makeText(requireContext(), "Dati aggiornati!", Toast.LENGTH_SHORT).show()
                            // In caso di successo, chiudiamo questo fragment e torniamo al profilo
                            parentFragmentManager.popBackStack()
                        }
                        is EditProfileUiState.Error -> {
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
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
            requireContext(),
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
