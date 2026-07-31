package com.example.mycarmanager.ui.auth

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mycarmanager.R
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class RegistrazioneActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrazione)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clregistrazione)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Listener per tornare al login tramite TextView
        findViewById<TextView>(R.id.txAccedi)?.setOnClickListener {
            finish() // Chiude l'activity e torna alla precedente (Login)
        }

        findViewById<Button>(R.id.txregistrati)?.setOnClickListener {
            Toast.makeText(this, "Registrazione completata!", Toast.LENGTH_SHORT).show()
            finish()
        }

        val etData = findViewById<TextInputEditText>(R.id.etDataNascita)
        etData?.setOnClickListener {
            showDatePicker(etData)
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
