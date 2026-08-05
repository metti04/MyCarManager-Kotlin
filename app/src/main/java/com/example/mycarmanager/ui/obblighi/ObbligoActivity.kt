package com.example.mycarmanager.ui.obblighi

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mycarmanager.R
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ObbligoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Se non hai ancora un layout activity_obbligo.xml, commenta la riga sotto
        // o creane uno. Per ora lo lascio commentato se non lo trovo.
        setContentView(R.layout.activity_obbligo)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etDataPagamento = findViewById<TextInputEditText>(R.id.etDataPagamento)
        etDataPagamento?.setOnClickListener {
            showDatePicker(etDataPagamento)
        }

        val etDataScadenza = findViewById<TextInputEditText>(R.id.etDataScadenza)
        etDataScadenza?.setOnClickListener {
            showDatePicker(etDataScadenza)
        }
    }

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
        // Imposta la data massima a oggi per evitare date future
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
}
