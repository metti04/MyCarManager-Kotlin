package com.example.mycarmanager.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.mycarmanager.MainActivity
import com.example.mycarmanager.R
import com.example.mycarmanager.ui.home.HomeActivity
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Attiva la modalità edge-to-edge
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Gestione degli insets per la visualizzazione corretta con edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clProfiloUtente)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // l'ID CLIENT WEB copiato dalla Google Cloud Console
        val webClientId = "983411459011-7mep3okn625fqj5foatnqpet2h5hofru.apps.googleusercontent.com"

        val credentialManager = CredentialManager.create(this)

        // Configura l'opzione di accesso con Google
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        // Login con Google
        findViewById<View>(R.id.btnGoogle).setOnClickListener {
            lifecycleScope.launch {
                try {
                    val result = credentialManager.getCredential(this@LoginActivity, request)
                    val credential = result.credential

                    // Invece di usare "is", usiamo il metodo statico createFrom che è più robusto
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                        Log.d("GoogleAuth", "Successo! Email: ${googleIdTokenCredential.id}")

                        // Ora navighiamo alla HomeActivity
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()

                    } catch (e: Exception) {
                        Log.e("GoogleAuth", "Errore nel parsing dei dati Google: ${e.message}")
                        // Se arrivi qui, stampa il tipo reale per capire cosa è arrivato
                        Log.d("GoogleAuth", "Tipo reale: ${credential.type}")
                    }

                } catch (e: GetCredentialException) {
                    Log.e("GoogleAuth", "Errore di autenticazione: ${e.message}")
                }
            }
        }

        // Login Classico (Accedi)
        findViewById<Button>(R.id.btnAccedi).setOnClickListener {
            // Per ora navighiamo semplicemente alla MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Navigazione verso la Registrazione
        findViewById<TextView>(R.id.tvRegistrati).setOnClickListener {
            startActivity(Intent(this, RegistrazioneActivity::class.java))
        }
    }
}
